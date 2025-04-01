package com.epam.training.gen.ai.history;

import com.epam.training.gen.ai.model.Chat;
import com.epam.training.gen.ai.model.ChatBotResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for interacting with the AI kernel, maintaining chat history.
 *
 * <p>This service provides a method to process user prompts while preserving chat history. It uses
 * the {@link Kernel} to invoke AI responses based on the user's input and the previous chat
 * context. The conversation history is updated after each interaction.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SimpleKernelHistory {
  private final Kernel kernel;
  private final ChatHistory chatHistory;

  public ChatBotResponse processWithHistory(Chat chat) {

    String prompt = Optional.ofNullable(chat.getPrompt()).orElseThrow();
    chatHistory.addUserMessage(prompt);
    var response =
        kernel
            .invokeAsync(getChat())
            .withArguments(getKernelFunctionArguments(prompt, chatHistory))
            .withPromptExecutionSettings(
                PromptExecutionSettings.builder()
                    .withTemperature(Optional.ofNullable(chat.getTemperature()).orElse(0.5D))
                    .withMaxTokens(
                        Optional.of(chat.getMaxTokens())
                            .filter(tokenValue -> tokenValue != 0)
                            .orElse(1000))
                    .withStopSequences(
                        Optional.ofNullable(chat.getStopSequences()).orElse(List.of()))
                    .build())
            .block();
    String result =
        Optional.ofNullable(response).map(FunctionResult::getResult).orElse("No Response..!");
    chatHistory.addAssistantMessage(result);
    return ChatBotResponse.builder()
        .userPrompt(prompt)
        .chatBotResponse(getBotResponse(chatHistory))
        .build();
  }

  public String getBotResponse(ChatHistory chatHistory) {
    return Optional.of(
            chatHistory.getMessages().stream()
                .map(
                    chatMessageContent -> {
                      String botResponse =
                          " ***** " + chatMessageContent.getAuthorRole().name() + ":";
                      botResponse += chatMessageContent.getContent();
                      return botResponse;
                    })
                .collect(Collectors.joining(" *****   ")))
        .orElse("No Response");
  }

  /**
   * Creates a kernel function for generating a chat response using a predefined prompt template.
   *
   * <p>The template includes the chat history and the user's message as variables.
   *
   * @return a {@link KernelFunction} for handling chat-based AI interactions
   */
  private KernelFunction<String> getChat() {
    return KernelFunction.<String>createFromPrompt(
            """
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
        .build();
  }

  /**
   * Creates the kernel function arguments with the user prompt and chat history.
   *
   * @param prompt the user's input
   * @param chatHistory the current chat history
   * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
   */
  private KernelFunctionArguments getKernelFunctionArguments(
      String prompt, ChatHistory chatHistory) {
    return KernelFunctionArguments.builder()
        .withVariable("request", prompt)
        .withVariable("chatHistory", chatHistory)
        .build();
  }
}
