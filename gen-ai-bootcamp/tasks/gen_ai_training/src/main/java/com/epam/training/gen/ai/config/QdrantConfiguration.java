package com.epam.training.gen.ai.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Qdrant Client.
 *
 * <p>This configuration defines a bean that provides a client for interacting with a Qdrant
 * service. The client is built using gRPC to connect to a Qdrant instance running at the specified
 * host and port.
 */
@Configuration
public class QdrantConfiguration {

  @Value("${qdrant.host}")
  private String host;

  @Value("${qdrant.port}")
  private int port;

  @Value("${qdrant.useTls}")
  private boolean useTls;

  /**
   * Creates a {@link QdrantClient} bean for interacting with the Qdrant service.
   *
   * @return an instance of {@link QdrantClient}
   */
  @Bean
  public QdrantClient qdrantClient() {
    return new QdrantClient(QdrantGrpcClient.newBuilder(host, port, useTls).build());
  }
}
