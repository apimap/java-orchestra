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

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Code pattern from :
 * https://java-design-patterns.com/patterns/converter/#explanation
 */
public class Converter<REST, DB> {

    private final BiFunction<URI, DB, REST> toRest;
    private final Function<REST, DB> toDatabase;

    public Converter(final BiFunction<URI, DB, REST> toRest, final Function<REST, DB> toDatabase) {
        this.toRest = toRest;
        this.toDatabase = toDatabase;
    }

    public final REST convertToRestEntity(final URI uri, final DB dto) {
        return toRest.apply(uri, dto);
    }

    public final DB convertToDatabaseEntity(final REST entity) {
        return toDatabase.apply(entity);
    }

    public final List<REST> convertToRestEntities(final URI uri, final Collection<DB> dtos) {
        return dtos.stream().map(e -> convertToRestEntity(uri, e)).collect(Collectors.toList());
    }

    public final List<DB> convertToDatabaseEntities(final Collection<REST> entities) {
        return entities.stream().map(this::convertToDatabaseEntity).collect(Collectors.toList());
    }
}