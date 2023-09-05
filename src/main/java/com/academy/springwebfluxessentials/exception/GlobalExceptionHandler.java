package com.academy.springwebfluxessentials.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext, ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request) {
        var query = request.uri().getQuery();
        //var options = isTraceEnabled(query) ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STACK_TRACE) : ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
        var options = isTraceEnabled(query) ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE) : ErrorAttributeOptions.defaults();

        var errorAttributesMap = getErrorAttributes(request, options);

        var status = (int) Optional.ofNullable((errorAttributesMap.get("status"))).orElse(500);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }

    private boolean isTraceEnabled(String query) {
        return !StringUtils.isEmpty(query) && query.contains("trace=true");
    }
}
