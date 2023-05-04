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

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.oauth.OpenIDConfigurationResponse;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class WellKnownService {
    protected final OIDCConfiguration oidcConfiguration;

    @SuppressFBWarnings
    public WellKnownService(OIDCConfiguration oidcConfiguration) {
        this.oidcConfiguration = oidcConfiguration;
    }

    public Mono<ServerResponse> jwksJson(final ServerRequest request) {
        JWK jwk = new RSAKey.Builder(oidcConfiguration.getAlgorithm().getPublicKey())
            .privateKey(oidcConfiguration.getAlgorithm().getPrivateKey())
            .keyUse(KeyUse.SIGNATURE)
            .keyID(UUID.randomUUID().toString())
            .build();

        final JWKSet jskw = new JWKSet(jwk).toPublicJWKSet();

        return ServerResponse.status(HttpStatusCode.valueOf(200))
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noStore())
            .bodyValue(jskw.toJSONObject());
    }

    public Mono<ServerResponse> openidConfiguration(final ServerRequest request) {
        OpenIDConfigurationResponse configurationResponse = new OpenIDConfigurationResponse(
            oidcConfiguration.getIssuer(),
            URIUtil.wellKnownFromURI(request.uri()).append("openid-configuration").stringValue()
        );

        return ServerResponse.status(HttpStatusCode.valueOf(200))
            .contentType(MediaType.APPLICATION_JSON)
            .cacheControl(CacheControl.noStore())
            .body(Mono.just(configurationResponse), OpenIDConfigurationResponse.class);
    }
}