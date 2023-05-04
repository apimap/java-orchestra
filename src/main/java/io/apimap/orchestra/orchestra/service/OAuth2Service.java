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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.oauth.TokenErrorResponse;
import io.apimap.oauth.TokenSuccessfulResponse;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.oidc.JWTGenerator;
import io.apimap.orchestra.orchestra.repository.mongodb.MongoDBAccessTokenRepository;
import io.apimap.orchestra.orchestra.repository.mongodb.MongoDBAccountRepository;
import io.apimap.orchestra.orchestra.repository.mongodb.documents.Account;
import io.apimap.orchestra.orchestra.router.OAuth2Router;
import io.apimap.orchestra.orchestra.service.idp.IdPProviderFactory;
import io.apimap.orchestra.orchestra.service.jwt.GitHubToken;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class OAuth2Service {
    protected final OIDCConfiguration oidcConfiguration;
    protected final MongoDBAccessTokenRepository accessTokenRepository;
    protected final MongoDBAccountRepository accountRepository;

    final protected WebClient webClient;

    @SuppressFBWarnings
    public OAuth2Service(final OIDCConfiguration oidcConfiguration,
                         final WebClient.Builder builder,
                         final MongoDBAccountRepository accountRepository,
                         final MongoDBAccessTokenRepository accessTokenRepository) {
        this.oidcConfiguration = oidcConfiguration;
        this.accountRepository = accountRepository;
        this.webClient = builder.build();
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * Redirect user to the default provider
     *
     * @param request HTTP Request
     * @return Redirect server response
     */
    public Mono<ServerResponse> redirectToIdP(final ServerRequest request) {
        return request
            .queryParam("state")
            .map(state -> request
                .queryParam("callback")
                .map(callback -> Flux.fromStream(oidcConfiguration
                        .getIdps()
                        .stream())
                    .filter(OIDCConfiguration.Idp::getDefaultProvider)
                    .next()
                    .map(idp -> URIUtil.idpsFromURI(request.uri())
                        .append(idp.getIdp())
                        .append("authorize")
                        .query(String.format("?state=%s&callback=%s", state, callback))
                        .uriValue())
                    .flatMap(uri -> ServerResponse
                        .temporaryRedirect(uri)
                        .build())
                    .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build())))
                .orElseGet(() -> ServerResponse.badRequest().build()))
            .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getAccountDisposableJwtToken(final ServerRequest request){
        return request
            .queryParam("client_id")
            .map(clientId -> request
                .queryParam("client_secret")
                .map(clientSecret -> accessTokenRepository
                    .getBySecret(clientId, clientSecret)
                    .filter(accessToken -> Instant.now().isAfter(accessToken.getValidity().getFrom()))
                    .filter(accessToken -> Instant.now().isBefore(accessToken.getValidity().getTo()))
                    .flatMap(validToken -> {
                        JWTGenerator generator = new JWTGenerator(
                            oidcConfiguration.getAlgorithm().getPublicKey(),
                            oidcConfiguration.getAlgorithm().getPrivateKey(),
                            oidcConfiguration.getIssuer()
                        );

                        String accessToken = generator.generateDisposableToken(validToken.getPublicId());
                        String idToken = generator.generateIdToken(validToken.getPublicId());

                        TokenSuccessfulResponse tokenSuccessfulResponse = new TokenSuccessfulResponse();
                        tokenSuccessfulResponse.setAccessToken(accessToken);
                        tokenSuccessfulResponse.setExpiresIn(JWTGenerator.DISPOSABLE_TOKEN_LIFETIME_SECONDS);
                        tokenSuccessfulResponse.setIdToken(idToken);

                        return ServerResponse.status(200)
                            .contentType(MediaType.APPLICATION_JSON)
                            .cacheControl(CacheControl.noStore())
                            .body(BodyInserters.fromValue(tokenSuccessfulResponse));
                        }
                    )
                    .switchIfEmpty(Mono.defer(() -> ServerResponse.badRequest().build())))
                .orElseGet(() -> ServerResponse.badRequest().build()))
            .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getAccessToken(final ServerRequest request) {
        return request
            .queryParam("provider")
            .map(provider -> request
                .queryParam("code")
                .map(code -> Flux.fromStream(oidcConfiguration
                        .getIdps()
                        .stream())
                    .filter(idp -> idp.getIdp().equals(provider))
                    .next()
                    .flatMap(idp -> IdPProviderFactory.withClient(webClient).withIdp(idp).retrieveAccessToken(code)
                        .flatMap(token -> IdPProviderFactory.withClient(webClient).withIdp(idp).retrieveUser(token)))
                    .flatMap(user -> accountRepository
                        .get(user.getId())
                        .switchIfEmpty(accountRepository.add(new Account(user.getId(), provider))))
                    .flatMap(account -> {
                            JWTGenerator generator = new JWTGenerator(
                                oidcConfiguration.getAlgorithm().getPublicKey(),
                                oidcConfiguration.getAlgorithm().getPrivateKey(),
                                oidcConfiguration.getIssuer()
                            );

                            String accessToken = generator.generatePersistentToken(account.getPublicId());
                            String idToken = generator.generateIdToken(account.getPublicId());

                            TokenSuccessfulResponse tokenSuccessfulResponse = new TokenSuccessfulResponse();
                            tokenSuccessfulResponse.setAccessToken(accessToken);
                            tokenSuccessfulResponse.setExpiresIn(JWTGenerator.PERSISTENT_TOKEN_LIFETIME_SECONDS);
                            tokenSuccessfulResponse.setIdToken(idToken);

                            return ServerResponse.status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .cacheControl(CacheControl.noStore())
                                .body(BodyInserters.fromValue(tokenSuccessfulResponse));
                        }
                    )
                    .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build())))
                .orElseGet(() -> ServerResponse.badRequest().build()))
            .orElseGet(() -> ServerResponse.badRequest().build());
    }

    /*
    Create a token that is valid only to pre-approved cloud systems. This is not a token to give access to each specific
    API or information inside the catalog. It's just to give access to the catalog itself
     */
    public Mono<ServerResponse> getCICDDisposableJwtToken(final ServerRequest request) {
        return request
            .principal()
            .map(principal -> ((JwtAuthenticationToken) principal).getDetails())
            .flatMap(details -> Flux.fromStream(oidcConfiguration
                    .getIssuers()
                    .stream())
                .filter(ci -> ci.getIssuer().equals(request.pathVariable(OAuth2Router.CLIENT_IDENTIFIER_KEY)))
                .next()
                .flatMap(ci -> switch (ci.getDecoder()) {
                        case GITHUB -> githubDisposableJwtToken(ci, (GitHubToken) details);
                        default -> Mono.empty();
                })
            )
            .flatMap(token -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .cacheControl(CacheControl.noStore())
                .body(Mono.just(token), TokenSuccessfulResponse.class))
            .switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()));
    }

    protected Mono<?> githubDisposableJwtToken(final OIDCConfiguration.Issuer issuer,
                                               final GitHubToken token) {
        if(issuer
            .getWhitelisted()
            .stream()
            .anyMatch(project -> project.equals(token.getRepositoryOwner()))){
                JWTGenerator generator = new JWTGenerator(
                    oidcConfiguration.getAlgorithm().getPublicKey(),
                    oidcConfiguration.getAlgorithm().getPrivateKey(),
                    oidcConfiguration.getIssuer()
                );

                return Mono.just(new TokenSuccessfulResponse(
                    generator.generateDisposableToken(UUID.randomUUID().toString()),
                    JWTGenerator.DISPOSABLE_TOKEN_LIFETIME_SECONDS
                ));
        }

        return Mono.just(new TokenErrorResponse("Unauthorized repository owner"));
    }
}
