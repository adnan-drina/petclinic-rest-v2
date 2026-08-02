package com.demo.util;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * SmallRye OpenAPI configuration for the Petclinic REST API.
 *
 * Replaces the legacy Springfox ApplicationSwaggerConfig which used
 * @EnableSwagger2, Docket, and a BeanPostProcessor workaround for
 * Spring Boot 2.6 path matching.  SmallRye OpenAPI auto-discovers
 * JAX-RS resources; the API metadata (title, contact, license) is
 * configured via mp.openapi.* properties in application.properties.
 *
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
public class OpenApiConfig {

    // OpenAPI metadata is configured declaratively in application.properties
    // under the mp.openapi.info.* namespace.  This class exists as a
    // programmatic extension point should future requirements need
    // custom OpenAPI filters, security schemes, or schema modifications.
}
