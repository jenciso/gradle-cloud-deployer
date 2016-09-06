/*
 * Copyright 2016 QAware GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.qaware.cloud.deployer.marathon.config.cloud;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface TokenClient {

    /**
     * Tries to login using the specified token.
     *
     * @param token The request body which contains the app.
     * @return The server's http response.
     */
    @POST("/acs/api/v1/auth/login")
    Call<TokenRequest> login(@Body TokenRequest token);
}
