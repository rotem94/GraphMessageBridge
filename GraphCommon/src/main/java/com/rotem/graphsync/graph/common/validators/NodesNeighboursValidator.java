package com.rotem.graphsync.graph.common.validators;

import com.rotem.graphsync.graph.common.Annotations.ValidNodesNeighbours;
import com.rotem.graphsync.graph.common.models.requests.NodeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NodesNeighboursValidator
        implements ConstraintValidator<ValidNodesNeighbours, List<NodeRequest>> {

    @Override
    public void initialize(ValidNodesNeighbours constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<NodeRequest> nodeRequests, ConstraintValidatorContext context) {
        Set<UUID> nodesIds = nodeRequests.stream()
                .map(NodeRequest::getId)
                .collect(Collectors.toSet());

        if (nodesIds.size() != nodeRequests.size()) {
            return false;
        }

        Set<UUID> allNeighbours = nodeRequests.stream()
                .map(NodeRequest::getNeighbors)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        boolean doAllNeighboursExist = nodesIds.containsAll(allNeighbours);

        System.out.println("here5");

        return doAllNeighboursExist;
    }
}
