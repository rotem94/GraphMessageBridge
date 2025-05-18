package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.reflect.TypeToken;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.AckType;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.Ack;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public abstract class WebSocketUpdateRequestHandler extends WebSocketRequestHandler<Ack> {

    public WebSocketUpdateRequestHandler(WebSocketManager webSocketManager,
                                         WebSocketMessageExtractor webSocketMessageExtractor) {
        super(webSocketManager, webSocketMessageExtractor);
    }

    @Override
    protected Type getResponsePayloadType() {
        Type payloadType = new TypeToken<Ack>() {
        }.getType();

        return payloadType;
    }

    protected CompletableFuture<String> sendUpdateRequestAsync(WebSocketMessageType messageType,
                                                               String message)
            throws WebSocketConnectionException {
        CompletableFuture<Ack> ackResponseFuture = super.sendRequestAsync(messageType, message);

        CompletableFuture<String> finalResponseFuture =
                ackResponseFuture.thenApply(ack -> AckType.OK.toString());

        return finalResponseFuture;
    }
}
