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

package io.apimap.orchestra.orchestra.repository.mongodb;

import io.apimap.orchestra.orchestra.repository.mongodb.documents.Account;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Repository
@ConditionalOnBean(io.apimap.orchestra.orchestra.configuration.MongoConfiguration.class)
public class MongoDBAccountRepository extends MongoDBRepository {
    public MongoDBAccountRepository(ReactiveMongoTemplate template) {
        super(template);
    }

    public Flux<Account> all() {
        return template
            .findAll(Account.class);
    }

    public Mono<Account> get(final String ssoAssociationId) {
        final Query query = new Query().addCriteria(Criteria.where("ssoAssociationId").is(ssoAssociationId));
        return template.findOne(query, Account.class);
    }

    public Mono<Account> getById(final String accountId) {
        final Query query = new Query().addCriteria(Criteria.where("id").is(accountId));
        return template.findOne(query, Account.class);
    }

    public Mono<Account> getByPublicId(final String publicId) {
        final Query query = new Query().addCriteria(Criteria.where("publicId").is(publicId));
        return template.findOne(query, Account.class);
    }

    public Mono<Account> add(final Account entity) {
        entity.setCreated(Instant.now());
        entity.setPublicId(UUID.randomUUID().toString());

        return getByPublicId(entity.getPublicId())
            .doOnNext(api -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The Account '" + entity.getPublicId() + "' already exists");
            })
            .switchIfEmpty(Mono.defer(() -> template.insert(entity)));
    }

    public Mono<Boolean> deleteByPublicId(final String publicId) {
        final Query query = new Query().addCriteria(Criteria.where("publicId").is(publicId));
        return template
            .remove(query, Account.class)
            .flatMap(result -> Mono.just((result.getDeletedCount() > 0)));
    }
}
