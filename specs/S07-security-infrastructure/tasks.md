# S07: Security & Infrastructure — Tasks

## UI / frontend / web surface
**Out of scope**: legacy UI, frontend, and web surface. This story modernizes security/OpenAPI/metrics for the REST API only.

## T-001: Add Quarkus Security and OpenAPI dependencies
**Shape**: modify
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Replace Spring Security artifacts with Quarkus Security (+ JDBC identity store support) and ensure SmallRye OpenAPI is on the classpath.

**Target design**: → `pom.xml`

**Actions**:
1. Add `quarkus-security` and JDBC auth support dependency suitable for basic auth against the users/authorities tables
2. Add `quarkus-smallrye-openapi` if missing
3. Do not add the spring-security compatibility extension

**Acceptance**: `pom.xml` declares Quarkus Security + OpenAPI deps; `mvn -q compile` resolves them

**Owns**:
- pom.xml

---

## T-002: Redesign BasicAuthenticationConfig to Quarkus Security basic auth
**Shape**: create
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Replace Spring `WebSecurityConfigurerAdapter` basic-auth config with Quarkus Security JDBC basic authentication enabled only when `petclinic.security.enable=true`.

**Target design**: → `src/main/java/com/demo/security/BasicAuthenticationConfig.java`

Also update `src/main/resources/application.properties` for Quarkus Security JDBC queries / basic auth, gated by `petclinic.security.enable`.

**Actions**:
1. Remove Spring Security HTTP/CSRF adapter APIs
2. Configure HTTP basic + JDBC identity store equivalent to legacy users/authorities SQL
3. Keep conditional enablement on `petclinic.security.enable=true`
4. Preserve `petclinic.security.enable` property name and default semantics

**Acceptance**: Basic auth path compiles; property `petclinic.security.enable=true` selects authenticated mode

**Owns**:
- src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java
- src/main/java/com/demo/security/BasicAuthenticationConfig.java
- src/main/resources/application.properties

---

## T-003: Redesign DisableSecurityConfig for security-disabled default
**Shape**: create
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Replace Spring permit-all adapter with Quarkus configuration/policy that permits all requests when `petclinic.security.enable=false` (the default).

**Target design**: → `src/main/java/com/demo/security/DisableSecurityConfig.java`

**Actions**:
1. Package rename to `com.demo.security`
2. Encode permit-all / authn disabled when `petclinic.security.enable=false`
3. Preserve `server.servlet.context-path` via existing `quarkus.http.root-path=/petclinic` (do not regress root-path)

**Acceptance**: With default properties, API calls succeed without credentials

**Owns**:
- src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java
- src/main/java/com/demo/security/DisableSecurityConfig.java
- src/main/resources/application.properties

---

## T-004: Redesign Roles to Quarkus security role constants
**Shape**: create
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Harvest role name constants from staging `Roles` into `com.demo.security.Roles` as an `@ApplicationScoped` CDI bean (or final constants) usable by `@RolesAllowed`.

**Target design**: → `src/main/java/com/demo/security/Roles.java`

**Actions**:
1. Package rename `org.springframework.samples.petclinic.security` → `com.demo.security`
2. Remove Spring `@Component`; use CDI `@ApplicationScoped` if a bean is required
3. Preserve literal role strings `ROLE_OWNER_ADMIN`, `ROLE_VET_ADMIN`, `ROLE_ADMIN`

**Acceptance**: `Roles.java` compiles under `com.demo.security`

**Owns**:
- src/main/java/org/springframework/samples/petclinic/security/Roles.java
- src/main/java/com/demo/security/Roles.java

---

## T-005: Replace @PreAuthorize with @RolesAllowed on non-vet REST resources
**Shape**: modify
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Apply `jakarta.annotation.security.RolesAllowed` on migrated JAX-RS controllers (except Vet — owned by T-009) to restore method-level role checks when security is enabled. Preserve REST error contract: missing entity → **404**, invalid input → **400**/`@Valid`, persistence failures → **503** via existing `ExceptionMapper`.

**Target design**: → `src/main/java/com/demo/rest/OwnerRestController.java`

Also update Pet/Visit/Specialty/PetType/User REST controllers under `src/main/java/com/demo/rest/` that still lack role annotations after S06 removed Spring `@PreAuthorize`.

**Actions**:
1. Add `@RolesAllowed` matching legacy role names (without Spring SpEL `@roles` bean refs)
2. Keep endpoints callable when `petclinic.security.enable=false`
3. Do not reintroduce Spring Security imports
4. Do not weaken 404/400/503/`ExceptionMapper`/`@Valid` contracts from S06

**Acceptance**: Controllers compile with Jakarta security annotations; no `org.springframework.security` imports

**Owns**:
- src/main/java/com/demo/rest/OwnerRestController.java
- src/main/java/com/demo/rest/PetRestController.java
- src/main/java/com/demo/rest/VisitRestController.java
- src/main/java/com/demo/rest/SpecialtyRestController.java
- src/main/java/com/demo/rest/PetTypeRestController.java
- src/main/java/com/demo/rest/UserRestController.java

---

## T-006: Redesign ApplicationSwaggerConfig to SmallRye OpenAPI
**Shape**: create
**Class**: infer
**Findings**: springboot-di-to-quarkus-00002, springboot-webmvc-to-quarkus-00000

Remove Springfox Docket / WebMvc `BeanPostProcessor` coupling. Provide Quarkus SmallRye OpenAPI configuration (properties and/or a small CDI OpenAPI filter/config class).

**Target design**: → `src/main/java/com/demo/util/OpenApiConfig.java`

**Actions**:
1. Do not keep Springfox `EnableSwagger2` / `RequestMappingInfoHandlerMapping` hacks
2. Configure OpenAPI title/contact equivalent to legacy `ApiInfo` via SmallRye OpenAPI
3. Package under `com.demo.util` (full rename from `org.springframework.samples.petclinic.util`)
4. Delete or leave unreferenced any Springfox-only types

**Acceptance**: OpenAPI available via Quarkus `/q/openapi` (or documented SmallRye path); no Springfox dependencies required to compile

**Owns**:
- src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java
- src/main/java/com/demo/util/OpenApiConfig.java
- pom.xml

**Absorbs**:
- src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java

---

## T-007: Redesign CallMonitoringAspect from JMX to Micrometer
**Shape**: create
**Class**: infer
**Findings**: springboot-jmx-to-quarkus-00001

Replace `@ManagedResource` / `@ManagedAttribute` / `@ManagedOperation` JMX aspect with Micrometer (or MP Metrics) call timing around repository/service join points.

**Target design**: → `src/main/java/com/demo/util/CallMonitoringAspect.java`

**Actions**:
1. Remove Spring JMX export annotations
2. Use Micrometer `Timer` / `@Timed` (or SmallRye MP Metrics) for call count and duration
3. Keep `@ApplicationScoped` (or equivalent) CDI lifecycle; Interceptor/Aspect style acceptable if compile-clean on Quarkus
4. Package rename to `com.demo.util`

**Acceptance**: Aspect/interceptor compiles without Spring JMX APIs; metrics scrapeable under `/q/metrics`

**Owns**:
- src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java
- src/main/java/com/demo/util/CallMonitoringAspect.java

---

## T-008: Align metrics dependencies for Micrometer / MP Metrics
**Shape**: modify
**Class**: infer
**Findings**: springboot-metrics-to-quarkus-0200

Ensure `pom.xml` uses Quarkus Micrometer or SmallRye Metrics (already preferred) instead of Spring Boot Actuator metrics wiring.

**Target design**: → `pom.xml`

**Actions**:
1. Keep/confirm `quarkus-smallrye-metrics` or add `quarkus-micrometer-registry-prometheus` as required by T-007
2. Remove any Spring Boot metrics-only leftovers if present

**Acceptance**: Metrics dependency resolves; app compiles

**Owns**:
- pom.xml

---

## T-009: Wire RolesAllowed and deploy acceptance on VetRestController
**Shape**: modify
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Deploy story (`deploy=true`): keep the full literal acceptance path `/petclinic/api/vets` served by `VetRestController` `@Path("/api/vets")` under `quarkus.http.root-path=/petclinic`. Add `@RolesAllowed` for VET_ADMIN when security is enabled. Security-disabled default must not break 200 + vet array. Missing vet → **404**; invalid input → **400**/`@Valid`; persistence failures → **503** via existing `ExceptionMapper`.

**Target design**: → `src/main/java/com/demo/rest/VetRestController.java`

**Actions**:
1. Confirm `@Path("/api/vets")` (not bare `/vets`) remains
2. Add Jakarta `@RolesAllowed` matching legacy VET_ADMIN checks
3. Do not introduce MinimalAcceptanceEndpoint or status-map placeholders
4. Adjust `src/test/java/com/demo/rest/RestApiAcceptanceTest.java` if security filters change the unauthenticated path

**Acceptance**: Unauthenticated GET `/petclinic/api/vets` returns 200 with vet `_array` when `petclinic.security.enable=false`

**Owns**:
- src/main/java/com/demo/rest/VetRestController.java
- src/test/java/com/demo/rest/RestApiAcceptanceTest.java

---

## T-010: Security and OpenAPI characterization tests
**Shape**: create
**Class**: infer
**Findings**: springboot-security-to-quarkus-00000

Add tests under `src/test/java/com/demo/security/` covering default-disabled security and basic auth enablement smoke (no placeholder status endpoint).

**Target design**: → `src/test/java/com/demo/security/SecurityConfigTest.java`

**Actions**:
1. Assert default `petclinic.security.enable=false` permits `/petclinic/api/vets`
2. Optional: with test profile `petclinic.security.enable=true`, unauthenticated call is 401
3. Pin OpenAPI document contains vet path when SmallRye is active

**Acceptance**: Tests compile and pass under `%test`

**Owns**:
- src/test/java/com/demo/security/SecurityConfigTest.java
