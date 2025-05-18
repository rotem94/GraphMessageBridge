package com.rotem.graphsync.graph.processor.configuration;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.processor.logic.message.processors.MessageProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MessageProcessorConfig {

    @Bean
    public Map<WebSocketMessageType, MessageProcessor> messageProcessorMap(
            List<MessageProcessor> messageProcessors) {
        Map<WebSocketMessageType, MessageProcessor> messageTypeToMessageProcessorMap =
                messageProcessors
                        .stream()
                        .collect(Collectors.toMap(
                                MessageProcessor::getWebSocketMessageType,
                                Function.identity()));

        return messageTypeToMessageProcessorMap;
    }
}

