package com.rotem.graphsync.graph.common.models.requests;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.UUID;

@Data
public class NodeRequest {

    private UUID id;

    @UniqueElements(message = "All neighbours ids must be unique")
    private List<UUID> neighbors;
}
