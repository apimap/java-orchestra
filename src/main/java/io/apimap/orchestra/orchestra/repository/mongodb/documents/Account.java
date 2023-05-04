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
import java.util.UUID;

public class Account {

    protected String publicId;
    protected Instant created;
    protected String ssoAssociationId;
    protected String providerId;
    @Id
    private String id;

    public Account() {
    }

    public Account(final String ssoAssociationId,
                   final String providerId) {
        this.publicId = UUID.randomUUID().toString();
        this.created = Instant.now();
        this.ssoAssociationId = ssoAssociationId;
        this.providerId = providerId;
        this.id = ssoAssociationId + "#" + providerId;
    }

    public Account(final String ssoAssociationId,
                   final String publicId,
                   final Instant created,
                   final String providerId) {
        this.publicId = publicId;
        this.created = created;
        this.ssoAssociationId = ssoAssociationId;
        this.providerId = providerId;
        this.id = ssoAssociationId + "#" + providerId;
    }

    public String getId() {
        return id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getSsoAssociationId() {
        return ssoAssociationId;
    }

    public void setSsoAssociationId(String ssoAssociationId) {
        this.ssoAssociationId = ssoAssociationId;
        this.id = ssoAssociationId + "#" + providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
        this.id = ssoAssociationId + "#" + providerId;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id='" + id + '\'' +
            ", publicId='" + publicId + '\'' +
            ", created=" + created +
            ", ssoAssociationId='" + ssoAssociationId + '\'' +
            ", providerId='" + providerId + '\'' +
            '}';
    }
}
