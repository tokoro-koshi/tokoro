package com.tokorokoshi.tokoro.modules.file;

/**
 * Constants for the S3 file storage module.
 */
public class S3Constants {
    /**
     * The expiration time for the signed URL.
     */
    public static final int COMMON_EXPIRATION = 5 * 60;
    /**
     * The root folder in the S3 bucket.
     */
    public static final String ROOT_FOLDER = "";
    /**
     * The folder for user files.
     */
    public static final String USER_FOLDER = "users/";
    /**
     * The folder for blog files.
     */
    public static final String DIRECTORY_TYPE = "directory";
}
