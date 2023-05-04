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

import io.apimap.orchestra.orchestra.service.GenericResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class GenericRouter {
    public static final String ROOT_PATH = "/";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = ROOT_PATH,
            method = RequestMethod.GET,
            beanClass = GenericResourceService.class,
            beanMethod = "rootResource",
            operation = @Operation(
                operationId = "API starting point",
                summary = "HATEOAS starting point.",
                description = "This endpoint is mostly to enable HATEOAS navigation.",
                tags = {"START"},
                security = {@SecurityRequirement(name = "JWT")}
            ))
    })

    /*
     /
     */

    RouterFunction<ServerResponse> genericRoutes(GenericResourceService service) {
        return RouterFunctions
            .route(GET(ROOT_PATH).and(accept(APPLICATION_JSON)), service::genericResource)
            .andRoute(GET(ROOT_PATH), service::blank);
    }
}
