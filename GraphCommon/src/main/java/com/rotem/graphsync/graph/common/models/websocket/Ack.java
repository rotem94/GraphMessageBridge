package com.rotem.graphsync.graph.common.models.websocket;

import com.rotem.graphsync.graph.common.enums.AckType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ack {
    private AckType type;
}
