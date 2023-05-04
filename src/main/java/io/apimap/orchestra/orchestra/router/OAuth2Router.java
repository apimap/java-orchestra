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

import io.apimap.orchestra.orchestra.service.OAuth2Service;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class OAuth2Router {
    public static final String CLIENT_IDENTIFIER_KEY = "serviceIdentifier";
    public static final String CLIENT_IDENTIFIER_KEY_REGEXP = CLIENT_IDENTIFIER_KEY + ":[a-z0-9A-Z]{1,24}";


    public static final String ROOT_PATH = "/oauth2";
    public static final String AUTHORIZE_PATH = ROOT_PATH + "/authorize";
    public static final String ACCESS_TOKEN_PATH = ROOT_PATH + "/access_token";
    public static final String INTEGRATION_PATH = ROOT_PATH + "/{" + CLIENT_IDENTIFIER_KEY_REGEXP + "}";
    public static final String INTEGRATION_TOKENS_SERVICE_PATH = INTEGRATION_PATH + "/access_token";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = ACCESS_TOKEN_PATH,
            method = RequestMethod.GET,
            beanClass = OAuth2Service.class,
            beanMethod = "getAccessToken",
            operation = @Operation(
                operationId = "Get Oauth Access Token",
                summary = "Initiate the process of acquiring an disposable jwt token",
                description = "Initiate the process of acquiring an disposable jwt token",
                tags = {"OAUTH"}
            )
        ),
        @RouterOperation(
            path = ACCESS_TOKEN_PATH,
            method = RequestMethod.POST,
            beanClass = OAuth2Service.class,
            beanMethod = "getAccountDisposableJwtToken",
            operation = @Operation(
                operationId = "Get Oauth Access Token using account + secret",
                summary = "Get a disposable jwt token based on account + a secret",
                description = "Get a disposable jwt token based on account + a secret",
                tags = {"OAUTH"}
            )
        ),
        @RouterOperation(
            path = AUTHORIZE_PATH,
            method = RequestMethod.GET,
            beanClass = OAuth2Service.class,
            beanMethod = "redirectToIdP",
            operation = @Operation(
                operationId = "Redirect to IdP",
                summary = "Get a url redirect to the selected IdP",
                description = "Get a url redirect to the selected IdP",
                tags = {"OAUTH"},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "serviceIdentifier", description = "Service provider name")
                }
            )
        ),
        @RouterOperation(
            path = INTEGRATION_TOKENS_SERVICE_PATH,
            method = RequestMethod.GET,
            beanClass = OAuth2Service.class,
            beanMethod = "getCICDDisposableJwtToken",
            operation = @Operation(
                operationId = "CI/CD Disposable JWT",
                summary = "Get a Disposable jwt to be used in CI/CD integrations",
                description = "Get a Disposable jwt to be used in CI/CD integrations",
                tags = {"OAUTH"},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "serviceIdentifier", description = "Service provider name")
                }
            )
        ),
    })

    /*
        /oauth2/authorize
        /oauth2/access_token
        /oauth2/{serviceIdentifier}
        /oauth2/{serviceIdentifier}/access_token
    */

    RouterFunction<ServerResponse> oauthRoutes(OAuth2Service oAuth2Service) {
        return RouterFunctions
            .route(GET(ACCESS_TOKEN_PATH), oAuth2Service::getAccessToken)
            .andRoute(POST(ACCESS_TOKEN_PATH), oAuth2Service::getAccountDisposableJwtToken)
            .andRoute(GET(AUTHORIZE_PATH), oAuth2Service::redirectToIdP)
            .andRoute(GET(INTEGRATION_TOKENS_SERVICE_PATH), oAuth2Service::getCICDDisposableJwtToken);
    }
}
