/*
Copyright 2021-2023 TELENOR NORGE AS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package io.apimap.orchestra.orchestra.router.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class XRequestIdFilter implements WebFilter {
    public static final String REQUEST_ID_HEADER_NAME = "X-Request-Id";
    public static final String REQUEST_ID_REGEXP = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    public static String requestId(ServerHttpRequest request){

        List<String> headers = request.getHeaders().get(REQUEST_ID_HEADER_NAME);

        String requestId = headers != null
            ? headers.get(0)
            : "<unknown>";

        Pattern UUID_REGEX = Pattern.compile(REQUEST_ID_REGEXP);

        if(UUID_REGEX.matcher(requestId).matches()){
            return requestId;
        }

        return "<unknown>";
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = requestId(exchange.getRequest());
        exchange.getResponse().getHeaders().set(REQUEST_ID_HEADER_NAME, requestId);

        return chain.filter(exchange);
    }
}