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

package io.apimap.orchestra.orchestra.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

public class OpenAPIConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes(
                "token",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("token")
            ))
            .components(new Components().addSchemas(
                "Relationships",
                new MapSchema().addProperty("key", new StringSchema()).addProperty("value", new ObjectSchema())
            ))
            .components(new Components().addSchemas(
                "Links",
                new MapSchema().addProperty("key", new StringSchema()).addProperty("url", new StringSchema())
            ));
    }
}
