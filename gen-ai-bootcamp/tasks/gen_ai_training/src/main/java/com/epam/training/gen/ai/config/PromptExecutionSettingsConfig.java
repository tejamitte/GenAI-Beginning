package com.epam.training.gen.ai.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "prompt-execution-settings")
public class PromptExecutionSettingsConfig {
    private Double temperature;
    private int maxTokens;
    private List<String> stopSequences;
    private String modelId;
}
