package com.rotem.graphsync.graph.common.models.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteNodesRequest {

    @NotNull(message = "Node list cannot be null")
    @Valid
    @UniqueElements(message = "All nodes ids must be unique")
    private List<UUID> nodesIds;
}
