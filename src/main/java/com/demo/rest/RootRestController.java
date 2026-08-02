/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.rest;

import java.net.URI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * @author Vitaliy Fedoriv
 */
@RequestScoped
@Path("/")
public class RootRestController {

    @GET
    public Response redirectToSwagger() {
        // Staging: response.sendRedirect(servletContextPath + "/swagger-ui/index.html")
        // quarkus.http.root-path=/petclinic
        return Response.temporaryRedirect(URI.create("/petclinic/swagger-ui/index.html")).build();
    }
}
