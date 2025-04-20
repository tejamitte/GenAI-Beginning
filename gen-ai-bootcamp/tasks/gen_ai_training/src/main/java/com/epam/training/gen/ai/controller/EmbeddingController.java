package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/embeddings")
public class EmbeddingController {

  private final EmbeddingService embeddingService;

  @PostMapping(value = "/build")
  public List<List<Float>> buildEmbeddings(@RequestParam String input) {
    return embeddingService.buildEmbedding(input);
  }

  @PostMapping(value = "/store")
  public ResponseEntity<String> storeEmbedding(@RequestParam String input) {
    return new ResponseEntity<>(embeddingService.storeEmbedding(input), HttpStatus.CREATED);
  }

  @GetMapping(value = "/search")
  public ResponseEntity<List<String>> searchEmbedding(@RequestParam String text) {
    return new ResponseEntity<>(embeddingService.searchEmbeddings(text), HttpStatus.OK);
  }
}
