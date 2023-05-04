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

import io.apimap.orchestra.orchestra.repository.mongodb.documents.AccessToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Repository
@ConditionalOnBean(io.apimap.orchestra.orchestra.configuration.MongoConfiguration.class)
public class MongoDBAccessTokenRepository extends MongoDBRepository {
    public MongoDBAccessTokenRepository(final ReactiveMongoTemplate template) {
        super(template);
    }

    public Flux<AccessToken> allBy(final String accountPublicId) {
        final Query query = new Query().addCriteria(Criteria.where("accountPublicId").is(accountPublicId));

        return template
            .find(query, AccessToken.class);
    }

    public Mono<AccessToken> getByPublicId(final String publicId) {
        final Query query = new Query().addCriteria(Criteria.where("publicId").is(publicId));
        return template.findOne(query, AccessToken.class);
    }

    public Mono<AccessToken> getBySecret(final String accountPublicId,
                                         final String secret){
        final Query query = new Query().addCriteria(Criteria.where("accountPublicId").is(accountPublicId));
        query.addCriteria(Criteria.where("secret").is(secret));
        return template.findOne(query, AccessToken.class);
    }

    public Mono<AccessToken> getBy(final String accountPublicId,
                                   final String publicId) {
        final Query query = new Query().addCriteria(Criteria.where("accountPublicId").is(accountPublicId));
        query.addCriteria(Criteria.where("publicId").is(publicId));
        return template.findOne(query, AccessToken.class);
    }

    public Mono<AccessToken> add(final AccessToken entity) {
        entity.setCreated(Instant.now());
        entity.setPublicId(UUID.randomUUID().toString());

        return template.insert(entity);
    }

    public Mono<Boolean> deleteBy(final String publicId,
                                  final String accountPublicId) {
        final Query query = new Query().addCriteria(Criteria.where("publicId").is(publicId));
        query.addCriteria(Criteria.where("accountPublicId").is(accountPublicId));

        return template
            .remove(query, AccessToken.class)
            .flatMap(result -> Mono.just((result.getDeletedCount() > 0)));
    }
}
