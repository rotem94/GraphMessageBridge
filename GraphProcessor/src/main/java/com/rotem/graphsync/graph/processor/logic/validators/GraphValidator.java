package com.rotem.graphsync.graph.processor.logic.validators;

import com.rotem.graphsync.graph.common.models.graph.Graph;
import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.processor.exceptions.GraphNodeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GraphValidator {

    public void validateNodesWithGivenIdsExistInGraph(Graph graph, List<UUID> nodesIds) {
        Set<UUID> graphNodesIds = graph.getNodes().stream()
                .map(NodeEntity::getId)
                .collect(Collectors.toSet());

        if (!graphNodesIds.containsAll(nodesIds)) {
            String errorMessage = "Could not find all provided nodes in graph " + graph.getName();

            throw new GraphNodeNotFoundException(errorMessage);
        }
    }
}
