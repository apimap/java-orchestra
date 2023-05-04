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

package io.apimap.orchestra.orchestra.configuration.jwtConverter;

import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.service.jwt.ApimapToken;
import io.apimap.orchestra.orchestra.service.jwt.GitHubToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class JwtConverterFactory {
    public static Converter<Jwt, AbstractAuthenticationToken> getJwtConverter(final OIDCConfiguration.Issuer issuer) {
        if (issuer.getDecoder().equals(OIDCConfiguration.Issuer.Decoder.GITHUB)) {
            return new GithubJwtAuthenticationConverter();
        }

        if (issuer.getDecoder().equals(OIDCConfiguration.Issuer.Decoder.APIMAP)) {
            return new ApimapJwtAuthenticationConverter();
        }

        return null;
    }

    private static class GithubJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        public AbstractAuthenticationToken convert(Jwt jwt) {
            AbstractAuthenticationToken token = converter.convert(jwt);

            GitHubToken details = new GitHubToken(
                jwt.getClaim("actor"),
                jwt.getClaim("actor_id"),
                jwt.getClaim("base_ref"),
                jwt.getClaim("event_name"),
                jwt.getClaim("head_ref"),
                jwt.getClaim("job_workflow_ref"),
                jwt.getClaim("job_workflow_sha"),
                jwt.getClaim("ref"),
                jwt.getClaim("ref_type"),
                jwt.getClaim("repository_visibility"),
                jwt.getClaim("repository"),
                jwt.getClaim("repository_id"),
                jwt.getClaim("repository_owner"),
                jwt.getClaim("repository_owner_id"),
                jwt.getClaim("run_id"),
                jwt.getClaim("run_number"),
                jwt.getClaim("run_attempt"),
                jwt.getClaim("workflow"),
                jwt.getClaim("workflow_ref"),
                jwt.getClaim("workflow_sha")
            );

            if(token != null) {
                token.setDetails(details);
            }

            return token;
        }
    }

    private static class ApimapJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        public AbstractAuthenticationToken convert(Jwt jwt) {
            AbstractAuthenticationToken token = converter.convert(jwt);
            if(token != null) {
                token.setDetails(new ApimapToken());
            }
            return token;
        }
    }
}
