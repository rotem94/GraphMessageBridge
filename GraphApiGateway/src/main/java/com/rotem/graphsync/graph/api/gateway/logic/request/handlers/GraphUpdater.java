package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.requests.ModifyGraphRequest;
import com.rotem.graphsync.graph.common.models.requests.SetNodesRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GraphUpdater extends WebSocketUpdateRequestHandler {

    private final Gson gson;

    public GraphUpdater(WebSocketManager webSocketManager,
                        WebSocketMessageExtractor webSocketMessageExtractor,
                        Gson gson) {
        super(webSocketManager, webSocketMessageExtractor);

        this.gson = gson;
    }

    public CompletableFuture<String> updateGraphAsync(String name, SetNodesRequest setNodesRequest)
            throws WebSocketConnectionException {
        ModifyGraphRequest updateGraphRequest = new ModifyGraphRequest(name, setNodesRequest);
        String messageContent = gson.toJson(updateGraphRequest);

        CompletableFuture<String> graphsScheduledForAnUpdateFuture =
                sendUpdateRequestAsync(WebSocketMessageType.UPDATE_GRAPH, messageContent);

        return graphsScheduledForAnUpdateFuture;
    }
}
