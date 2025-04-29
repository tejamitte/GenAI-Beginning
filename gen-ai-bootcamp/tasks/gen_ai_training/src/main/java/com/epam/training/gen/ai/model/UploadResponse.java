package com.epam.training.gen.ai.model;

/**
 * Constants to decide String response in File processing
 */
public enum UploadResponse {
    /** To return success message.*/
    SUCCESS,

    /** To return if file type is not txt.*/
    INVALID_TYPE,

    /** To return if file has empty content.*/
    EMPTY_FILE,

    /** To return if file size is more than 2KB.*/
    HAS_MORE_SIZE,

    /** To return if directory creation is failed.*/
    DIR_CREATION_ERROR,

    /** To return if file creation with content encountered an error.*/
    FILE_TRANSFER_ERROR
}
