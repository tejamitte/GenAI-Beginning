package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.AIResponse;
import com.epam.training.gen.ai.model.UserRequest;
import com.epam.training.gen.ai.service.ImageGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ImageGeneratorController {

  private final ImageGeneratorService imageGeneratorService;

  @PostMapping(value = "/generateImage")
  public AIResponse getResponse(@RequestBody UserRequest userRequest) {
    return imageGeneratorService.getResponse(userRequest);
  }
}
