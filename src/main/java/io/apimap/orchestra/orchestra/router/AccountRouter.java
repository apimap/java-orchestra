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

import io.apimap.orchestra.orchestra.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {
    public static final String ACCOUNT_ID_KEY = "accountId";
    public static final String ACCOUNT_ID_KEY_REGEXP = ACCOUNT_ID_KEY + ":[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    public static final String TOKEN_ID_KEY = "tokenId";
    public static final String TOKEN_ID_KEY_REGEXP = TOKEN_ID_KEY + ":[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    public static final String ROOT_PATH = "/account";
    public static final String ACCOUNT_ITEM_PATH = ROOT_PATH + "/{" + ACCOUNT_ID_KEY_REGEXP + "}";
    public static final String TOKEN_PATH = ACCOUNT_ITEM_PATH + "/token";
    public static final String TOKEN_ITEM_PATH = TOKEN_PATH + "/{" + TOKEN_ID_KEY_REGEXP + "}";

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = ROOT_PATH,
            method = RequestMethod.GET,
            beanClass = AccountService.class,
            beanMethod = "getAccounts",
            operation = @Operation(
                operationId = "Get Accounts",
                summary = "Get all accounts",
                description = "This endpoint gives you all the accounts your credentials have access to.",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")}
            )
        ),
        @RouterOperation(
            path = ROOT_PATH,
            method = RequestMethod.POST,
            beanClass = AccountService.class,
            beanMethod = "createAccount",
            operation = @Operation(
                operationId = "Create Account",
                summary = "Create a new account",
                description = "This endpoint creates a new account",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")}
            )
        ),
        @RouterOperation(
            path = ACCOUNT_ITEM_PATH,
            method = RequestMethod.GET,
            beanClass = AccountService.class,
            beanMethod = "getAccount",
            operation = @Operation(
                operationId = "Get Account",
                summary = "Get an account",
                description = "This endpoint returns an account if your credentials have access to it.",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID")
                }
            )
        ),
        @RouterOperation(
            path = ACCOUNT_ITEM_PATH,
            method = RequestMethod.DELETE,
            beanClass = AccountService.class,
            beanMethod = "deleteAccount",
            operation = @Operation(
                operationId = "Delete Account",
                summary = "Delete an account",
                description = "This endpoint deletes an account if your credentials have access to it.",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID")
                }
            )
        ),
        @RouterOperation(
            path = TOKEN_PATH,
            method = RequestMethod.GET,
            beanClass = AccountService.class,
            beanMethod = "getTokens",
            operation = @Operation(
                operationId = "Get Account Access Tokens",
                summary = "Get all Access Tokens associated with an account",
                description = "This endpoint returns all account access tokens if your credentials have access to it.",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID")
                }
            )
        ),
        @RouterOperation(
            path = TOKEN_PATH,
            method = RequestMethod.POST,
            beanClass = AccountService.class,
            beanMethod = "createToken",
            operation = @Operation(
                operationId = "Create Account Access Tokens",
                summary = "Get all Access Tokens associated with an account",
                description = "This endpoint returns all account access tokens if your credentials have access to it.",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID")
                }
            )
        ),
        @RouterOperation(
            path = TOKEN_ITEM_PATH,
            method = RequestMethod.GET,
            beanClass = AccountService.class,
            beanMethod = "getToken",
            operation = @Operation(
                operationId = "Get Account Access Token",
                summary = "Get an Access Tokens associated with an account",
                description = "This endpoint returns an account access token",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID"),
                    @Parameter(in = ParameterIn.PATH, name = "tokenId", description = "Token ID")
                }
            )
        ),
        @RouterOperation(
            path = TOKEN_ITEM_PATH,
            method = RequestMethod.DELETE,
            beanClass = AccountService.class,
            beanMethod = "deleteToken",
            operation = @Operation(
                operationId = "Delete Account Access Token",
                summary = "Delete an Access Tokens associated with an account",
                description = "This endpoint returns an account access token",
                tags = {"ACCOUNT"},
                security = {@SecurityRequirement(name = "JWT")},
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "accountId", description = "Account ID"),
                    @Parameter(in = ParameterIn.PATH, name = "tokenId", description = "Token ID")
                }
            )
        )
    })

    /*
        /account
        /account/{accountId}
        /account/{accountId}/token
        /account/{accountId}/token/{tokenId}
    */

    RouterFunction<ServerResponse> accountRoutes(AccountService accountService) {
        return RouterFunctions
            .route(GET(ROOT_PATH).and(accept(MediaType.APPLICATION_JSON)), accountService::getAccounts)
            .andRoute(POST(ROOT_PATH).and(contentType(MediaType.APPLICATION_JSON)), accountService::createAccount)
            .andRoute(GET(ACCOUNT_ITEM_PATH).and(accept(MediaType.APPLICATION_JSON)), accountService::getAccount)
            .andRoute(DELETE(ACCOUNT_ITEM_PATH), accountService::deleteAccount)
            .andRoute(GET(TOKEN_PATH).and(accept(MediaType.APPLICATION_JSON)), accountService::getTokens)
            .andRoute(POST(TOKEN_PATH).and(contentType(MediaType.APPLICATION_JSON)), accountService::createToken)
            .andRoute(GET(TOKEN_ITEM_PATH).and(accept(MediaType.APPLICATION_JSON)), accountService::getToken)
            .andRoute(DELETE(TOKEN_ITEM_PATH), accountService::deleteToken)
            ;
    }
}