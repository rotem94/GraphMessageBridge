package com.rotem.graphsync.graph.common.models.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NodeResponse {

    private UUID id;
    private List<UUID> neighborsIds;
}
