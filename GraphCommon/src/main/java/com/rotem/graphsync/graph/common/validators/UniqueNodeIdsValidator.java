package com.rotem.graphsync.graph.common.validators;

import com.rotem.graphsync.graph.common.Annotations.UniqueNodeIds;
import com.rotem.graphsync.graph.common.models.requests.NodeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UniqueNodeIdsValidator
        implements ConstraintValidator<UniqueNodeIds, List<NodeRequest>> {

    @Override
    public void initialize(UniqueNodeIds constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<NodeRequest> nodeRequests, ConstraintValidatorContext context) {
        Set<UUID> nodesIds = nodeRequests.stream()
                .map(NodeRequest::getId)
                .collect(Collectors.toSet());

        boolean areAllNodesUnique = nodesIds.size() == nodeRequests.size();

        return areAllNodesUnique;
    }
}
