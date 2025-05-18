package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.requests.DeleteGraphNodesRequest;
import com.rotem.graphsync.graph.common.models.requests.DeleteNodesRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NodesDeleter extends WebSocketUpdateRequestHandler {

    private final Gson gson;

    public NodesDeleter(WebSocketManager webSocketManager,
                        WebSocketMessageExtractor webSocketMessageExtractor,
                        Gson gson) {
        super(webSocketManager, webSocketMessageExtractor);

        this.gson = gson;
    }

    public CompletableFuture<String> deleteGraphNodesAsync(String graphName,
                                                           DeleteNodesRequest deleteNodesRequest)
            throws WebSocketConnectionException {
        DeleteGraphNodesRequest deleteGraphNodesRequest =
                new DeleteGraphNodesRequest(graphName, deleteNodesRequest);
        String messageContent = gson.toJson(deleteGraphNodesRequest);

        CompletableFuture<String> nodesDeletionScheduledFuture =
                sendUpdateRequestAsync(WebSocketMessageType.DELETE_GRAPH_NODES, messageContent);

        return nodesDeletionScheduledFuture;
    }
}
