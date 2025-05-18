package com.rotem.graphsync.graph.api.gateway.controllers.exceptions.handler;

import com.rotem.graphsync.graph.api.gateway.exceptions.DataNotFoundException;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketBadRequestException;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketConnectionException;
import com.rotem.graphsync.graph.api.gateway.exceptions.WebSocketSendMessageException;
import com.rotem.graphsync.graph.common.exceptions.InvalidWebSocketMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public CustomErrorResponse handleDataNotFoundException(DataNotFoundException e) {
        return new CustomErrorResponse(e.getMessage());
    }

    @ExceptionHandler(WebSocketBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorResponse handleWebSocketBadRequestException(WebSocketBadRequestException e) {
        return new CustomErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            WebSocketConnectionException.class,
            WebSocketSendMessageException.class,
            InvalidWebSocketMessageException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleGeneralException(Exception e) {
        return new CustomErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleUnknownException(Exception e) {
        return new CustomErrorResponse("Got unknown error: " + e.getMessage());
    }
}
