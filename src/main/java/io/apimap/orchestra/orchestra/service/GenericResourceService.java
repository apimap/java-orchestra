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
import io.apimap.orchestra.orchestra.configuration.ApimapConfiguration;
import io.apimap.orchestra.orchestra.service.response.ResponseBuilder;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import io.apimap.rest.jsonapi.JsonApiRestResponseWrapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class GenericResourceService {
    final protected ApimapConfiguration apimapConfiguration;

    @SuppressFBWarnings
    public GenericResourceService(final ApimapConfiguration apimapConfiguration) {
        this.apimapConfiguration = apimapConfiguration;
    }

    public Mono<ServerResponse> genericResource(final ServerRequest request) {
        final long startTime = System.currentTimeMillis();

        return request
            .principal()
            .map(principal -> (JwtAuthenticationToken) principal)
            .flatMap(jwt -> ResponseBuilder
                .builder(startTime, apimapConfiguration)
                .withResourceURI(request.uri())
                .withoutBody()
                .addRelatedRef(JsonApiRestResponseWrapper.ORGANIZATION_COLLECTION, URIUtil.accountFromURI(request.uri()).uriValue())
                .okResource()
            );
    }

    public Mono<ServerResponse> blank(final ServerRequest request) {
        return ServerResponse.status(HttpStatusCode.valueOf(200))
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue("Apimap.io");
    }
}
