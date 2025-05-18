package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.logic.WebSocketMessageTypeParser;
import com.rotem.graphsync.graph.processor.exceptions.InvalidWebSocketMessageTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageTypeParserWrapper {

    private final WebSocketMessageTypeParser webSocketMessageTypeParser;

    @Autowired
    public WebSocketMessageTypeParserWrapper(WebSocketMessageTypeParser webSocketMessageTypeParser) {
        this.webSocketMessageTypeParser = webSocketMessageTypeParser;
    }

    public WebSocketMessageType getWebSocketMessageType(String messageType)
            throws InvalidWebSocketMessageTypeException {
        try {
            WebSocketMessageType webSocketMessageType =
                    webSocketMessageTypeParser.getWebSocketMessageType(messageType);

            return webSocketMessageType;
        } catch (Exception e) {
            throw new InvalidWebSocketMessageTypeException(e.getMessage(), e);
        }
    }
}
