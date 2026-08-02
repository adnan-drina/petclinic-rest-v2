package com.demo.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * Quarkus redesign of Spring DisableSecurityConfig (WebSecurityConfigurerAdapter).
 * When petclinic.security.enable=false (default), all requests are permitted
 * via application.properties: basic auth disabled, JDBC identity store disabled,
 * authenticated permission disabled.
 */
@ApplicationScoped
public class DisableSecurityConfig {

    private static final Logger LOG = Logger.getLogger(DisableSecurityConfig.class);

    @ConfigProperty(name = "petclinic.security.enable", defaultValue = "false")
    boolean securityEnabled;

    /**
     * Legacy: configure(HttpSecurity) — anyRequest permitAll + csrf disable.
     * Quarkus: permit-all is the default when securityEnabled=false (properties-driven).
     */
    public void configure() {
        if (!securityEnabled) {
            LOG.debug("configure: Quarkus security disabled — all requests permitted via properties");
        }
    }
}
