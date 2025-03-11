package org.datatraining4j.aws.stream.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class KinesisSourceConfig {

    @NonNull
    private final String secretKey;
    @NonNull
    private final String accessKey;
    @NonNull
    private final String streamsEndpointUrl;

    @NonNull
    private final String streamName;

}
