package com.epam.training.gen.ai.util;

import com.epam.training.gen.ai.model.UploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/** Utility class to process files. It has logic to validate file and upload. */
@Slf4j
public class FileUtils {

  private static final int MAX_TEXT_FILE_SIZE = 10 * 1024;
  private static final String UPLOAD_DIR = System.getProperty("user.dir") + "\\" + "uploaded-files";
  private static final String TXT = ".txt";
  private static final String ERROR_IN_CREATING_DIRECTORY =
      "Exception occurred while creating directory : ";
  private static final String ERROR_IN_TRANSFERRING_FILE_TO_PATH =
      "Exception occurred while transferring file to path - {} : ";
  private static final String SUCCESSFUL_UPLOAD = "File uploaded successfully to . {} ";

  /**
   * Validates file with size, type and then stores to the project directory.
   *
   * <ul>
   *   <li>If file size is more than 2KB and not a text type then returns respective error message.
   *   <li>If directory generation or file generation encounters any error, returns exception.
   * </ul>
   *
   * @param file text file to be uploaded
   * @return upload status message
   */
  public static UploadResponse uploadFile(MultipartFile file) {
    /*Check if file is empty*/
    if (file.isEmpty()) {
      return UploadResponse.EMPTY_FILE;
    }

    /*Validate file type*/
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    if (!fileName.endsWith(TXT)) {
      return UploadResponse.INVALID_TYPE;
    }

    /*
     * Renaming for making uploaded content to have in same file with this name which will be easy
     * during reading
     */
    fileName = "inputText.txt";

    /* Validate file size*/
    if (file.getSize() >= MAX_TEXT_FILE_SIZE) {
      return UploadResponse.HAS_MORE_SIZE;
    }

    Path uploadPath = Paths.get(UPLOAD_DIR);
    /*create directory if not exist*/
    if (!Files.exists(uploadPath)) {
      try {
        Files.createDirectory(uploadPath);
      } catch (IOException e) {
        log.error(ERROR_IN_CREATING_DIRECTORY, e);
        return UploadResponse.DIR_CREATION_ERROR;
      }
    }

    /*Save file*/
    Path filePath = uploadPath.resolve(fileName);
    try {
      file.transferTo(filePath.toFile());
    } catch (IOException e) {
      log.error(ERROR_IN_TRANSFERRING_FILE_TO_PATH, filePath, e);
      return UploadResponse.FILE_TRANSFER_ERROR;
    }
    log.info(SUCCESSFUL_UPLOAD, filePath);
    return UploadResponse.SUCCESS;
  }
}
