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

package io.apimap.orchestra.orchestra.router.log;

import io.apimap.orchestra.orchestra.router.filter.XRequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleHttpExchangeRepository extends InMemoryHttpExchangeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpExchangeRepository.class);

    @Override
    public void add(HttpExchange trace) {
        LOGGER.info(getInfoLogMessage(trace));
        super.add(trace);
    }

    private String getInfoLogMessage(HttpExchange trace) {
        return String.format(
            "%s, %s, TIMETAKEN=%d ms",
            getRequest(trace.getRequest()),
            getResponse(trace.getResponse()),
            trace.getTimeTaken().toMillis()
        );
    }

    private String getResponse(HttpExchange.Response response) {
        if (response != null) {
            return String.format(
                "STATUS=%s",
                response.getStatus()
            );
        }

        return "";
    }

    private String getRequest(HttpExchange.Request request) {
        if(request != null){
            String requestId = request.getHeaders().get(XRequestIdFilter.REQUEST_ID_HEADER_NAME) != null
                ? request.getHeaders().get(XRequestIdFilter.REQUEST_ID_HEADER_NAME).get(0)
                : "<unknown>";

            return String.format(
                "REQUEST-ID=%s, METHOD=%s, URI=%s",
                requestId,
                request.getMethod(),
                request.getUri()
            );
        }

        return "";
    }
}
