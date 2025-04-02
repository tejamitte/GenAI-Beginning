package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.history.SimpleKernelHistory;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.model.AIResponse;
import com.epam.training.gen.ai.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatBotController {
  private final ChatBotService botService;
  private final SimpleKernelHistory kernelHistory;
  private final RestTemplate restTemplate;

  @Value("${epam.dial.deployment-names-api}")
  private String DEPLOYMENT_NAMES_URL;

  /**
   * @param userPrompt Input from the User
   * @param integratePlugin To work with Mobile Phones plugin
   * @return Response of th eChatBot
   */
  @GetMapping(value = "/getResponse")
  public AIResponse getGeneratedResponse(
      @RequestParam String userPrompt,
      @RequestParam(value = "integratePlugin", required = false) Boolean integratePlugin) {

    return Optional.ofNullable(botService)
        .map(
            chatBotService ->
                chatBotService.getChatBotResponse(
                    userPrompt, integratePlugin != null ? integratePlugin : false))
        .orElseGet(AIResponse::new);
  }

  @PostMapping(value = "/init-chat")
  public AIResponse getResponseFromHistory(@RequestBody UserRequest userRequest) {
    return Optional.ofNullable(kernelHistory)
        .map(kernelHistory -> kernelHistory.processWithHistory(userRequest))
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
