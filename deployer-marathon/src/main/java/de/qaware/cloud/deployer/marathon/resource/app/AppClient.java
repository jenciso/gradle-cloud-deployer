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
package de.qaware.cloud.deployer.marathon.resource.app;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * App interface which will be used by retrofit to create an app client.
 */
interface AppClient {

    /**
     * Returns the http response for a request to the app resource with the specified id.
     *
     * @param appId The app's id.
     * @return The server's http response.
     */
    @GET("service/marathon/v2/apps/{appId}")
    Call<ResponseBody> get(@Path("appId") String appId);

    /**
     * Creates the specified app.
     *
     * @param appDescription The request body which contains the app.
     * @return The server's http response.
     */
    @POST("service/marathon/v2/apps?force=true")
    Call<ResponseBody> create(@Body RequestBody appDescription);

    /**
     * Deletes the app resource with the specified id.
     *
     * @param appId The app's id.
     * @return The server's http response.
     */
    @DELETE("service/marathon/v2/apps/{appId}?force=true")
    Call<ResponseBody> delete(@Path("appId") String appId);

    /**
     * Updates the specified app.
     *
     * @param appId          The app's id.
     * @param appDescription The request body with the updated app.
     * @return The server's http response.
     */
    @PUT("service/marathon/v2/apps/{appId}?force=true")
    Call<ResponseBody> update(@Path("appId") String appId, @Body RequestBody appDescription);
}
