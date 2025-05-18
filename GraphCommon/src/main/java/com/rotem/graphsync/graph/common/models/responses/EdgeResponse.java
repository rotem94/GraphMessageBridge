package com.rotem.graphsync.graph.common.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EdgeResponse {
    private UUID sourceId;
    private UUID targetId;
}
