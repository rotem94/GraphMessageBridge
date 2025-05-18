package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.requests.ModifyGraphRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GraphCreator extends WebSocketUpdateRequestHandler {

    private final Gson gson;

    public GraphCreator(WebSocketManager webSocketManager,
                        WebSocketMessageExtractor webSocketMessageExtractor,
                        Gson gson) {
        super(webSocketManager, webSocketMessageExtractor);

        this.gson = gson;
    }

    public CompletableFuture<String> createGraphRequestAsync(ModifyGraphRequest createGraphRequest)
            throws WebSocketConnectionException {
        String messageContent = gson.toJson(createGraphRequest);

        CompletableFuture<String> graphsCreationScheduledFuture =
                sendUpdateRequestAsync(WebSocketMessageType.CREATE_GRAPH, messageContent);

        return graphsCreationScheduledFuture;
    }
}
