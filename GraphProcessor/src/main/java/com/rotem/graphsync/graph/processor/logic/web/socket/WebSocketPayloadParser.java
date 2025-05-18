package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.processor.exceptions.InvalidWebSocketMessagePayloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPayloadParser {

    private final Gson gson;

    @Autowired
    public WebSocketPayloadParser(Gson gson) {
        this.gson = gson;
    }

    public <T> T parseMessage(String message, Class<T> clazz) throws InvalidWebSocketMessagePayloadException {
        try {
            T object = gson.fromJson(message, clazz);

            if (object == null) {
                String errorMessage = "Could not parse message: " + message + " to '"
                        + clazz.getSimpleName() + "'";

                throw new InvalidWebSocketMessagePayloadException(errorMessage);
            }

            return object;
        } catch (RuntimeException e) {
            String errorMessage = "Could not parse message: " + message + " to '"
                    + clazz.getSimpleName() + "'";

            throw new InvalidWebSocketMessagePayloadException(errorMessage);
        }
    }
}
