package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.AIResponse;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.service.ChatBotService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class ChatBotController {
  private final ChatBotService botService;
  private final ChatBotService chatBotService;
  private final RestTemplate restTemplate;

  @Value("${epam.dial.deployment-names-api}")
  private String DEPLOYMENT_NAMES_URL;

  /**
   * @param userPrompt Input from the User
   * @return Response of th eChatBot
   */
  @GetMapping(value = "/chat")
  public AIResponse getGeneratedResponse(
      @RequestParam String userPrompt) {

    return Optional.ofNullable(botService)
        .map(
            chatBotService ->
                chatBotService.getChatBotResponse(
                    userPrompt))
        .orElseGet(AIResponse::new);
  }

  @PostMapping(value = "/init-chat")
  public AIResponse getResponseFromHistory(@RequestBody UserRequest userRequest) {
    return Optional.ofNullable(chatBotService)
        .map(chatBotService -> chatBotService.processWithHistory(userRequest))
        .orElseGet(AIResponse::new);
  }

  @GetMapping(value = "getDeploymentNames")
  public ResponseEntity<String> getDeploymentNames() {
    return new ResponseEntity<>(
        Optional.ofNullable(restTemplate.getForObject(DEPLOYMENT_NAMES_URL, String.class))
            .orElseThrow(),
        HttpStatus.OK);
  }
}
