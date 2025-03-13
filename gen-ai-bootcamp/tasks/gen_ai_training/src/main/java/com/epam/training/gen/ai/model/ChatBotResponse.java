package com.epam.training.gen.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatBotResponse {
  private String userPrompt;
  private String chatBotResponse;
}
