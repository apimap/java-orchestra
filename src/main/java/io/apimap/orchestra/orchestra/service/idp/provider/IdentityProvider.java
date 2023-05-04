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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.service.idp.TokenRequestResponse;
import io.apimap.orchestra.orchestra.service.idp.entity.IdPUser;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public abstract class IdentityProvider {

    protected WebClient webClient;
    protected OIDCConfiguration.Idp idp;

    @SuppressFBWarnings
    public IdentityProvider(WebClient webClient, OIDCConfiguration.Idp idp) {
        this.webClient = webClient;
        this.idp = idp;
    }

    public abstract Mono<TokenRequestResponse> retrieveAccessToken(final String code);

    public abstract Mono<IdPUser> retrieveUser(final TokenRequestResponse token, String jwks);

    public abstract Mono<URI> getAuthorizeURI(final String state);
}
