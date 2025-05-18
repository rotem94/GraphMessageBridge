package com.rotem.graphsync.graph.processor.logic.converters;

import com.rotem.graphsync.graph.common.models.graph.NodeEntity;
import com.rotem.graphsync.graph.common.models.requests.NodeRequest;
import com.rotem.graphsync.graph.common.models.responses.EdgeResponse;
import com.rotem.graphsync.graph.common.models.responses.NodeRelationshipsResponse;
import com.rotem.graphsync.graph.common.models.responses.NodeResponse;
import com.rotem.graphsync.graph.processor.utils.StackHashMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NodeConverter {

    public List<NodeResponse> convertNodeEntitiesToNodesResponses(List<NodeEntity> nodesEntities) {
        List<NodeResponse> nodesResponses = nodesEntities.stream()
                .map(this::convertNodeEntityToNodeResponse)
                .toList();

        return nodesResponses;
    }

    public NodeRelationshipsResponse convertNodeEntityToNodeRelationshipsResponse(NodeEntity nodeEntity) {
        StackHashMap<UUID, NodeEntity> stackHashMap = new StackHashMap<>();

        List<UUID> descendants = new ArrayList<>();
        List<EdgeResponse> edges = new ArrayList<>();

        stackHashMap.push(nodeEntity.getId(), nodeEntity);

        while (!stackHashMap.isEmpty()) {
            NodeEntity currentVisitedEntity = stackHashMap.pop();

            if (currentVisitedEntity != nodeEntity) {
                descendants.add(currentVisitedEntity.getId());
            }

            for (NodeEntity neighbour : currentVisitedEntity.getNeighbors()) {
                if (!stackHashMap.containsKey(neighbour.getId())) {
                    EdgeResponse edgeResponse =
                            new EdgeResponse(currentVisitedEntity.getId(), neighbour.getId());

                    edges.add(edgeResponse);
                    stackHashMap.push(neighbour.getId(), neighbour);
                }
            }
        }

        NodeRelationshipsResponse nodeRelationshipsResponse = new NodeRelationshipsResponse();

        nodeRelationshipsResponse.setId(nodeEntity.getId());
        nodeRelationshipsResponse.setDescendants(descendants);
        nodeRelationshipsResponse.setEdges(edges);

        return nodeRelationshipsResponse;
    }

    public List<NodeEntity> convertNodesRequestsToNodeEntities(List<NodeRequest> nodeRequests) {
        List<NodeEntity> nodeEntitiesWithoutNeighbours =
                convertNodeRequestsToNodeEntitiesWithoutNeighbours(nodeRequests);

        List<NodeEntity> fullNodeEntities =
                addNodeEntitiesNeighbours(nodeEntitiesWithoutNeighbours, nodeRequests);

        return fullNodeEntities;
    }

    private List<NodeEntity> convertNodeRequestsToNodeEntitiesWithoutNeighbours(
            List<NodeRequest> nodeRequests) {
        List<NodeEntity> nodeEntitiesWithoutNeighbours = nodeRequests.stream()
                .map(nodeRequest -> new NodeEntity(nodeRequest.getId()))
                .toList();

        return nodeEntitiesWithoutNeighbours;
    }

    private List<NodeEntity> addNodeEntitiesNeighbours(List<NodeEntity> nodeEntitiesWithoutNeighbours,
                                                       List<NodeRequest> nodeRequests) {
        Map<UUID, NodeEntity> nodeIdToNodeEntityMap = nodeEntitiesWithoutNeighbours.stream()
                .collect(Collectors.toMap(NodeEntity::getId, Function.identity()));

        for (NodeRequest nodeRequest : nodeRequests) {
            UUID nodeId = nodeRequest.getId();
            NodeEntity nodeEntity = nodeIdToNodeEntityMap.get(nodeId);

            List<NodeEntity> nodeNeighbours = nodeRequest.getNeighbors()
                    .stream()
                    .map(nodeIdToNodeEntityMap::get)
                    .toList();

            nodeEntity.setNeighbors(nodeNeighbours);
        }

        List<NodeEntity> fullNodeEntities = new ArrayList<>(nodeIdToNodeEntityMap.values());

        return fullNodeEntities;
    }

    private NodeResponse convertNodeEntityToNodeResponse(NodeEntity nodeEntity) {
        List<UUID> nodeNeighboursIds = nodeEntity.getNeighbors()
                .stream()
                .map(NodeEntity::getId)
                .toList();

        NodeResponse nodeResponse = new NodeResponse();

        nodeResponse.setId(nodeEntity.getId());
        nodeResponse.setNeighborsIds(nodeNeighboursIds);

        return nodeResponse;
    }
}

