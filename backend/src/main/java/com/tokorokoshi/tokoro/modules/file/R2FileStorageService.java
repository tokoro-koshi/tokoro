package com.tokorokoshi.tokoro.modules.file;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of FileStorageService using AWS S3.
 */
@Service
public class R2FileStorageService implements FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(
            R2FileStorageService.class);

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${aws.bucket}")
    private String bucketName;

    /**
     * Constructs an R2FileStorageService instance.
     */
    public R2FileStorageService(
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.accountId}") String accountId
    ) {
        AwsCredentials credentials = AwsBasicCredentials.create(
                accessKey,
                secretKey
        );
        var endpointUri = URI.create(
                String.format("https://%s.r2.cloudflarestorage.com", accountId)
        );
        this.s3Client = S3Client.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                credentials
                        ))
                .region(Region.of("auto"))
                .endpointOverride(endpointUri)
                .build();

        this.presigner = S3Presigner.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                credentials
                        ))
                .region(Region.of("auto"))
                .endpointOverride(endpointUri)
                .build();

        logger.trace(
                "S3FileStorageService initialized with bucket: {}",
                bucketName
        );
    }

    @Override
    public CompletableFuture<String> createFolder(String folder) {
        logger.trace("Creating folder: {}", folder);
        String normalizedFolder = normalizeFolderPath(folder);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(normalizedFolder)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            s3Client.putObject(request, RequestBody.empty());
            logger.trace("Folder created successfully: {}", normalizedFolder);
            return normalizedFolder;
        }).exceptionally(ex -> {
            throw new RuntimeException(
                    "Failed to create folder: " + folder,
                    ex
            );
        });
    }

    @Override
    public CompletableFuture<String> uploadFile(
            MultipartFile file,
            String folder
    ) {
        logger.trace(
                "Uploading file: {} to folder: {}",
                file.getOriginalFilename(),
                folder
        );
        return CompletableFuture.supplyAsync(() -> {
            try {
                String extension = getFileExtension(
                        Objects.requireNonNull(file.getOriginalFilename())
                );
                String key = buildObjectKey(folder, extension);

                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentDisposition(
                                "inline; filename=\"" + file.getOriginalFilename() + "\"")
                        .contentType(file.getContentType())
                        .contentLength(file.getSize())
                        .build();

                s3Client.putObject(
                        request,
                        RequestBody.fromBytes(file.getBytes())
                );
                logger.trace("File uploaded successfully: {}", key);
                return key;
            } catch (Exception ex) {
                throw new RuntimeException(
                        "Failed to upload file: " + file.getOriginalFilename(),
                        ex
                );
            }
        });
    }

    @Override
    public CompletableFuture<List<String>> uploadFiles(
            @Nullable List<MultipartFile> files,
            String folder
    ) {
        if (files == null || files.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        logger.trace("Uploading {} files to folder: {}", files.size(), folder);

        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> uploadFile(
                        file,
                        folder
                ))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .exceptionally(ex -> {
                    throw new RuntimeException(
                            "Failed to upload files",
                            ex
                    );
                });
    }

    @Override
    public CompletableFuture<byte[]> getFile(String key) {
        logger.trace("Retrieving file: {}", key);
        return CompletableFuture.supplyAsync(() -> {
            var response = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(), ResponseTransformer.toBytes()
            );

            logger.trace("File retrieved successfully: {}", key);
            return response.asByteArray();
        }).exceptionally(ex -> {
            logger.warn("Failed to retrieve file: {}", key, ex);
            return new byte[0];
        });
    }

    @Override
    public CompletableFuture<String> generateSignedUrl(String key) {
        return generateSignedUrl(key, S3Constants.COMMON_EXPIRATION, null);
    }

    @Override
    public CompletableFuture<String> generateSignedUrl(
            String key,
            Integer expirationInSeconds
    ) {
        return generateSignedUrl(key, expirationInSeconds, null);
    }

    @Override
    public CompletableFuture<String> generateSignedUrl(
            String key,
            Integer expirationInSeconds,
            String overrideContentDisposition
    ) {
        logger.trace("Generating signed URL for file: {}", key);
        return CompletableFuture.supplyAsync(() -> {
            GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key);

            if (overrideContentDisposition != null)
                requestBuilder.responseContentDisposition(
                        overrideContentDisposition
                );

            GetObjectRequest request = requestBuilder.build();
            Duration expiration = Duration.ofSeconds(
                    expirationInSeconds != null
                            ? expirationInSeconds
                            : S3Constants.COMMON_EXPIRATION
            );

            String signedUrl = presigner
                    .presignGetObject(r -> r
                            .getObjectRequest(request)
                            .signatureDuration(expiration)
                    )
                    .url()
                    .toString();
            logger.trace("Signed URL generated successfully: {}", signedUrl);
            return signedUrl;
        }).exceptionally(ex -> {
            throw new RuntimeException(
                    "Failed to generate signed URL for file: " + key,
                    ex
            );
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteFile(String key) {
        logger.trace("Deleting file: {}", key);
        return CompletableFuture.supplyAsync(() -> {
            DeleteObjectResponse response = s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
            boolean success = response.sdkHttpResponse().isSuccessful();
            if (success) {
                logger.trace("File deleted successfully: {}", key);
            } else {
                logger.error("Failed to delete file: {}", key);
            }
            return success;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteFolder(String folder) {
        logger.trace("Deleting folder: {}", folder);
        return CompletableFuture.supplyAsync(() -> {
            String normalizedFolder = normalizeFolderPath(folder);
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(
                            bucketName)
                    .prefix(
                            normalizedFolder)
                    .build();

            List<S3Object> objects = s3Client.listObjectsV2(listRequest)
                    .contents();
            if (objects.isEmpty()) {
                logger.trace("Folder is empty: {}", folder);
                return true;
            }

            List<ObjectIdentifier> identifiers = objects.stream()
                    .map(obj -> ObjectIdentifier.builder()
                            .key(obj.key())
                            .build())
                    .toList();

            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder()
                            .objects(identifiers)
                            .build())
                    .build();

            DeleteObjectsResponse response = s3Client.deleteObjects(
                    deleteRequest);
            boolean success = !response.hasErrors();
            if (success) {
                logger.trace("Folder deleted successfully: {}", folder);
            } else {
                logger.error("Failed to delete folder: {}", folder);
            }
            return success;
        });
    }

    @Override
    public CompletableFuture<List<FileEntry>> listFileEntries(
            String directory,
            boolean groupByFolder
    ) {
        logger.trace("Listing entries in directory: {}", directory);
        return CompletableFuture.supplyAsync(() -> {
            String normalizedDir = normalizeDirectoryPath(directory);
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(normalizedDir)
                    .delimiter(groupByFolder ? "/" : null)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            List<FileEntry> entries = new ArrayList<>();

            // Process common prefixes (folders)
            response.commonPrefixes().forEach(prefix ->
                    entries.add(new FileEntry(
                            prefix.prefix(),
                            S3Constants.DIRECTORY_TYPE,
                            null,
                            null
                    )));

            // Process files
            response.contents().stream()
                    .filter(obj -> !obj.key().equals(normalizedDir))
                    .forEach(obj -> entries.add(new FileEntry(
                            obj.key(),
                            "file",
                            obj.size(),
                            obj.lastModified()
                    )));

            logger.trace(
                    "Retrieved {} entries from directory: {}",
                    entries.size(),
                    directory
            );
            return entries;
        }).exceptionally(ex -> {
            throw new RuntimeException(
                    "Failed to list entries in directory: " + directory,
                    ex
            );
        });
    }

    // Helper methods (same as before)
    private String normalizeFolderPath(String folder) {
        String path = folder.startsWith(S3Constants.ROOT_FOLDER)
                ? folder
                : S3Constants.ROOT_FOLDER + folder;
        return path.endsWith("/") ? path : path + "/";
    }

    private String normalizeDirectoryPath(String directory) {
        if (directory == null) return S3Constants.ROOT_FOLDER;
        String path = directory.startsWith(S3Constants.ROOT_FOLDER)
                ? directory
                : S3Constants.ROOT_FOLDER + directory;
        return path.endsWith("/") ? path : path + "/";
    }

    private String buildObjectKey(String folder, String extension) {
        String normalizedFolder = folder != null
                ? normalizeFolderPath(folder)
                : S3Constants.ROOT_FOLDER;
        return normalizedFolder + UUID.randomUUID() + "." + extension;
    }

    private String getFileExtension(String filename) {
        return filename.contains(".")
                ? filename.substring(filename.lastIndexOf(".") + 1)
                : "";
    }
}
