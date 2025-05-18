package com.rotem.graphsync.graph.common.models.requests;

import com.rotem.graphsync.graph.common.Annotations.UniqueNodeIds;
import com.rotem.graphsync.graph.common.Annotations.ValidNodesNeighbours;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SetNodesRequest {

    @NotNull(message = "Node list cannot be null")
    @Valid
    @UniqueNodeIds
    @ValidNodesNeighbours
    private List<NodeRequest> nodes;
}
