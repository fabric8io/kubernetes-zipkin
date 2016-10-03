/*
 * Copyright (C) 2016 to origin authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fabric8.zipkin.starter.minimal;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import io.fabric8.annotations.ServiceName;
import io.fabric8.arquillian.kubernetes.Session;
import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.utils.URLUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ZipkinMinimalKubernetesTest {

    @ArquillianResource
    Session session;

    @ServiceName("zipkin")
    @ArquillianResource
    Service service;

    @Test
    @RunAsClient
    public void testConnectionToZipkinQuery() throws Exception {
        String url = URLUtils.join(KubernetesHelper.getServiceURL(service), "/api/v1/services");
        OkHttpClient httpClient = new OkHttpClient();
        try {
            Request request = new Request.Builder().get().url(url).build();
            Response response = httpClient.newCall(request).execute();
            Assert.assertNotNull(response);
            Assert.assertEquals(200, response.code());
        } finally {
            close(httpClient);
        }
    }

    private void close(OkHttpClient httpClient) {
        if (httpClient.connectionPool() != null) {
            httpClient.connectionPool().evictAll();
        }
        if (httpClient.dispatcher() != null &&
                httpClient.dispatcher().executorService() != null &&
                !httpClient.dispatcher().executorService().isShutdown()
                ) {
            httpClient.dispatcher().executorService().shutdown();
        }
    }
}
