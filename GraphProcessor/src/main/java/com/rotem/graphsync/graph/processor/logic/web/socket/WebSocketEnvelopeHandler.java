package com.rotem.graphsync.graph.processor.logic.web.socket;

import com.rotem.graphsync.graph.common.enums.WebSocketMessageType;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketEnvelope;
import com.rotem.graphsync.graph.common.models.websocket.WebSocketMessage;
import com.rotem.graphsync.graph.processor.exceptions.*;
import com.rotem.graphsync.graph.processor.models.WebSocketClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class WebSocketEnvelopeHandler extends TextWebSocketHandler {

    private final WebSocketEnvelopeParser webSocketEnvelopeParser;
    private final WebSocketMessageHandler webSocketMessageHandler;
    private final TextMessageBuilder textMessageBuilder;

    @Autowired
    public WebSocketEnvelopeHandler(WebSocketEnvelopeParser webSocketEnvelopeParser,
                                    WebSocketMessageHandler webSocketMessageHandler,
                                    TextMessageBuilder textMessageBuilder) {
        this.webSocketEnvelopeParser = webSocketEnvelopeParser;
        this.webSocketMessageHandler = webSocketMessageHandler;
        this.textMessageBuilder = textMessageBuilder;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Got the following message: " + message.getPayload());

        Optional<WebSocketEnvelope> envelopeOptional = parseMessageToWebSocketEnvelopeOptional(message);

        if (envelopeOptional.isPresent()) {
            WebSocketEnvelope envelope = envelopeOptional.get();
            String correlationId = envelope.getCorrelationId();

            WebSocketClientSession webSocketClientSession =
                    new WebSocketClientSession(session, correlationId);

            handleWebSocketEnvelopeAsync(envelope, webSocketClientSession);
        }
    }

    private Optional<WebSocketEnvelope> parseMessageToWebSocketEnvelopeOptional(TextMessage message) {
        try {
            WebSocketEnvelope envelope = webSocketEnvelopeParser.parseToWebSocketEnvelope(message);

            return Optional.of(envelope);
        } catch (InvalidWebSocketEnvelopeException e) {
            String errorMessage = "Got an invalid message. " + e.getMessage() + ". Didn't send message " +
                    "back to the client";
            System.out.println(errorMessage);

            return Optional.empty();
        }
    }

    private void handleWebSocketEnvelopeAsync(WebSocketEnvelope envelope,
                                              WebSocketClientSession clientSession) {
        try {
            tryHandlingWebSocketEnvelopeAsync(envelope, clientSession.getSession());
        } catch (InvalidWebSocketMessageTypeException
                 |
                 InvalidWebSocketMessagePayloadException
                 |
                 InvalidWebSocketEnvelopeException e) {
            handleExceptionAsync(e, clientSession, WebSocketMessageType.BAD_REQUEST);
        } catch (Exception e) {
            handleExceptionAsync(e, clientSession, WebSocketMessageType.INVALID);
        }
    }

    private void tryHandlingWebSocketEnvelopeAsync(WebSocketEnvelope request, WebSocketSession session)
            throws Exception {
        String correlationId = request.getCorrelationId();
        WebSocketMessage requestMessage = request.getWebSocketMessage();

        if (requestMessage == null) {
            throw new InvalidWebSocketEnvelopeException("Envelope must have a 'WebSocketMessage'");
        }

        WebSocketClientSession webSocketClientSession = new WebSocketClientSession(session, correlationId);

        webSocketMessageHandler
                .handleWebSocketMessageAsync(requestMessage)
                .thenApply(responseMessage ->
                        textMessageBuilder.buildTextMessage(responseMessage, correlationId))
                .thenAcceptAsync(response -> sendResponse(session, response))
                .exceptionally(e -> handleFutureExceptionAsync(e, webSocketClientSession));
    }

    private Void handleFutureExceptionAsync(Throwable e, WebSocketClientSession clientSession) {
        Throwable cause = e.getCause();

        WebSocketMessageType messageType;

        if (cause instanceof GraphAlreadyExistsException) {
            messageType = WebSocketMessageType.BAD_REQUEST;
        } else if (cause instanceof GraphNotFoundException || cause instanceof GraphNodeNotFoundException) {
            messageType = WebSocketMessageType.NOT_FOUND;
        } else {
            messageType = WebSocketMessageType.INVALID;
        }

        handleExceptionAsync(cause, clientSession, messageType);

        return null;
    }

    private void handleExceptionAsync(Throwable e,
                                      WebSocketClientSession clientSession,
                                      WebSocketMessageType webSocketMessageType) {
        String correlationId = clientSession.getCorrelationId();
        WebSocketSession session = clientSession.getSession();

        String errorMessage = "Request was not handled. " + e.getMessage();
        System.out.println(errorMessage);

        TextMessage responseMessage =
                textMessageBuilder.buildErrorTextMessage(webSocketMessageType, errorMessage, correlationId);

        sendResponseAsync(session, responseMessage);
    }

    private void sendResponseAsync(WebSocketSession session, TextMessage textMessage) {
        CompletableFuture.runAsync(() -> sendResponse(session, textMessage));
    }

    private void sendResponse(WebSocketSession session, TextMessage textMessage) {
        try {
            System.out.println("Sending the following response: " + textMessage);

            session.sendMessage(textMessage);
        } catch (Exception e) {
            System.err.println("Failed to send any response. Got the following exception: " + e.getMessage());
        }
    }
}