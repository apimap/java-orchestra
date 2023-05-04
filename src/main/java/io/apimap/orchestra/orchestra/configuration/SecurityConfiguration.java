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

package io.apimap.orchestra.orchestra.configuration;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.apimap.orchestra.orchestra.configuration.jwtConverter.JwtConverterFactory;
import io.apimap.orchestra.orchestra.configuration.jwtDecoder.JwtDecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerReactiveAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    protected final OIDCConfiguration oidcConfiguration;

    @SuppressFBWarnings
    public SecurityConfiguration(OIDCConfiguration oidcConfiguration) {
        this.oidcConfiguration = oidcConfiguration;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        final String[] ACCOUNT_PATH = {"/account", "/account/**"};
        final String[] OAUTH_PATH = {"/oauth2/authorize**", "/oauth2/access_token**"};
        final String[] WELL_KNOWN_PATH = {"/.well-known/**"};
        final String[] SSO_PATH = {"/sso/idps/**"};
        final String[] ZEROCONF_PATH = {"/.zeroconf/**"};
        final String[] DOCUMENTATION_PATH = {"/documentation/**", "/actuator/**"};

        // only method-by-method security
        return http
            .authorizeExchange()
                .pathMatchers(HttpMethod.GET, ACCOUNT_PATH)
                    .hasAuthority("SCOPE_account:management")
                .pathMatchers(HttpMethod.POST, ACCOUNT_PATH)
                    .hasAuthority("SCOPE_account:management")
                .pathMatchers(HttpMethod.PUT, ACCOUNT_PATH)
                    .hasAuthority("SCOPE_account:management")
                .pathMatchers(HttpMethod.DELETE, ACCOUNT_PATH)
                    .hasAuthority("SCOPE_account:management")
                .pathMatchers(HttpMethod.GET, WELL_KNOWN_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.GET, OAUTH_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.POST, OAUTH_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.GET, SSO_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.GET, ZEROCONF_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.GET, DOCUMENTATION_PATH)
                    .permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                .anyExchange()
                    .denyAll()
            .and()
            .oauth2ResourceServer(oauth2 -> oauth2
                .authenticationManagerResolver(authManager())
            )
            .build();
    }

    public JwtIssuerReactiveAuthenticationManagerResolver authManager() {
        Map<String, Mono<ReactiveAuthenticationManager>> authenticationManagers = new HashMap<>();

        oidcConfiguration.issuers.forEach(provider -> {
            JwtReactiveAuthenticationManager manager = new JwtReactiveAuthenticationManager(JwtDecoderFactory.getJwtDecoder(provider));
            manager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(JwtConverterFactory.getJwtConverter(provider)));
            authenticationManagers.put(provider.issuer, Mono.just(manager));

            LOGGER.debug("Created JWT decoder for provider {}", provider.getIssuer());
        });

        return new JwtIssuerReactiveAuthenticationManagerResolver(authenticationManagers::get);
    }
}
