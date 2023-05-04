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

package io.apimap.orchestra.orchestra;

import io.apimap.orchestra.orchestra.configuration.ApimapConfiguration;
import io.apimap.orchestra.orchestra.configuration.MongoConfiguration;
import io.apimap.orchestra.orchestra.configuration.OIDCConfiguration;
import io.apimap.orchestra.orchestra.configuration.ZeroconfConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition(
    info = @Info(
        title = "Apimap.io",
        description = "Apimap.io is a centralized registry of our APIs. This API is built to comply with the JSON:API standard version 1.1 (https://jsonapi.org/)",
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
    ),
    tags = {
        @Tag(name = "ACCOUNT", description = "Endpoints managing Account related resources"),
        @Tag(name = "OAUTH", description = "Endpoints managing Oauth related resources"),
        @Tag(name = "SSO", description = "Endpoints managing SSO related resources"),
        @Tag(name = "WELLKNOWN", description = "Endpoints managing .well-knonw related resources"),
        @Tag(name = "ZEROCONF", description = "Endpoints managing configuration related resources")
    }
)
@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    MongoReactiveAutoConfiguration.class
})
@EnableConfigurationProperties({
    ApimapConfiguration.class,
    OIDCConfiguration.class,
    MongoConfiguration.class,
    ZeroconfConfiguration.class
})
public class OrchestraApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrchestraApplication.class, args);
    }
}
