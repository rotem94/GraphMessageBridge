package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.logic.message.processors.MessageProcessor;
import com.rotem.graphsync.graph.processor.logic.message.processors.MessageProcessorFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WebSocketMessageHandler {

    private final MessageProcessorFactory messageProcessorFactory;
    private final WebSocketMessageTypeParserWrapper webSocketMessageTypeParserWrapper;

    public WebSocketMessageHandler(MessageProcessorFactory messageProcessorFactory,
                                   WebSocketMessageTypeParserWrapper webSocketMessageTypeParserWrapper) {
        this.messageProcessorFactory = messageProcessorFactory;
        this.webSocketMessageTypeParserWrapper = webSocketMessageTypeParserWrapper;
    }

    public CompletableFuture<WebSocketMessage> handleWebSocketMessageAsync(WebSocketMessage message)
            throws Exception {
        String messageType = message.getType();
        String messagePayload = message.getPayload();

        WebSocketMessageType webSocketMessageType =
                webSocketMessageTypeParserWrapper.getWebSocketMessageType(messageType);

        MessageProcessor messageProcessor = messageProcessorFactory.getMessageProcessor(webSocketMessageType);

        CompletableFuture<WebSocketMessage> responseMessageFuture =
                messageProcessor.processMassageAsync(messagePayload);

        return responseMessageFuture;
    }
}
