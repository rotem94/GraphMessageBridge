package com.rotem.graphsync.graph.processor.logic.message.processors;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.processor.exceptions.InvalidWebSocketMessageTypeException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageProcessorFactory {

    private final Map<WebSocketMessageType, MessageProcessor> messageTypeToMessageProcessorMap;

    public MessageProcessorFactory(
            Map<WebSocketMessageType, MessageProcessor> messageTypeToMessageProcessorMap) {
        this.messageTypeToMessageProcessorMap = messageTypeToMessageProcessorMap;
    }

    public MessageProcessor getMessageProcessor(WebSocketMessageType messageType)
            throws InvalidWebSocketMessageTypeException {
        MessageProcessor messageProcessor = messageTypeToMessageProcessorMap.get(messageType);

        if (messageProcessor == null) {
            String errorMessage = "Invalid web socket message type received from service A - " + messageType;

            throw new InvalidWebSocketMessageTypeException(errorMessage);
        } else {
            return messageProcessor;
        }
    }
}
