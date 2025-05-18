package com.rotem.graphsync.graph.api.gateway.controllers.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomErrorResponse {
    private String message;
}
