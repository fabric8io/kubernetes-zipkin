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

package io.fabric8.zipkin.examples.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class HelloController implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private int port;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Tracer tracer;

    @Autowired
    private Random random;

    @RequestMapping("/hello")
    public String hello(@RequestParam("name") String name) throws InterruptedException {
        Thread.sleep(this.random.nextInt(1000));
        String greeting = this.restTemplate.getForObject("http://localhost:" + this.port + "/greeting", String.class);
        return greeting + " " + name + "!";
    }


    @RequestMapping("/greeting")
    public String greeting() throws InterruptedException {
        int millis = this.random.nextInt(1000);
        Thread.sleep(millis);
        this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
        return "Hello";
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.port = event.getEmbeddedServletContainer().getPort();
    }

}
