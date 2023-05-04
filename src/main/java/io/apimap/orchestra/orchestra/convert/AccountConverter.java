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

import io.apimap.orchestra.orchestra.repository.mongodb.documents.Account;
import io.apimap.orchestra.orchestra.utils.URIUtil;
import io.apimap.orchestra.rest.AccountDataRestEntity;

import java.net.URI;

public class AccountConverter extends Converter<AccountDataRestEntity, Account> {
    public AccountConverter() {
        super(AccountConverter::convertToRest, AccountConverter::convertToDB);
    }

    private static Account convertToDB(AccountDataRestEntity input) {
        return new Account();
    }

    private static AccountDataRestEntity convertToRest(URI uri, Account input) {
        AccountDataRestEntity entity = new AccountDataRestEntity(input.getPublicId());
        entity.setUri(URIUtil.accountFromURI(uri).append(input.getPublicId()).stringValue());
        return entity;
    }
}
