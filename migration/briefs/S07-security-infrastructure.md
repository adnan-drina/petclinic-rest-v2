# S07: Security & Infrastructure

## Goal & position

Modernize security configuration and infrastructure components (Swagger/OpenAPI, JMX monitoring) to Quarkus equivalents. This story can be done independently but depends on the core application being functional from S06. This is the final modernization story that completes the migration with proper infrastructure setup.

## In scope

The exact legacy classes/files this story modernizes:

- `BasicAuthenticationConfig` - Spring Security configuration:
  ```java
  package org.springframework.samples.petclinic.security;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
  import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
  import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
  import org.springframework.security.crypto.password.PasswordEncoder;

  @Configuration
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  @ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "true")
  public class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

      @Autowired
      private DataSource dataSource;
  }
  ```

- `DisableSecurityConfig` - Security disabled configuration:
  ```java
  package org.springframework.samples.petclinic.security;

  import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

  @Configuration
  @ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "false")
  public class DisableSecurityConfig extends WebSecurityConfigurerAdapter {
  }
  ```

- `Roles` - Security roles enumeration:
  ```java
  package org.springframework.samples.petclinic.security;

  import org.springframework.stereotype.Component;

  @Component
  public class Roles {
      public final String OWNER_ADMIN = "ROLE_OWNER_ADMIN";
      public final String VET_ADMIN = "ROLE_VET_ADMIN";
      public final String ADMIN = "ROLE_ADMIN";
  }
  ```

- `ApplicationSwaggerConfig` - Swagger/OpenAPI configuration:
  ```java
  package org.springframework.samples.petclinic.util;

  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.config.BeanPostProcessor;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import springfox.documentation.builders.PathSelectors;
  import springfox.documentation.builders.RequestHandlerSelectors;
  ```

- `CallMonitoringAspect` - JMX monitoring aspect:
  ```java
  package org.springframework.samples.petclinic.util;

  import org.aspectj.lang.ProceedingJoinPoint;
  import org.aspectj.lang.annotation.Around;
  import org.aspectj.lang.annotation.Aspect;
  import org.springframework.jmx.export.annotation.ManagedAttribute;
  import org.springframework.jmx.export.annotation.ManagedOperation;
  import org.springframework.jmx.export.annotation.ManagedResource;
  import org.springframework.util.StopWatch;

  @ManagedResource("petclinic:type=CallMonitor")
  @Aspect
  public class CallMonitoringAspect {

      private boolean enabled = true;
      private int callCount = 0;
      private long accumulatedCallTime = 0;

      @ManagedAttribute
      public boolean isEnabled() {
          return enabled;
      }

      @ManagedOperation
      public void reset() {
          this.callCount = 0;
          this.accumulatedCallTime = 0;
      }
  }
  ```

## Out of scope

All core application components (models, repositories, services, controllers). These have been modernized in S02-S06.

## Class roles & target contract

For each in-scope class, its role and target contract:

- `BasicAuthenticationConfig` — REDESIGN: Spring Security → Quarkus Security (basic auth)
- `DisableSecurityConfig` — REDESIGN: Security disabled → Quarkus Security (disabled)
- `Roles` — REDESIGN: Security roles enumeration → Quarkus Security roles
- `ApplicationSwaggerConfig` — REDESIGN: Swagger → Quarkus SmallRye OpenAPI
- `CallMonitoringAspect` — REDESIGN: JMX → Micrometer metrics

**Target contract from architecture-profile §7**:
- **Quarkus Security**: Basic authentication using database-stored credentials
- **SmallRye OpenAPI**: Automatic API documentation generation via annotations
- **Micrometer Metrics**: Method call monitoring and metrics collection
- **Preserve security enablement**: Maintain petclinic.security.enable=false default

## Decided target shapes

The MAPPINGS.md rows that apply:

- **springboot-di-to-quarkus-00003**: Apply Quarkus Spring DI conversion guidance — decided target: native CDI constructor injection (NOT the spring-di extension)
- **springboot-jmx-to-quarkus-00001**: Spring JMX annotations are not supported — decided target: Micrometer-based observability
- **springboot-security-to-quarkus-00000**: Replace SpringBoot Security artifact with Quarkus 'spring-security' extension — decided target: Quarkus Security
- **springboot-webmvc-to-quarkus-00000**: Spring MVC is not supported — decided target: JAX-RS resources with SmallRye OpenAPI
- **springboot-metrics-to-quarkus-0200**: Replace Micrometer code with Microprofile Metrics code — decided target: MP Metrics annotations

## Contracts owned by this story

- **Findings**: springboot-di-to-quarkus-00003, springboot-jmx-to-quarkus-00001, springboot-security-to-quarkus-00000, springboot-webmvc-to-quarkus-00000, springboot-metrics-to-quarkus-0200
- **Preserve**: 
  - `petclinic.security.enable=false` to keep security disabled by default
  - `server.servlet.context-path=/petclinic/` to maintain API path structure
- **Behavioral pins**: 
  - Security configuration preserves exact behavior (enabled/disabled)
  - JMX monitoring replaced with equivalent Micrometer metrics
  - Swagger/OpenAPI maintains exact API documentation contract
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- Spring Security configuration replaced with Quarkus Security
- Swagger configuration replaced with SmallRye OpenAPI
- JMX monitoring replaced with Micrometer metrics
- All @PreAuthorize annotations replaced with appropriate Quarkus Security alternatives
- Application builds successfully
- Security behavior preserved (disabled by default)
- API documentation available via SmallRye OpenAPI
- Metrics available via Micrometer endpoints
