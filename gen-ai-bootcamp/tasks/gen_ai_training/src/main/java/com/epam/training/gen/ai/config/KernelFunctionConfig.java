package com.epam.training.gen.ai.config;

import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.semanticfunctions.OutputVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** {@code KernelFunctionConfig} creates KernelFunction objects based on given inputs. */
@Slf4j
@Component
@RequiredArgsConstructor
public class KernelFunctionConfig {

  /**
   * Builds KernelFunction with configured model and given PromptExecutionSettings configurations
   *
   * @param prompt user prompt
   * @return Kernel function having type argument
   */
  public KernelFunction<String> buildKernelFunction(String prompt) throws IOException {
    return KernelFunctionFromPrompt.builder()
        .withTemplate(prompt)
        .withDefaultExecutionSettings(
            PromptExecutionSettings.builder()
                .withTemperature(0.2)
                .withMaxTokens(100)
                .withModelId("text-embedding-ada-002")
                .build())
        .withOutputVariable(new OutputVariable<>("response", String.class))
        .build();
  }
}
