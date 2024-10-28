package com.emailapigateway.service;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/auth", "/swagger-ui.html", "/v3/api-docs"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest ->
            openEndpoints.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

}
