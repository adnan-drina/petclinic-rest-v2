package com.demo.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * Quarkus redesign of Spring BasicAuthenticationConfig (WebSecurityConfigurerAdapter).
 * HTTP basic + JDBC auth are wired in application.properties when
 * petclinic.security.enable=true. Method names preserved for O-REDESIGNSIG.
 */
@ApplicationScoped
public class BasicAuthenticationConfig {

    private static final Logger LOG = Logger.getLogger(BasicAuthenticationConfig.class);

    @ConfigProperty(name = "petclinic.security.enable", defaultValue = "false")
    boolean securityEnabled;

    /**
     * Legacy: configure(HttpSecurity) — anyRequest authenticated + httpBasic + csrf off.
     * Quarkus: quarkus.http.auth.basic + permission.authenticated when securityEnabled.
     */
    public void configure() {
        if (securityEnabled) {
            LOG.debug("configure: Quarkus basic auth enforced via properties");
        }
    }

    /**
     * Legacy: configureGlobal(AuthenticationManagerBuilder) JDBC users/roles queries.
     * Quarkus: quarkus.security.jdbc.principal-query / roles-query.
     */
    public void configureGlobal() {
        if (securityEnabled) {
            LOG.debug("configureGlobal: JDBC identity store via quarkus.security.jdbc.*");
        }
    }
}
