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
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.repository.mongodb.MongoDBAccountRepository;
import io.apimap.orchestra.orchestra.repository.nitrite.NitriteAuthorizeRequestCache;
import io.apimap.orchestra.orchestra.repository.nitrite.entities.AuthorizeRequestCache;
import io.apimap.orchestra.orchestra.router.SSORouter;
import io.apimap.orchestra.orchestra.service.idp.IdPProviderFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class SSOService {
    final protected MongoDBAccountRepository accountRepository;
    final protected OIDCConfiguration oidcConfiguration;
    final protected NitriteAuthorizeRequestCache authorizeRequestCache;

    @SuppressFBWarnings
    public SSOService(final MongoDBAccountRepository accountRepository,
                      final OIDCConfiguration oidcConfiguration,
                      final NitriteAuthorizeRequestCache authorizeRequestCache) {
        this.accountRepository = accountRepository;
        this.oidcConfiguration = oidcConfiguration;
        this.authorizeRequestCache = authorizeRequestCache;
    }

    public Mono<ServerResponse> redirectToIdP(final ServerRequest request) {
        return request
            .queryParam("callback")
            .map(callback -> request
                .queryParam("state")
                .map(state -> Flux.fromStream(oidcConfiguration
                        .getIdps()
                        .stream())
                    .filter(idp -> idp.getIdp().equals(request.pathVariable(SSORouter.IDP_KEY)))
                    .next()
                    .flatMap(idp -> new IdPProviderFactory().withIdp(idp).getAuthorizeURI(state))
                    .doOnNext(idp -> authorizeRequestCache.add(new AuthorizeRequestCache(state, callback)))
                    .flatMap(url -> ServerResponse
                        .temporaryRedirect(url)
                        .build()
                    )
                    .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build())))
                .orElseGet(() -> ServerResponse.badRequest().build()))
            .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> callbackFromIdP(final ServerRequest request) {
        return request
            .queryParam("code")
            .map(code -> request
                .queryParam("state")
                .map(state -> authorizeRequestCache
                    .getAndDelete(state)
                    .map(cache -> {
                        final String query = (String.format("?code=%s&state=%s&provider=%s", code, cache.getState(), request.pathVariable(SSORouter.IDP_KEY)));
                        return URI.create(cache.getCallback() + query);
                    })
                    .flatMap(url -> ServerResponse
                        .temporaryRedirect(url)
                        .build()
                    )
                    .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build())))
                .orElseGet(() -> ServerResponse.badRequest().build()))
            .orElseGet(() -> ServerResponse.badRequest().build());
    }
}
