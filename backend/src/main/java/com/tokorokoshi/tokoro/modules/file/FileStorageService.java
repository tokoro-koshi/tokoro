package com.tokorokoshi.tokoro.modules.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for file storage operations.
 * Provides methods for managing files and folders in a storage service.
 */
public interface FileStorageService {
    /**
     * Creates a folder in the storage service.
     *
     * @param folder The name of the folder to create.
     * @return A CompletableFuture containing the key of the created folder.
     */
    CompletableFuture<String> createFolder(String folder);

    /**
     * Uploads a file to the storage service.
     *
     * @param file   The file to upload.
     * @param folder The folder to upload the file to (can be null for root folder).
     * @return A CompletableFuture containing the key of the uploaded file.
     */
    CompletableFuture<String> uploadFile(MultipartFile file, String folder);

    /**
     * Uploads multiple files to the storage service.
     *
     * @param files  The list of files to upload.
     * @param folder The folder to upload the files to (can be null for root folder).
     * @return A CompletableFuture containing the list of keys of the uploaded files.
     */
    CompletableFuture<List<String>> uploadFiles(List<MultipartFile> files, String folder);

    /**
     * Retrieves a file from the storage service.
     *
     * @param key The key of the file to retrieve.
     * @return A CompletableFuture containing the file content as a byte array.
     */
    CompletableFuture<byte[]> getFile(String key);

    /**
     * Generates a signed URL for a file in the storage service.
     *
     * @param key                        The key of the file to generate a signed URL for.
     * @param expirationInSeconds        The expiration time of the signed URL in seconds.
     * @param overrideContentDisposition The desired content disposition for the file.
     * @return A CompletableFuture containing the signed URL.
     */
    CompletableFuture<String> generateSignedUrl(String key, Integer expirationInSeconds, String overrideContentDisposition);

    /**
     * Deletes a file from the storage service.
     *
     * @param key The key of the file to delete.
     * @return A CompletableFuture indicating whether the deletion was successful.
     */
    CompletableFuture<Boolean> deleteFile(String key);

    /**
     * Deletes a folder and its contents from the storage service.
     *
     * @param folder The folder to delete.
     * @return A CompletableFuture indicating whether the deletion was successful.
     */
    CompletableFuture<Boolean> deleteFolder(String folder);

    /**
     * Lists the entries in a directory in the storage service.
     *
     * @param directory     The directory to list the entries of.
     * @param groupByFolder Whether to group the entries by folder.
     * @return A CompletableFuture containing the list of file entries.
     */
    CompletableFuture<List<FileEntry>> listFileEntries(String directory, boolean groupByFolder);
}