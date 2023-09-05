package com.academy.springwebfluxessentials.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        var errorAttributesMap = super.getErrorAttributes(request, options);
        var throwable = getError(request);
        if (throwable instanceof ResponseStatusException) {
            var ex = (ResponseStatusException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            errorAttributesMap.put("developerMessage", "Respect my history, a response status exception happened");
        }
        return errorAttributesMap;
    }
}
