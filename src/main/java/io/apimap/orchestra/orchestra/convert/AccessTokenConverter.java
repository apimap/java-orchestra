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

package io.apimap.orchestra.orchestra.convert;

import io.apimap.orchestra.orchestra.repository.mongodb.documents.AccessToken;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import io.apimap.orchestra.rest.AccessTokenDataRestEntity;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class AccessTokenConverter extends Converter<AccessTokenDataRestEntity, AccessToken> {
    public AccessTokenConverter() {
        super(AccessTokenConverter::convertToRest, AccessTokenConverter::convertToDB);
    }

    private static AccessToken convertToDB(AccessTokenDataRestEntity input) {
        return new AccessToken(
            UUID.randomUUID().toString(),
            AccessToken.SYSTEM_CATEGORY.valueOf(input.getCategory().getValue()),
            Instant.now(),
            input.getValidity().getFrom().toInstant(),
            input.getValidity().getTo().toInstant(),
            input.getToken(),
            UUID.randomUUID().toString()
        );
    }

    private static AccessTokenDataRestEntity convertToRest(URI uri, AccessToken input) {
        AccessTokenDataRestEntity entity = new AccessTokenDataRestEntity(
            AccessTokenDataRestEntity.CategoryType.valueOf(input.getCategory().toString()),
            input.getSecret(),
            Date.from(input.getValidity().getFrom()),
            Date.from(input.getValidity().getTo()),
            input.getPublicId()
        );

        entity.setUri(URIUtil.fromURI(uri).append(input.getPublicId()).stringValue());

        return entity;
    }
}