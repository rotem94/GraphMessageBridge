package com.rotem.graphsync.graph.api.gateway.logic.managers;

import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.logic.request.handlers.*;
import com.rotem.graphsync.graph.common.models.requests.DeleteNodesRequest;
import com.rotem.graphsync.graph.common.models.requests.ModifyGraphRequest;
import com.rotem.graphsync.graph.common.models.requests.SetNodesRequest;
import com.rotem.graphsync.graph.common.models.responses.GraphResponse;
import com.rotem.graphsync.graph.common.models.responses.NodeRelationshipsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class GraphManager {

    private final GraphFetcher graphFetcher;
    private final GraphCreator graphCreator;
    private final GraphUpdater graphUpdater;
    private final GraphDeleter graphDeleter;
    private final NodeFetcher nodeFetcher;
    private final NodesDeleter nodesDeleter;

    @Autowired
    public GraphManager(GraphFetcher graphFetcher,
                        GraphCreator graphCreator,
                        GraphUpdater graphUpdater,
                        GraphDeleter graphDeleter,
                        NodeFetcher nodeFetcher,
                        NodesDeleter nodesDeleter) {
        this.graphFetcher = graphFetcher;
        this.graphCreator = graphCreator;
        this.graphUpdater = graphUpdater;
        this.graphDeleter = graphDeleter;
        this.nodeFetcher = nodeFetcher;
        this.nodesDeleter = nodesDeleter;
    }

    public CompletableFuture<GraphResponse> fetchGraphAsync(String name)
            throws WebSocketConnectionException {
        CompletableFuture<GraphResponse> graphFuture = graphFetcher.fetchGraphAsync(name);

        return graphFuture;
    }

    public CompletableFuture<NodeRelationshipsResponse> fetchGraphNodeByNodeIDAsync(UUID nodeId)
            throws WebSocketConnectionException {
        CompletableFuture<NodeRelationshipsResponse> nodeFuture = nodeFetcher.fetchNodeByIdAsync(nodeId);

        return nodeFuture;
    }

    public CompletableFuture<String> createGraphAsync(ModifyGraphRequest createGraphRequest)
            throws WebSocketConnectionException {
        CompletableFuture<String> graphScheduledForCreationFuture =
                graphCreator.createGraphRequestAsync(createGraphRequest);

        return graphScheduledForCreationFuture;
    }

    public CompletableFuture<String> updateGraphAsync(String name, SetNodesRequest setNodesRequest)
            throws WebSocketConnectionException {
        CompletableFuture<String> graphScheduledForAnUpdateFuture =
                graphUpdater.updateGraphAsync(name, setNodesRequest);

        return graphScheduledForAnUpdateFuture;
    }

    public CompletableFuture<String> deleteGraphAsync(String name) throws WebSocketConnectionException {
        CompletableFuture<String> graphScheduledForDeletionFuture = graphDeleter.deleteGraphAsync(name);

        return graphScheduledForDeletionFuture;
    }

    public CompletableFuture<String> deleteGraphNodesAsync(String name,
                                                           DeleteNodesRequest deleteNodesRequest)
            throws WebSocketConnectionException {
        CompletableFuture<String> graphNodesScheduledForDeletionFuture =
                nodesDeleter.deleteGraphNodesAsync(name, deleteNodesRequest);

        return graphNodesScheduledForDeletionFuture;
    }
}

