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

package io.apimap.orchestra.orchestra.oidc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

public class JWTGenerator {
    public static final String AUDIENCE = "apimap";

    // Persistent token
    public static final Integer PERSISTENT_TOKEN_LIFETIME_SECONDS = 3600;

    // Disposable token
    public static final Integer DISPOSABLE_TOKEN_LIFETIME_SECONDS = 120;

    // Generic token
    private static final String SCOPE_CLAIM_KEY = "scope";
    private static final String PERSISTENT_SCOPE_CLAIM_VALUE = "api:view api:upload taxonomy:read account:management statistics:read";
    private static final String DISPOSABLE_TOKEN_SCOPE_CLAIM_VALUE = "api:view api:upload";

    final protected RSAPublicKey publicKey;
    final protected RSAPrivateKey privateKey;
    final protected String issuer;

    public JWTGenerator(final RSAPublicKey publicKey,
                        final RSAPrivateKey privateKey,
                        final String issuer) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.issuer = issuer;
    }

    public String generateIdToken(String subject){
        final Algorithm algorithm = Algorithm.RSA256(this.publicKey, this.privateKey);

        return JWT.create()
            .withIssuer(this.issuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(PERSISTENT_TOKEN_LIFETIME_SECONDS))
            .withAudience(AUDIENCE)
            .withSubject(subject)
            .sign(algorithm);
    }

    public String generatePersistentToken(String subject) {
        final Algorithm algorithm = Algorithm.RSA256(this.publicKey, this.privateKey);

        return JWT.create()
            .withIssuer(this.issuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(PERSISTENT_TOKEN_LIFETIME_SECONDS))
            .withAudience(AUDIENCE)
            .withClaim(SCOPE_CLAIM_KEY, PERSISTENT_SCOPE_CLAIM_VALUE)
            .withSubject(subject)
            .sign(algorithm);
    }

    public String generateDisposableToken(final String subject) {
        final Algorithm algorithm = Algorithm.RSA256(this.publicKey, this.privateKey);

        return JWT.create()
            .withIssuer(this.issuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(DISPOSABLE_TOKEN_LIFETIME_SECONDS))
            .withAudience(AUDIENCE)
            .withClaim(SCOPE_CLAIM_KEY, DISPOSABLE_TOKEN_SCOPE_CLAIM_VALUE)
            .withSubject(subject)
            .sign(algorithm);
    }
}
