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

package io.apimap.orchestra.orchestra.repository.nitrite;

import io.apimap.orchestra.orchestra.repository.nitrite.entities.AuthorizeRequestCache;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@Service
public class NitriteAuthorizeRequestCache extends NitriteRepository {
    public Mono<AuthorizeRequestCache> getAndDelete(String state) {
        ObjectRepository<AuthorizeRequestCache> repository = database.getRepository(AuthorizeRequestCache.class);
        AuthorizeRequestCache returnValue = repository.find((eq("state", state))).firstOrDefault();

        if (returnValue != null) {
            repository.remove(eq("state", state));
        }

        return Mono.justOrEmpty(returnValue);
    }

    public Boolean delete(String state) {
        ObjectRepository<AuthorizeRequestCache> repository = database.getRepository(AuthorizeRequestCache.class);
        return repository.remove(eq("state", state)).getAffectedCount() > 0;
    }

    public Mono<AuthorizeRequestCache> add(AuthorizeRequestCache entity) {
        ObjectRepository<AuthorizeRequestCache> repository = database.getRepository(AuthorizeRequestCache.class);
        return Mono.justOrEmpty(repository.getById(repository.insert(entity).iterator().next()));
    }
}
