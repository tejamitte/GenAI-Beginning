package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.epam.training.gen.ai.util.TextChunker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
  private final OpenAIAsyncClient openAIAsyncClient;
  private final QdrantVectorStorageService vectorStorageService;

  @Value("${model.embedding.name}")
  private String EMBEDDING_MODEL;

  @Value("${model.embedding.limit}")
  private int LIMIT;

  public List<List<Float>> buildEmbedding(String text) {
    List<String> chunks = TextChunker.chunk(text);
    List<List<EmbeddingItem>> embeddingItems = new ArrayList<>();
    chunks.forEach(
        chunk -> {
          var embeddings = retrieveEmbeddings(chunk);
          embeddingItems.add(embeddings);
        });

    return Optional.of(embeddingItems).orElse(List.of()).stream()
        .flatMap(Collection::stream)
        .map(EmbeddingItem::getEmbedding)
        .toList();
  }

  public String storeEmbedding(String input) {

    List<String> chunks = TextChunker.chunk(input);

    chunks.forEach(
        chunk ->
            retrieveEmbeddings(chunk)
                .forEach(
                    embeddingItem ->
                        vectorStorageService.upsert(chunk, embeddingItem.getEmbedding())));
    return "Given input stored to vector database.";
  }

  private List<EmbeddingItem> retrieveEmbeddings(String text) {
    return Objects.requireNonNull(
            openAIAsyncClient
                .getEmbeddings(EMBEDDING_MODEL, new EmbeddingsOptions(List.of(text)))
                .block())
        .getData();
  }

  public List<String> searchEmbeddings(String input) {
    EmbeddingsOptions options = new EmbeddingsOptions(List.of(input));
    List<String> result = new ArrayList<>();
    Objects.requireNonNull(openAIAsyncClient.getEmbeddings(EMBEDDING_MODEL, options).block())
        .getData()
        .forEach(
            embedding ->
                result.addAll(vectorStorageService.search(embedding.getEmbedding(), LIMIT)));
    return result;
  }
}
