package com.demo.security;

import io.quarkus.security.spi.runtime.AuthorizationController;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * When petclinic.security.enable=false (default), disable JAX-RS authorization
 * so @RolesAllowed does not 403 anonymous callers. When true, enforce roles.
 */
@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
@ApplicationScoped
public class OptionalAuthorizationController extends AuthorizationController {

    @ConfigProperty(name = "petclinic.security.enable", defaultValue = "false")
    boolean securityEnabled;

    @Override
    public boolean isAuthorizationEnabled() {
        return securityEnabled;
    }
}
