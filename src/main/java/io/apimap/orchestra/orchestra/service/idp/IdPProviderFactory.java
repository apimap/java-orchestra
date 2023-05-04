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

package io.apimap.orchestra.orchestra.service.idp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.service.idp.entity.IdPUser;
import io.apimap.orchestra.orchestra.service.idp.provider.GitHubIdentityProvider;
import io.apimap.orchestra.orchestra.service.idp.provider.Microsoft365IdentityProvider;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class IdPProviderFactory {
    protected WebClient webClient;
    protected OIDCConfiguration.Idp idp;

    public IdPProviderFactory() {
    }

    @SuppressFBWarnings
    public IdPProviderFactory(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressFBWarnings
    public static IdPProviderFactory withClient(WebClient webClient) {
        return new IdPProviderFactory(webClient);
    }

    @SuppressFBWarnings
    public IdPProviderFactory withIdp(OIDCConfiguration.Idp IdP) {
        this.setIdp(IdP);
        return this;
    }

    @SuppressFBWarnings
    public void setIdp(OIDCConfiguration.Idp idp) {
        this.idp = idp;
    }

    public Mono<URI> getAuthorizeURI(final String state) {
        return switch (idp.getIdp()) {
            case "github" -> new GitHubIdentityProvider(webClient, idp).getAuthorizeURI(state);
            case "microsoft365" -> new Microsoft365IdentityProvider(webClient, idp).getAuthorizeURI(state);
            default -> Mono.empty();
        };
    }

    public Mono<TokenRequestResponse> retrieveAccessToken(final String code) {
        return switch (idp.getIdp()) {
            case "github" -> new GitHubIdentityProvider(webClient, idp).retrieveAccessToken(code);
            case "microsoft365" -> new Microsoft365IdentityProvider(webClient, idp).retrieveAccessToken(code);
            default -> Mono.empty();
        };
    }

    public Mono<IdPUser> retrieveUser(final TokenRequestResponse token) {
        return switch (idp.getIdp()) {
            case "github" -> new GitHubIdentityProvider(webClient, idp).retrieveUser(token, idp.getJwks());
            case "microsoft365" -> new Microsoft365IdentityProvider(webClient, idp).retrieveUser(token, idp.getJwks());
            default -> Mono.empty();
        };
    }

    @Override
    public String toString() {
        return "IdPProviderFactory{" +
            "webClient=" + webClient +
            ", idp=" + idp +
            '}';
    }
}
