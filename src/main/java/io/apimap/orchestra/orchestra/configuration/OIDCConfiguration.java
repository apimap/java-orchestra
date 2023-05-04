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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "apimap.oidc")
public class OIDCConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(OIDCConfiguration.class);

    protected final ArrayList<Issuer> issuers;
    protected final String issuer;
    protected final RsaAlgorithm algorithm;
    protected ArrayList<Idp> idps;

    @ConstructorBinding
    public OIDCConfiguration(final ArrayList<Issuer> issuers,
                             final String issuer,
                             final RsaAlgorithm algorithm,
                             final ArrayList<Idp> idps) {
        this.issuers = new ArrayList<>(issuers);
        this.issuer = issuer;
        this.algorithm = algorithm;
        this.idps = new ArrayList<>(idps);
    }

    public String getIssuer() {
        return issuer;
    }

    public ArrayList<Issuer> getIssuers() {
        return new ArrayList<>(issuers);
    }

    public ArrayList<Idp> getIdps() {
        return new ArrayList<>(idps);
    }

    public void setIdps(ArrayList<Idp> idps) {
        this.idps = new ArrayList<>(idps);
    }

    public RsaAlgorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return "OIDCConfiguration{" +
            "issuers=" + issuers +
            ", issuer='" + issuer + '\'' +
            ", algorithm=" + algorithm +
            ", idps=" + idps +
            '}';
    }

    public static class Issuer {

        protected String issuer;
        protected String openidConfiguration;
        protected String audience;
        protected List<String> whitelisted;
        protected String jwks;
        protected Decoder decoder;

        public Issuer(final String issuer,
                      final String openidConfiguration,
                      final String audience,
                      final List<String> whitelisted,
                      final String jwks,
                      final Decoder decoder) {
            this.issuer = issuer;
            this.openidConfiguration = openidConfiguration;
            this.whitelisted = whitelisted != null ? new ArrayList<>(whitelisted) : null;
            this.audience = audience;
            this.jwks = jwks;
            this.decoder = decoder;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getOpenidConfiguration() {
            return openidConfiguration;
        }

        public void setOpenidConfiguration(String openidConfiguration) {
            this.openidConfiguration = openidConfiguration;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public List<String> getWhitelisted() {
            return new ArrayList<>(whitelisted);
        }

        public void setWhitelisted(List<String> whitelisted) {
            this.whitelisted =  whitelisted != null ? new ArrayList<>(whitelisted) : null;
        }

        public String getJwks() {
            return jwks;
        }

        public void setJwks(String jwks) {
            this.jwks = jwks;
        }

        public Decoder getDecoder() {
            return decoder;
        }

        public void setDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public String toString() {
            return "Provider{" +
                "issuer='" + issuer + '\'' +
                ", openidConfiguration='" + openidConfiguration + '\'' +
                ", audience='" + audience + '\'' +
                ", whitelisted=" + whitelisted +
                ", jwks='" + jwks + '\'' +
                ", decoder=" + decoder +
                '}';
        }

        public enum Decoder {
            GITHUB,
            APIMAP
        }
    }

    public static class Idp {
        protected String idp;
        protected String authorizeEndpoint;
        protected String accessTokenEndpoint;
        protected String secret;
        protected String clientId;
        protected String scope;
        protected Boolean defaultProvider;
        protected String jwks;

        public Idp(final String idp,
                   final String authorizeEndpoint,
                   final String accessTokenEndpoint,
                   final String secret,
                   final String clientId,
                   final String scope,
                   final Boolean defaultProvider,
                   final String jwks) {
            this.idp = idp;
            this.authorizeEndpoint = authorizeEndpoint;
            this.accessTokenEndpoint = accessTokenEndpoint;
            this.clientId = clientId;
            this.secret = secret;
            this.scope = scope;
            this.defaultProvider = defaultProvider;
            this.jwks = jwks;
        }

        public Boolean getDefaultProvider() {
            return defaultProvider;
        }

        public void setDefaultProvider(Boolean defaultProvider) {
            this.defaultProvider = defaultProvider;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getIdp() {
            return idp;
        }

        public void setIdp(String idp) {
            this.idp = idp;
        }

        public String getAuthorizeEndpoint() {
            return authorizeEndpoint;
        }

        public void setAuthorizeEndpoint(String authorizeEndpoint) {
            this.authorizeEndpoint = authorizeEndpoint;
        }

        public String getAccessTokenEndpoint() {
            return accessTokenEndpoint;
        }

        public void setAccessTokenEndpoint(String accessTokenEndpoint) {
            this.accessTokenEndpoint = accessTokenEndpoint;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getJwks() {
            return jwks;
        }

        public void setJwks(String jwks) {
            this.jwks = jwks;
        }

        @Override
        public String toString() {
            return "Idp{" +
                "idp='" + idp + '\'' +
                ", authorizeEndpoint='" + authorizeEndpoint + '\'' +
                ", accessTokenEndpoint='" + accessTokenEndpoint + '\'' +
                ", secret='" + secret + '\'' +
                ", clientId='" + clientId + '\'' +
                ", scope='" + scope + '\'' +
                ", defaultProvider=" + defaultProvider +
                ", jwks='" + jwks + '\'' +
                '}';
        }
    }

    public static class RsaAlgorithm {
        protected final RSAPublicKey publicKey;
        protected final RSAPrivateKey privateKey;
        protected final String algorithm;

        public RsaAlgorithm(String publicKeyFile,
                            String privateKeyFile,
                            String algorithm) {
            this.publicKey = readPublicKey(publicKeyFile);
            this.privateKey = readPrivateKey(privateKeyFile);
            this.algorithm = algorithm;
        }

        private RSAPublicKey readPublicKey(String filepath) {
            DataInputStream dataInputStream = null;

            try {
                File key = new File(filepath);
                FileInputStream fileInputStream = new FileInputStream(key);
                dataInputStream = new DataInputStream(fileInputStream);
                byte[] bytes = new byte[(int) key.length()];
                dataInputStream.readFully(bytes);

                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            } catch (Exception exception) {
                LOGGER.debug(exception.getMessage());
            } finally {
                try {
                    if (dataInputStream != null) dataInputStream.close();
                } catch (Exception exception) {
                    LOGGER.debug(exception.getMessage());
                }
            }

            return null;
        }

        private RSAPrivateKey readPrivateKey(String filepath) {
            DataInputStream dataInputStream = null;

            try {
                File key = new File(filepath);
                FileInputStream fileInputStream = new FileInputStream(key);
                dataInputStream = new DataInputStream(fileInputStream);
                byte[] bytes = new byte[(int) key.length()];
                dataInputStream.readFully(bytes);

                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            } catch (Exception exception) {
                LOGGER.debug(exception.getMessage());
            } finally {
                try {
                    if (dataInputStream != null) dataInputStream.close();
                } catch (Exception exception) {
                    LOGGER.debug(exception.getMessage());
                }
            }

            return null;
        }

        public RSAPublicKey getPublicKey() {
            return publicKey;
        }

        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        @Override
        public String toString() {
            return "RsaAlgorithm{" +
                "publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                ", algorithm='" + algorithm + '\'' +
                '}';
        }
    }
}
