package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatBotResponse;
import com.epam.training.gen.ai.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatBotController {
  private final ChatBotService botService;

  /**
   * @param userPrompt Input from the User
   * @param integratePlugin To work with Mobile Phones plugin
   * @return Response of th eChatBot
   */
  @GetMapping(value = "/getResponse")
  public ChatBotResponse getGeneratedResponse(
      @RequestParam String userPrompt,
      @RequestParam(value = "integratePlugin", required = false) Boolean integratePlugin) {

    return Optional.ofNullable(botService)
        .map(
            chatBotService ->
                chatBotService.getChatBotResponse(
                    userPrompt, integratePlugin != null ? integratePlugin : false))
        .orElseGet(ChatBotResponse::new);
  }
}
