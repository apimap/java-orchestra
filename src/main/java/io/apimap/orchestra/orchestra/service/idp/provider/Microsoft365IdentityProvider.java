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

package io.apimap.orchestra.orchestra.service.idp.provider;

import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.service.idp.TokenRequestResponse;
import io.apimap.orchestra.orchestra.service.idp.entity.IdPUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class Microsoft365IdentityProvider extends IdentityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Microsoft365IdentityProvider.class);

    public Microsoft365IdentityProvider(WebClient webClient, OIDCConfiguration.Idp idp) {
        super(webClient, idp);
    }

    @Override
    public Mono<TokenRequestResponse> retrieveAccessToken(String code) {
        final String body = String.format(
            "client_id=%s&client_secret=%s&code=%s&grant_type=%s",
            idp.getClientId(),
            idp.getSecret(),
            code,
            "authorization_code"
        );

        final URI uri = URI.create(idp.getAccessTokenEndpoint());

        return webClient
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(TokenRequestResponse.class)
            .doOnError(error -> LOGGER.error("Token request failed with {}", error.toString()));
    }

    @Override
    public Mono<IdPUser> retrieveUser(TokenRequestResponse token, String jwks) {
        NimbusReactiveJwtDecoder jwtDecoder = new NimbusReactiveJwtDecoder(jwks);

        OAuth2TokenValidator<Jwt> tokenValidator = new DelegatingOAuth2TokenValidator<>();
        jwtDecoder.setJwtValidator(tokenValidator);

        return jwtDecoder
            .decode(token.getIdToken())
            .map(idToken -> new IdPUser(idToken.getClaim("email")));
    }

    @Override
    public Mono<URI> getAuthorizeURI(String state) {
        final String query = String.format("?scope=%s&client_id=%s&state=%s&response_type=code", idp.getScope(), idp.getClientId(), state);
        return Mono.justOrEmpty(URI.create(idp.getAuthorizeEndpoint() + query));
    }
}