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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Optional;

@ConfigurationProperties(prefix = "apimap.zeroconf")
public class ZeroconfConfiguration {
    protected HashMap<String, String> endpoints;

    public ZeroconfConfiguration(HashMap<String, String> endpoints) {
        this.endpoints = new HashMap<>(endpoints);
    }

    public Optional<String> getEndpoint(String key){
        if(this.endpoints.containsKey(key)){
            return Optional.of(this.endpoints.get(key));
        }

        return Optional.empty();
    }

    public HashMap<String, String> getEndpoints(){
        return new HashMap<>(endpoints);
    }
}