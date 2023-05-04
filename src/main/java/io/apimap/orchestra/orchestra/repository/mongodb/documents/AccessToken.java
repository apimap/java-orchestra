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

package io.apimap.orchestra.orchestra.repository.mongodb.documents;

import org.springframework.data.annotation.Id;

import java.time.Instant;

public class AccessToken {

    protected String publicId;
    protected Instant created;
    protected String accountPublicId;
    protected SYSTEM_CATEGORY category;
    protected Validity validity;
    protected String secret;

    @Id
    private String id;

    public AccessToken() {
    }

    public AccessToken(String id,
                       SYSTEM_CATEGORY category,
                       Instant created,
                       Instant validFrom,
                       Instant validTo,
                       String secret,
                       final String publicId) {
        this.id = id;
        this.category = category;
        this.created = created;
        this.validity = new Validity(validFrom, validTo);
        this.secret = secret;
        this.publicId = publicId;
    }

    public AccessToken(final String id,
                       final SYSTEM_CATEGORY category,
                       final Instant created,
                       final Validity validity,
                       final String secret,
                       final String accountPublicId,
                       final String publicId) {
        this.id = id;
        this.accountPublicId = accountPublicId;
        this.category = category;
        this.created = created;
        this.validity = validity;
        this.secret = secret;
        this.publicId = publicId;
    }

    public AccessToken(final String id,
                       final SYSTEM_CATEGORY category,
                       final Instant created,
                       final Instant validFrom,
                       final Instant validTo,
                       final String secret,
                       final String accountPublicId,
                       final String publicId) {
        this.id = id;
        this.accountPublicId = accountPublicId;
        this.category = category;
        this.created = created;
        this.validity = new Validity(validFrom, validTo);
        this.secret = secret;
        this.publicId = publicId;
    }

    public String getAccountPublicId() {
        return accountPublicId;
    }

    public void setAccountPublicId(String accountPublicId) {
        this.accountPublicId = accountPublicId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SYSTEM_CATEGORY getCategory() {
        if (category == null) return SYSTEM_CATEGORY.UNKNOWN;
        return category;
    }

    public void setCategory(SYSTEM_CATEGORY category) {
        this.category = category;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getaccountId() {
        return accountPublicId;
    }

    public void setaccountId(String accountId) {
        this.accountPublicId = accountId;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
            "id='" + id + '\'' +
            ", publicId='" + publicId + '\'' +
            ", created=" + created +
            ", accountPublicId='" + accountPublicId + '\'' +
            ", category=" + category +
            ", validity=" + validity +
            ", secret='" + secret + '\'' +
            '}';
    }

    public static enum SYSTEM_CATEGORY {
        CLI,
        API,
        UNKNOWN
    }

    public static class Validity {
        final protected Instant from;
        final protected Instant to;

        public Validity(Instant from, Instant to) {
            this.from = from;
            this.to = to;
        }

        public Instant getFrom() {
            return from;
        }

        public Instant getTo() {
            return to;
        }

        @Override
        public String toString() {
            return "Validity{" +
                "from=" + from +
                ", to=" + to +
                '}';
        }
    }
}
