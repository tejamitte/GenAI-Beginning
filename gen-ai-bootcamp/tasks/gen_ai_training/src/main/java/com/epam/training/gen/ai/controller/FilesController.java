package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.UploadResponse;
import com.epam.training.gen.ai.util.DocumentQueryProcessor;
import com.epam.training.gen.ai.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/** This class has endpoints to upload documents of type 1. Text : Should have size <= 10KB */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/files")
public class FilesController {

  private final DocumentQueryProcessor documentQueryProcessor;

  /**
   * @param file file to be uploaded
   * @return upload status message
   */
  @Operation(summary = "Only .txt files allowed with size <= 10KB.")
  @PostMapping(value = "/uploadDoc", consumes = "multipart/form-data")
  public ResponseEntity<String> ragResponse(@RequestParam("file") MultipartFile file)
      throws IOException {
    UploadResponse response = FileUtils.uploadFile(file);
    return switch (response) {
      case EMPTY_FILE -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
      case INVALID_TYPE ->
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only TXT files are allowed.");
      case HAS_MORE_SIZE ->
          ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("File should not be more than or equal to 10KB");
      case DIR_CREATION_ERROR ->
          ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Exception occurred while creating directory : ");
      case FILE_TRANSFER_ERROR ->
          ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Exception occurred while transferring file to path");
      case SUCCESS -> {
        documentQueryProcessor.generateEmbeddings();
        yield ResponseEntity.ok("File uploaded successfully ");
      }
    };
  }
}
