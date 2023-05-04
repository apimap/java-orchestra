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

package io.apimap.orchestra.orchestra.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.orchestra.orchestra.configuration.ApimapConfiguration;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.convert.AccessTokenConverter;
import io.apimap.orchestra.orchestra.convert.AccountConverter;
import io.apimap.orchestra.orchestra.repository.mongodb.MongoDBAccessTokenRepository;
import io.apimap.orchestra.orchestra.repository.mongodb.MongoDBAccountRepository;
import io.apimap.orchestra.orchestra.router.AccountRouter;
import io.apimap.orchestra.orchestra.service.response.ResponseBuilder;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import io.apimap.orchestra.rest.AccessTokenCollectionRootRestEntity;
import io.apimap.orchestra.rest.AccessTokenDataRestEntity;
import io.apimap.orchestra.rest.AccountCollectionRootRestEntity;
import io.apimap.orchestra.rest.AccountDataRestEntity;
import io.apimap.rest.jsonapi.JsonApiRestRequestWrapper;
import io.apimap.rest.jsonapi.JsonApiRestResponseWrapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

@Service
public class AccountService {
    final protected MongoDBAccountRepository accountRepository;
    final protected MongoDBAccessTokenRepository accessTokenRepository;
    final protected OIDCConfiguration oidcConfiguration;
    final protected ApimapConfiguration apimapConfiguration;

    @SuppressFBWarnings
    public AccountService(final MongoDBAccountRepository accountRepository,
                          final MongoDBAccessTokenRepository accessTokenRepository,
                          final OIDCConfiguration oidcConfiguration,
                          final ApimapConfiguration apimapConfiguration) {
        this.accountRepository = accountRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.oidcConfiguration = oidcConfiguration;
        this.apimapConfiguration = apimapConfiguration;
    }

    /*
    Accounts
     */
    public Mono<ServerResponse> getAccounts(final ServerRequest request) {
        final AccountConverter converter = new AccountConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        return request
            .principal()
            .map(principal -> ((JwtAuthenticationToken) principal).getDetails())
            .flatMapMany(details -> accountRepository.all())
            .collectList()
            .map(account -> converter.convertToRestEntities(uri, account))
            .flatMap(accounts -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(uri)
                .withBody(new JsonApiRestResponseWrapper<>(new AccountCollectionRootRestEntity(accounts)))
                .okCollection()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> createAccount(final ServerRequest request) {
        final AccountConverter converter = new AccountConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        final JavaType type = new ObjectMapper()
            .getTypeFactory()
            .constructParametricType(JsonApiRestRequestWrapper.class, AccountDataRestEntity.class);

        return request
            .principal()
            .map(principal -> ((JwtAuthenticationToken) principal).getDetails())
            .flatMap(details -> request
                .bodyToMono(ParameterizedTypeReference.forType(type))
                .filter(Objects::nonNull)
                .map(input -> converter.convertToDatabaseEntity((AccountDataRestEntity) ((JsonApiRestRequestWrapper<?>) input).getData()))
                .flatMap(accountRepository::add)
                .map(e -> converter.convertToRestEntity(uri, e))
            )
            .flatMap(account -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(uri)
                .withBody(new JsonApiRestResponseWrapper<>(account))
                .created())
            .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    /*
    Account
     */
    public Mono<ServerResponse> getAccount(final ServerRequest request) {
        final AccountConverter converter = new AccountConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .filter(principal -> principal.getName().equals(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMap(principal -> accountRepository.getByPublicId(principal.getName()))
            .map(account -> converter.convertToRestEntity(uri, account))
            .flatMap(account -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(uri)
                .withBody(new JsonApiRestResponseWrapper<>(account))
                .okResource()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> deleteAccount(final ServerRequest request) {
        final long startTime = System.currentTimeMillis();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .filter(principal -> principal.getName().equals(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMap(principal -> accountRepository.deleteByPublicId(principal.getName()))
            .filter(value -> (Boolean) value)
            .flatMap(result -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .noContent()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    /*
    Tokens
     */
    public Mono<ServerResponse> getTokens(final ServerRequest request) {
        final AccessTokenConverter converter = new AccessTokenConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .filter(principal -> principal.getName().equals(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMapMany(details -> accessTokenRepository
                .allBy(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY))
                .doOnNext(token -> token.setSecret("*****"))
            )
            .collectList()
            .map(tokens -> converter.convertToRestEntities(URIUtil
                .accountFromURI(uri)
                .append(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY))
                .append("token").uriValue(), tokens))
            .flatMap(tokens -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(uri)
                .withBody(new JsonApiRestResponseWrapper<>(new AccessTokenCollectionRootRestEntity(tokens)))
                .cacheDisabled()
                .okCollection()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> createToken(final ServerRequest request) {
        final AccessTokenConverter converter = new AccessTokenConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        final JavaType type = new ObjectMapper()
            .getTypeFactory()
            .constructParametricType(JsonApiRestRequestWrapper.class, AccessTokenDataRestEntity.class);

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .flatMap(principal -> request
                .bodyToMono(ParameterizedTypeReference.forType(type))
                .filter(Objects::nonNull)
                .map(input -> converter.convertToDatabaseEntity((AccessTokenDataRestEntity) ((JsonApiRestRequestWrapper<?>) input).getData()))
                .doOnNext(dto -> dto.setaccountId(principal.getName()))
                .flatMap(dto -> accessTokenRepository.add(dto))
                .map(token -> converter.convertToRestEntity(URIUtil
                    .accountFromURI(uri)
                    .append(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY))
                    .append("token").uriValue(), token))
            )
            .flatMap(response -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(URI.create(response.getUri()))
                .withBody(new JsonApiRestResponseWrapper<>(response))
                .created())
            .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    /*
    Token
     */
    public Mono<ServerResponse> getToken(final ServerRequest request) {
        final AccessTokenConverter converter = new AccessTokenConverter();
        final long startTime = System.currentTimeMillis();
        final URI uri = request.uri();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .filter(principal -> principal.getName().equals(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMap(details -> accessTokenRepository.getBy(
                request.pathVariable(AccountRouter.ACCOUNT_ID_KEY),
                request.pathVariable(AccountRouter.TOKEN_ID_KEY)))
            .map(token -> converter.convertToRestEntity(URIUtil
                .accountFromURI(uri)
                .append(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY))
                .append("token").uriValue(), token))
            .doOnSuccess(token -> token.setToken("*****"))
            .flatMap(token -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(uri)
                .withBody(new JsonApiRestResponseWrapper<>(token))
                .okCollection()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> deleteToken(final ServerRequest request) {
        final long startTime = System.currentTimeMillis();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .filter(principal -> principal.getName().equals(request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMap(principal -> accessTokenRepository.deleteBy(
                request.pathVariable(AccountRouter.TOKEN_ID_KEY),
                request.pathVariable(AccountRouter.ACCOUNT_ID_KEY)))
            .flatMap(result -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .noContent()
            ).switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }
}
