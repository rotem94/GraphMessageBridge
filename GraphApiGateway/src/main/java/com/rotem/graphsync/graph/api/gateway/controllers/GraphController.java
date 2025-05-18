package com.rotem.graphsync.graph.api.gateway.controllers;

import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.logic.managers.GraphManager;
import com.rotem.graphsync.graph.common.models.requests.DeleteNodesRequest;
import com.rotem.graphsync.graph.common.models.requests.ModifyGraphRequest;
import com.rotem.graphsync.graph.common.models.requests.SetNodesRequest;
import com.rotem.graphsync.graph.common.models.responses.GraphResponse;
import com.rotem.graphsync.graph.common.models.responses.NodeRelationshipsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/graphs")
@Tag(name = "GraphController", description = "Graph manipulator API")
public class GraphController {

    private final GraphManager graphManager;

    @Autowired
    public GraphController(GraphManager graphManager) {
        this.graphManager = graphManager;
    }

    @Operation(summary = "Get graph by name", description = "Returns graph")
    @ApiResponse(responseCode = "200", description = "Graph returned successfully")
    @ApiResponse(responseCode = "404", description = "Graph not found")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{name}")
    public CompletableFuture<GraphResponse> getGraphByNameAsync(@PathVariable String name)
            throws WebSocketConnectionException {
        System.out.println("Got request to get graph " + name);
        CompletableFuture<GraphResponse> graph = graphManager.fetchGraphAsync(name);

        return graph;
    }

    @Operation(summary = "Get graph node by id", description = "Returns node")
    @ApiResponse(responseCode = "200", description = "Node returned successfully")
    @ApiResponse(responseCode = "404", description = "Node not found")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/nodes/{nodeId}")
    public CompletableFuture<NodeRelationshipsResponse> getGraphNodeAsync(@PathVariable UUID nodeId)
            throws WebSocketConnectionException {
        System.out.println("Got request to get node " + nodeId);
        CompletableFuture<NodeRelationshipsResponse> graph = graphManager.fetchGraphNodeByNodeIDAsync(nodeId);

        return graph;
    }

    @Operation(summary = "Creates graph", description = "Creates graph")
    @ApiResponse(responseCode = "201", description = "Graph created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<String> createGraphAsync(
            @RequestBody @Valid ModifyGraphRequest createGraphRequest) throws WebSocketConnectionException {
        System.out.println("Got request to create graph " + createGraphRequest.getName());

        CompletableFuture<String> graphCreatedFuture =
                graphManager.createGraphAsync(createGraphRequest);

        return graphCreatedFuture;
    }

    @Operation(summary = "Updates graph nodes", description = "Updates graph nodes")
    @ApiResponse(responseCode = "204", description = "Graph updated")
    @ApiResponse(responseCode = "400", description = "Invalid set nodes request")
    @ApiResponse(responseCode = "404", description = "Graph not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{name}")
    public CompletableFuture<String> updateGraphAsync(@PathVariable String name,
                                                      @RequestBody @Valid SetNodesRequest setGraphRequest)
            throws WebSocketConnectionException {
        System.out.println("Got request to update graph " + name);

        CompletableFuture<String> graphUpdatedFuture =
                graphManager.updateGraphAsync(name, setGraphRequest);

        return graphUpdatedFuture;
    }

    @Operation(summary = "Delete graph", description = "Deletes the graph")
    @ApiResponse(responseCode = "204", description = "Graph deleted")
    @ApiResponse(responseCode = "400", description = "Invalid patch nodes request")
    @ApiResponse(responseCode = "404", description = "Graph / nodes not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public CompletableFuture<String> deleteGraphAsync(@PathVariable String name)
            throws WebSocketConnectionException {
        System.out.println("Got request to delete graph " + name);

        CompletableFuture<String> graphDeletedFuture = graphManager.deleteGraphAsync(name);

        return graphDeletedFuture;
    }

    @Operation(summary = "Delete graph nodes", description = "Deletes graph nodes")
    @ApiResponse(responseCode = "204", description = "Graph nodes deleted")
    @ApiResponse(responseCode = "400", description = "Invalid delete nodes request")
    @ApiResponse(responseCode = "404", description = "Graph / nodes not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}/nodes")
    public CompletableFuture<String> deleteGraphNodesAsync(@PathVariable String name,
                                                           @RequestBody
                                                           @Valid DeleteNodesRequest deleteNodesRequest)
            throws WebSocketConnectionException {
        System.out.println("Got request to delete graph nodes " + name);

        CompletableFuture<String> graphNodesDeletedFuture =
                graphManager.deleteGraphNodesAsync(name, deleteNodesRequest);

        return graphNodesDeletedFuture;
    }
}
