package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.reflect.TypeToken;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.responses.NodeRelationshipsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class NodeFetcher extends WebSocketRequestHandler<NodeRelationshipsResponse> {

    @Autowired
    public NodeFetcher(WebSocketManager webSocketManager,
                       WebSocketMessageExtractor webSocketMessageExtractor) {
        super(webSocketManager, webSocketMessageExtractor);
    }

    public CompletableFuture<NodeRelationshipsResponse> fetchNodeByIdAsync(UUID nodeId)
            throws WebSocketConnectionException {
        CompletableFuture<NodeRelationshipsResponse> nodeFuture =
                sendRequestAsync(WebSocketMessageType.GET_NODE, nodeId.toString());

        return nodeFuture;
    }

    @Override
    protected Type getResponsePayloadType() {
        Type payloadType = new TypeToken<NodeRelationshipsResponse>() {
        }.getType();

        return payloadType;
    }
}
