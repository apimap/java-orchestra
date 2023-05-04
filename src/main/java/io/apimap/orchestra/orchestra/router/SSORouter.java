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

import io.apimap.orchestra.orchestra.service.SSOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class SSORouter {
    public static final String IDP_KEY = "idpIdentifier";
    public static final String IDP_KEY_REGEXP = IDP_KEY + ":[a-zA-Z0-9]{1,24}";


    public static final String ROOT_PATH = "/sso";
    public static final String IDPS_PATH = ROOT_PATH + "/idps";
    public static final String IDP_PATH = IDPS_PATH + "/{" + IDP_KEY_REGEXP + "}";
    public static final String IDP_INITIALIZE_PATH = IDP_PATH + "/authorize";
    public static final String IDP_CALLBACK_PATH = IDP_PATH + "/callback";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = IDP_INITIALIZE_PATH,
            method = RequestMethod.GET,
            beanClass = SSOService.class,
            beanMethod = "redirectToIdP",
            operation = @Operation(
                operationId = "Redirect to Default IdP",
                summary = "Redirect to the default IdP used by Apimap",
                description = "Redirect to the default IdP used by Apimap",
                tags = {"SSO"},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "idpIdentifier", description = "IdP provider name")
                }
            )
        ),
        @RouterOperation(
            path = IDP_CALLBACK_PATH,
            method = RequestMethod.GET,
            beanClass = SSOService.class,
            beanMethod = "callbackFromIdP",
            operation = @Operation(
                operationId = "Callback from Default IdP",
                summary = "Redirect to the default IdP used by Apimap",
                description = "Redirect to the default IdP used by Apimap",
                tags = {"SSO"},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "idpIdentifier", description = "IdP provider name")
                }
            )
        )
    })

    /*
        /sso/idps/<idpIdentifier>/authorize
        /sso/idps/<idpIdentifier>/callback
    */

    RouterFunction<ServerResponse> ssoRoutes(SSOService ssoService) {
        return RouterFunctions
            .route(GET(IDP_INITIALIZE_PATH), ssoService::redirectToIdP)
            .andRoute(GET(IDP_CALLBACK_PATH), ssoService::callbackFromIdP);
    }
}
