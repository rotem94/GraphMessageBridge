package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.exceptions.InvalidWebSocketMessageException;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public abstract class WebSocketRequestHandler<T> {

    protected final WebSocketManager webSocketManager;
    protected final WebSocketMessageExtractor webSocketMessageExtractor;

    public WebSocketRequestHandler(WebSocketManager webSocketManager,
                                   WebSocketMessageExtractor webSocketMessageExtractor) {
        this.webSocketManager = webSocketManager;
        this.webSocketMessageExtractor = webSocketMessageExtractor;
    }

    protected abstract Type getResponsePayloadType();

    protected CompletableFuture<T> sendRequestAsync(WebSocketMessageType messageType, String messageContent)
            throws WebSocketConnectionException, InvalidWebSocketMessageException {
        WebSocketMessage webSocketMessage = new WebSocketMessage(messageType.toString(), messageContent);

        CompletableFuture<T> responseFuture = webSocketManager
                .sendMessageAsync(webSocketMessage)
                .thenApply(message ->
                        webSocketMessageExtractor.convertWebSocketMessage(message, getResponsePayloadType()));

        return responseFuture;
    }
}
