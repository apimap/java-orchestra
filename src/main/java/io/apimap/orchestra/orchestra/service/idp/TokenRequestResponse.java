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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRequestResponse {
    @JsonProperty("access_token")
    protected String accessToken;

    @JsonProperty("scope")
    protected String scope;

    @JsonProperty("token_type")
    protected String tokenType;

    @JsonProperty("id_token")
    protected String idToken;

    public TokenRequestResponse() {
    }

    public TokenRequestResponse(final String accessToken,
                                final String scope,
                                final String tokenType,
                                final String idToken) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "TokenRequestResponse{" +
            "accessToken='" + accessToken + '\'' +
            ", scope='" + scope + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", profileInfo='" + idToken + '\'' +
            '}';
    }
}
