# S07: Security & Infrastructure — Plan

## Package rename
`org.springframework.samples.petclinic.X` → `com.demo.X` (never invent `com.demo.coolstore`).

## Sequence
1. Add Quarkus Security (+ JDBC) and OpenAPI/metrics dependencies in `pom.xml`.
2. Redesign `Roles`, `BasicAuthenticationConfig`, `DisableSecurityConfig` under `com.demo.security` gated by `petclinic.security.enable`.
3. Replace remaining Spring `@PreAuthorize` usage on JAX-RS controllers with `@RolesAllowed`.
4. Remove Springfox `ApplicationSwaggerConfig`; configure SmallRye OpenAPI.
5. Redesign `CallMonitoringAspect` from JMX to Micrometer.
6. Keep deploy acceptance `/petclinic/api/vets` on `VetRestController` `@Path("/api/vets")` with 404/400/503 via existing ExceptionMapper.
7. Characterization tests for security-disabled default and OpenAPI/metrics wiring.

## Preserve
- `petclinic.security.enable` (default false)
- `server.servlet.context-path` (mapped to `quarkus.http.root-path`)
