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

package io.apimap.orchestra.orchestra.router;

import io.apimap.orchestra.orchestra.service.WellKnownService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class WellKnownRouter {

    public static final String WELL_KNOWN_PATH = "/.well-known";
    public static final String JSKW_JSON = WELL_KNOWN_PATH + "/jwks.json";
    public static final String OPENID_CONFIGURATION_PATH = WELL_KNOWN_PATH + "/openid-configuration";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = JSKW_JSON,
            method = RequestMethod.GET,
            beanClass = WellKnownService.class,
            beanMethod = "jwksJson",
            operation = @Operation(
                operationId = "JSON Web Key Sets",
                summary = "JSON Web Key Sets",
                description = "JSON Web Key Sets",
                tags = {"WELLKNOWN"}
            )
        ),
        @RouterOperation(
            path = OPENID_CONFIGURATION_PATH,
            method = RequestMethod.GET,
            beanClass = WellKnownService.class,
            beanMethod = "openidConfiguration",
            operation = @Operation(
                operationId = "OpenID Connect Discovery",
                summary = "OpenID Connect Discovery",
                description = "OpenID Connect Discovery",
                tags = {"WELLKNOWN"}
            )
        )
    })

    /*
        /.well-known
        /.well-known/jwks.json
        /.well-known/openid-configuration
    */

    RouterFunction<ServerResponse> wellKnownRoutes(WellKnownService wellKnownService) {
        return RouterFunctions
            .route(GET(JSKW_JSON).and(accept(APPLICATION_JSON)), wellKnownService::jwksJson)
            .andRoute(GET(OPENID_CONFIGURATION_PATH).and(accept(APPLICATION_JSON)), wellKnownService::openidConfiguration);
    }
}
