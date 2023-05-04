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

package io.apimap.orchestra.orchestra.repository.nitrite.entities;

import org.dizitart.no2.objects.Id;

import java.util.UUID;

public class AuthorizeRequestCache {

    protected String state;
    protected String callback;
    @Id
    private String id;

    public AuthorizeRequestCache() {
    }

    public AuthorizeRequestCache(String state, String callback) {
        this.id = UUID.randomUUID().toString();
        this.state = state;
        this.callback = callback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @Override
    public String toString() {
        return "AuthorizeRequestCache{" +
            "id='" + id + '\'' +
            ", state='" + state + '\'' +
            ", callback='" + callback + '\'' +
            '}';
    }
}
