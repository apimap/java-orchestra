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
import io.apimap.orchestra.orchestra.service.idp.entity.GitHubIdPUser;
import io.apimap.orchestra.orchestra.service.idp.entity.IdPUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class GitHubIdentityProvider extends IdentityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubIdentityProvider.class);

    private static final URI USER_ENDPOINT_URL = URI.create("https://api.github.com/user");

    public GitHubIdentityProvider(WebClient webClient, OIDCConfiguration.Idp idp) {
        super(webClient, idp);
    }

    @Override
    public Mono<TokenRequestResponse> retrieveAccessToken(String code) {
        final String query = String.format(
            "?client_id=%s&client_secret=%s&code=%s",
            idp.getClientId(),
            idp.getSecret(),
            code);

        final URI uri = URI.create(idp.getAccessTokenEndpoint() + query);

        return webClient
            .post()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(TokenRequestResponse.class)
            .doOnError(error -> LOGGER.error("Token request failed with {}", error.toString()));
    }

    @Override
    public Mono<IdPUser> retrieveUser(TokenRequestResponse token, String jwks) {
        return webClient
            .get()
            .uri(USER_ENDPOINT_URL)
            .accept(MediaType.valueOf("application/vnd.github+json"))
            .header("Authorization", "Bearer " + token.getAccessToken())
            .header("X-GitHub-Api-Version", "2022-11-28")
            .retrieve()
            .bodyToMono(GitHubIdPUser.class)
            .map(gitHubIdPUser -> new IdPUser(String.valueOf(gitHubIdPUser.getId())))
            .doOnError(error -> LOGGER.error("Token request failed with {}", error.toString()));
    }

    @Override
    public Mono<URI> getAuthorizeURI(String state) {
        final String query = String.format("?scope=%s&client_id=%s&state=%s", idp.getScope(), idp.getClientId(), state);
        return Mono.justOrEmpty(URI.create(idp.getAuthorizeEndpoint() + query));
    }
}
