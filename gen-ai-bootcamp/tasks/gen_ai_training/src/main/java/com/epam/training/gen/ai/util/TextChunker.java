package com.epam.training.gen.ai.util;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

  private static final int DEFAULT_OVERLAP_SENTENCES = 1;
  private static final String SEMANTIC_REGEX = "(?<=[\\.\\!\\?])\\s+|\\n\\n";

  public static List<String> chunk(String text) {
    return chunk(text, DEFAULT_OVERLAP_SENTENCES);
  }

  public static List<String> chunk(String text, int overlapSentences) {
    if (text == null || text.isBlank()) return List.of();

    String[] semanticToken = text.split(SEMANTIC_REGEX);
    List<String> chunks = new ArrayList<>();

    for (int i = 0; i < semanticToken.length; i++) {
      StringBuilder chunk = new StringBuilder();
      int start = Math.max(i - overlapSentences, 0);
      for (int j = start; j <= i; j++) {
        chunk.append(semanticToken[j].trim()).append(" ");
      }
      chunks.add(chunk.toString().trim());
    }
    return chunks;
  }
}
