package com.epam.training.gen.ai.service;

import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QdrantVectorStorageService {
  public static final String INFO = "info";
  public static final int DEFAULT_VECTOR_SIZE = 1536;
  public static final int DEFAULT_TIMEOUT = 5;
  private final QdrantClient qdrantClient;

  @Value("${vector.collection.name}")
  private String collectionName;

  @PostConstruct
  public void ensureCollectionExists() {
    Collections.Distance distance = Collections.Distance.Cosine;

    try {
      qdrantClient
          .getCollectionInfoAsync(collectionName, Duration.ofSeconds(DEFAULT_TIMEOUT))
          .get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

      log.info("Qdrant collection '{}' already exists — skipping creation.", collectionName);
    } catch (Exception ex) {
      log.warn(
          "Qdrant collection '{}' not found or failed to fetch. Attempting to create...",
          collectionName);

      Collections.CreateCollection request =
          Collections.CreateCollection.newBuilder()
              .setCollectionName(collectionName)
              .setVectorsConfig(
                  Collections.VectorsConfig.newBuilder()
                      .setParams(
                          Collections.VectorParams.newBuilder()
                              .setSize(DEFAULT_VECTOR_SIZE)
                              .setDistance(distance)
                              .build())
                      .build())
              .build();

      try {
        qdrantClient.createCollectionAsync(request);
        log.info("Created Qdrant collection '{}'.", collectionName);
      } catch (Exception createEx) {
        log.error(
            "Failed to create Qdrant collection '{}': {}",
            collectionName,
            createEx.getMessage(),
            createEx);
      }
    }
  }

  public void upsert(String input, List<Float> points) {
    try {
      Points.PointStruct pointStruct =
          Points.PointStruct.newBuilder()
              .setId(Points.PointId.newBuilder().setUuid(UUID.randomUUID().toString()).build())
              .setVectors(vectors(points))
              .putAllPayload(Map.of(INFO, value(input)))
              .build();
      var updateResult = qdrantClient.upsertAsync(collectionName, List.of(pointStruct)).get();
      log.info("Insert status : {}", updateResult.getStatus().name());
    } catch (ExecutionException | InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  public List<String> search(List<Float> queryEmbedding, int limit) {
    Points.SearchPoints request =
        Points.SearchPoints.newBuilder()
            .setCollectionName(collectionName)
            .addAllVector(queryEmbedding)
            .setLimit(limit)
            .setWithPayload(Points.WithPayloadSelector.newBuilder().setEnable(true).build())
            .build();

    try {
      return qdrantClient.searchAsync(request).get().stream()
          .map(
              scoredPoint ->
                  scoredPoint
                      .getPayloadMap()
                      .getOrDefault(
                          INFO, JsonWithInt.Value.newBuilder().setStringValue("").build()))
          .map(JsonWithInt.Value::getStringValue)
          .toList();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
