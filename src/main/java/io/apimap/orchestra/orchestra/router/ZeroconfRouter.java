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

import io.apimap.orchestra.orchestra.service.ZeroconfService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ZeroconfRouter {

    public static final String ROOT_PATH = "/.zeroconf";
    public static final String ZEROCONF_CONFIGURATION_PATH = ROOT_PATH + "/configuration";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = ZEROCONF_CONFIGURATION_PATH,
            method = RequestMethod.GET,
            beanClass = ZeroconfService.class,
            beanMethod = "apimapConfiguration",
            operation = @Operation(
                operationId = "Apimap Configuration Discovery",
                summary = "Apimap Configuration Discovery",
                description = "Apimap Configuration Discovery",
                tags = {"ZEROCONF"}
            )
        )
    })

    /*
	    /.zeroconf/configuration
   */

    RouterFunction<ServerResponse> configurationRoutes(ZeroconfService zeroconfService) {
        return RouterFunctions
            .route(GET(ZEROCONF_CONFIGURATION_PATH).and(accept(MediaType.APPLICATION_JSON)), zeroconfService::apimapConfiguration);
    }
}
