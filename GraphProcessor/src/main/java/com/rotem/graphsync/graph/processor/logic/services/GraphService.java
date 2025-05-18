package com.rotem.graphsync.graph.processor.logic.services;

import com.rotem.graphsync.graph.common.models.graph.Graph;
import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.processor.exceptions.GraphDatabaseException;
import com.rotem.graphsync.graph.processor.exceptions.GraphModificationException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNodeNotFoundException;
import com.rotem.graphsync.graph.processor.exceptions.GraphNotFoundException;
import com.rotem.graphsync.graph.processor.repositories.GraphRepository;
import com.rotem.graphsync.graph.processor.repositories.NodeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class GraphService {

    private final GraphRepository graphRepository;
    private final NodeEntityRepository nodeEntityRepository;

    @Autowired
    public GraphService(GraphRepository graphRepository,
                        NodeEntityRepository nodeEntityRepository) {
        this.graphRepository = graphRepository;
        this.nodeEntityRepository = nodeEntityRepository;
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Graph> fetchGraphAsync(String graphName)
            throws GraphDatabaseException, GraphNotFoundException {
        Optional<Graph> graphOptional = tryFetchingGraphAsync(graphName);

        Graph graph = graphOptional.orElseThrow(() ->
                new GraphNotFoundException("Could not find the graph: " + graphName));

        return CompletableFuture.completedFuture(graph);
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Boolean> doesGraphExistAsync(String graphName) throws GraphDatabaseException {
        Optional<Graph> graphOptional = tryFetchingGraphAsync(graphName);
        boolean doesGraphExists = graphOptional.isPresent();

        return CompletableFuture.completedFuture(doesGraphExists);
    }

    @Async
    @Transactional
    public CompletableFuture<Graph> createGraphAsync(Graph graph) throws GraphModificationException {
        Graph graphCreated = saveGraph(graph);

        return CompletableFuture.completedFuture(graphCreated);
    }

    @Async
    @Transactional
    public CompletableFuture<Graph> updateGraphAsync(Graph graphToUpdate, List<NodeEntity> newGraphNodes)
            throws GraphModificationException {
        deleteGraphNodes(graphToUpdate.getNodes());
        graphToUpdate.setNodes(newGraphNodes);

        Graph updatedGraph = saveGraph(graphToUpdate);

        return CompletableFuture.completedFuture(updatedGraph);
    }

    @Async
    @Transactional
    public CompletableFuture<Graph> deleteGraphAsync(Graph graph) throws GraphModificationException {
        deleteGraphNodes(graph.getNodes());
        tryDeletingGraph(graph);

        return CompletableFuture.completedFuture(graph);
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<NodeEntity> fetchGraphNodeAsync(UUID nodeId)
            throws GraphDatabaseException, GraphNodeNotFoundException {
        Optional<NodeEntity> nodeOptional;

        try {
            nodeOptional = nodeEntityRepository.findById(nodeId);
        } catch (Exception e) {
            String errorMessage = "The following exception caught while fetching data from the " +
                    "database: " + e.getMessage();

            throw new GraphDatabaseException(errorMessage, e);
        }

        NodeEntity node = nodeOptional.orElseThrow(() ->
                new GraphNodeNotFoundException("Could not find graph node with id: " + nodeId));

        return CompletableFuture.completedFuture(node);
    }

    @Async
    @Transactional
    public CompletableFuture<List<UUID>> deleteGraphNodesByIds(List<UUID> nodesToDelete)
            throws GraphModificationException {
        try {
            nodeEntityRepository.deleteAllById(nodesToDelete);

            return CompletableFuture.completedFuture(nodesToDelete);
        } catch (Exception e) {
            String errorMessage = "The following exception caught while trying to delete graph nodes from " +
                    "the database: " + e.getMessage();

            throw new GraphModificationException(errorMessage, e);
        }
    }

    private Optional<Graph> tryFetchingGraphAsync(String graphName) throws GraphDatabaseException {
        try {
            Optional<Graph> graphOptional = graphRepository.findByName(graphName);

            return graphOptional;
        } catch (Exception e) {
            String errorMessage = "The following exception caught while fetching data from the " +
                    "database: " + e.getMessage();

            throw new GraphDatabaseException(errorMessage, e);
        }
    }

    private Graph saveGraph(Graph graph) throws GraphModificationException {
        try {
            graphRepository.save(graph);

            return graph;
        } catch (Exception e) {
            String errorMessage = "An exception occurred while attempting to save the graph " +
                    "'" + graph.getName() + "' in the database. This may be due to network connectivity " +
                    "issues or because nodes with the same IDs already exist in other graphs";

            System.out.println(errorMessage);

            throw new GraphModificationException(errorMessage);
        }
    }

    private void deleteGraphNodes(List<NodeEntity> nodes) throws GraphModificationException {
        try {
            nodeEntityRepository.deleteAll(nodes);
        } catch (Exception e) {
            String errorMessage = "The following exception caught while trying to delete graph nodes from " +
                    "the database: " + e.getMessage();

            throw new GraphModificationException(errorMessage, e);
        }
    }

    private void tryDeletingGraph(Graph graph) throws GraphModificationException {
        try {
            graphRepository.delete(graph);
        } catch (Exception e) {
            String errorMessage = "The following exception caught while trying to delete " +
                    "graph '" + graph.getName() + "'from the database: " + e.getMessage();

            throw new GraphModificationException(errorMessage, e);
        }
    }
}
