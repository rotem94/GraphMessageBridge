package com.rotem.graphsync.graph.api.gateway.logic.request.handlers;

import com.google.gson.reflect.TypeToken;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.interfaces.WebSocketManager;
import com.rotem.graphsync.graph.api.gateway.extractors.WebSocketMessageExtractor;
import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.responses.GraphResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

@Service
public class GraphFetcher extends WebSocketRequestHandler<GraphResponse> {

    @Autowired
    public GraphFetcher(WebSocketManager webSocketManager,
                        WebSocketMessageExtractor webSocketMessageExtractor) {
        super(webSocketManager, webSocketMessageExtractor);
    }

    public CompletableFuture<GraphResponse> fetchGraphAsync(String name)
            throws WebSocketConnectionException {
        CompletableFuture<GraphResponse> graphsFuture =
                sendRequestAsync(WebSocketMessageType.GET_GRAPH, name);

        return graphsFuture;
    }

    @Override
    protected Type getResponsePayloadType() {
        Type payloadType = new TypeToken<GraphResponse>() {
        }.getType();

        return payloadType;
    }
}
