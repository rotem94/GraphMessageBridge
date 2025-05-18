package com.rotem.graphsync.graph.api.gateway.interfaces;

import com.google.gson.Gson;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketSendMessageException;
import com.rotem.graphsync.graph.api.gateway.utils.RandomIdGenerator;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketEnvelope;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.*;

public class WebSocketInterface extends WebSocketClient {

    private final ConcurrentMap<String, CompletableFuture<WebSocketMessage>> correlationMap;
    private final Gson gson;
    private final long cleanupRequestsInterval;

    public WebSocketInterface(URI serviceBUri, Gson gson, long cleanupRequestsInterval) {
        super(serviceBUri);

        correlationMap = new ConcurrentHashMap<>();

        this.gson = gson;
        this.cleanupRequestsInterval = cleanupRequestsInterval;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to service B successfully");

        ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

        cleanupScheduler.scheduleAtFixedRate(
                this::cleanCompletedRequests,
                0,
                cleanupRequestsInterval,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void onMessage(String message) {
        try {
            System.out.println("Got message: " + message);
            tryReadingMessage(message);
        } catch (Exception e) {
            System.out.println("Unknown message received. Got the following error: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection to service B refused");
    }

    @Override
    public void onError(Exception e) {
        System.err.println("Connection to service B failed. " + e.getMessage());
    }

    public CompletableFuture<WebSocketMessage> sendMessageAsync(WebSocketMessage message) {
        String correlationId = RandomIdGenerator.GenerateRandomId();
        WebSocketEnvelope webSocketRequest = new WebSocketEnvelope(message, correlationId);

        String requestAsString = gson.toJson(webSocketRequest);

        CompletableFuture<WebSocketMessage> responseFuture = new CompletableFuture<>();
        responseFuture.orTimeout(5, TimeUnit.SECONDS);

        correlationMap.put(correlationId, responseFuture);

        sendRequestAsync(requestAsString, correlationId);

        return responseFuture;
    }

    private void cleanCompletedRequests() {
        correlationMap.entrySet().removeIf(entry -> entry.getValue().isDone());
    }

    private void sendRequestAsync(String requestAsString, String correlationId) {
        CompletableFuture.runAsync(() -> sendRequest(requestAsString, correlationId));
    }

    private void sendRequest(String requestAsString, String correlationId) {
        try {
            send(requestAsString);

            System.out.println("Sent the following request: " + requestAsString);
        } catch (Exception e) {
            CompletableFuture<WebSocketMessage> responseToFinishExceptionally =
                    correlationMap.remove(correlationId);

            responseToFinishExceptionally.completeExceptionally(
                    new WebSocketSendMessageException("Error occurred while sending message", e));
        }
    }

    private void tryReadingMessage(String message) {
        WebSocketEnvelope webSocketRequest = gson.fromJson(message, WebSocketEnvelope.class);

        WebSocketMessage webSocketMessage = webSocketRequest.getWebSocketMessage();
        String correlationId = webSocketRequest.getCorrelationId();

        if (correlationMap.containsKey(correlationId)) {
            CompletableFuture<WebSocketMessage> responseFuture = correlationMap.remove(correlationId);

            responseFuture.complete(webSocketMessage);
        } else {
            System.out.println("Message with unknown correlation id was received");
        }
    }
}
