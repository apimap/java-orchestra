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

package io.apimap.orchestra.orchestra.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URIUtil {
    protected URI uri;

    public URIUtil(URI uri) {
        this.uri = uri;
    }

    public static URIUtil rootLevelFromURI(final URI uri) {
        URI rootURI;

        try {
            if (uri.getPort() <= 0) {
                rootURI = new java.net.URI(uri.getScheme() + "://" + uri.getHost());
            } else {
                rootURI = new java.net.URI(uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort());
            }
        } catch (Exception e) {
            return new URIUtil(null);
        }

        return new URIUtil(rootURI);
    }

    public static URIUtil fromURI(final URI uri) {
        return new URIUtil(uri);
    }

    public static URIUtil accountFromURI(final URI uri) {
        URIUtil.rootLevelFromURI(uri);
        return URIUtil.rootLevelFromURI(uri).append("account");
    }

    public static URIUtil oauth2FromURI(final URI uri) {
        URIUtil.rootLevelFromURI(uri);
        return URIUtil.rootLevelFromURI(uri).append("oauth2");
    }

    public static URIUtil wellKnownFromURI(final URI uri) {
        URIUtil.rootLevelFromURI(uri);
        return URIUtil.rootLevelFromURI(uri).append(".well-known");
    }

    public static URIUtil idpsFromURI(final URI uri) {
        URIUtil.rootLevelFromURI(uri);
        return URIUtil.rootLevelFromURI(uri).append("sso").append("idps");
    }

    public URIUtil append(String path) {
        try {
            path = URLEncoder.encode(path, StandardCharsets.UTF_8);

            String basePath = this.uri.toString();

            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            this.uri = new java.net.URI(basePath + "/" + path);
        } catch (URISyntaxException e) {
            return this;
        }

        return this;
    }

    public URIUtil query(String query) {
        try {
            String basePath = this.uri.toString();

            if (query.startsWith("/")) {
                query = query.substring(1);
            }

            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            this.uri = new java.net.URI(basePath + query);
        } catch (URISyntaxException e) {
            return this;
        }

        return this;
    }

    public String stringValue() {
        return this.uri.toString();
    }

    public java.net.URI uriValue() {
        return this.uri;
    }
}
