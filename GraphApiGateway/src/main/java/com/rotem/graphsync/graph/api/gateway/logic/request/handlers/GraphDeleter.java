package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.AckType;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.Ack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GraphDeleter extends WebSocketUpdateRequestHandler {

    @Autowired
    public GraphDeleter(WebSocketManager webSocketManager,
                        WebSocketMessageExtractor webSocketMessageExtractor) {
        super(webSocketManager, webSocketMessageExtractor);
    }

    public CompletableFuture<String> deleteGraphAsync(String graphName) throws WebSocketConnectionException {
        CompletableFuture<Ack> ackResponseFuture =
                sendRequestAsync(WebSocketMessageType.DELETE_GRAPH, graphName);

        CompletableFuture<String> finalResponseFuture =
                ackResponseFuture.thenApply(ack -> AckType.OK.toString());

        return finalResponseFuture;
    }
}
