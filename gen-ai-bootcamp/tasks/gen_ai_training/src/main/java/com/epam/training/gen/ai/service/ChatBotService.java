package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.AIResponse;
import com.epam.training.gen.ai.model.MobilePhones;
import com.google.gson.Gson;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatBotService {
  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final InvocationContext invocationContext;

  public AIResponse getChatBotResponse(String userPrompt, boolean integratePlugin) {

    ContextVariableTypes.addGlobalConverter(
        ContextVariableTypeConverter.builder(MobilePhones.class)
            .toPromptString(new Gson()::toJson)
            .build());

    ChatHistory history = new ChatHistory();
    history.addUserMessage(userPrompt);
    List<ChatMessageContent<?>> results;

    results =
        chatCompletionService
            .getChatMessageContentsAsync(
                history, integratePlugin ? kernel : null, invocationContext)
            .block();

    String response =
        Optional.ofNullable(results).orElse(List.of()).stream()
            .filter(
                chatMessageContent ->
                    chatMessageContent.getAuthorRole() == AuthorRole.ASSISTANT
                        && chatMessageContent.getContent() != null)
            .map(ChatMessageContent::getContent)
            .findFirst()
            .orElse("No Output, Something is wrong . . !");

    return AIResponse.builder().userPrompt(userPrompt).aiResponse(response).build();
  }
}
