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

package io.apimap.orchestra.orchestra.error.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-4)
class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof org.springframework.security.oauth2.jwt.BadJwtException) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }

        if (ex instanceof org.springframework.data.mongodb.UncategorizedMongoDbException) {
            return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Database not available"));
        }

        if (ex instanceof ResponseStatusException) {
            return Mono.error(new ResponseStatusException(((ResponseStatusException) ex).getStatusCode(), ((ResponseStatusException) ex).getReason() != null ? ((ResponseStatusException) ex).getReason() : "Unknown error"));
        }

        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown error"));
    }
}
