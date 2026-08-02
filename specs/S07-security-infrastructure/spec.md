# S07: Security & Infrastructure — Specification

## Overview
Modernize Spring Security, Springfox Swagger, and JMX call-monitoring into Quarkus Security (JDBC basic auth with `petclinic.security.enable` gate), SmallRye OpenAPI, and Micrometer/MP Metrics. This is the final deploy story (`deploy=true`); ship must keep serving acceptance path `/petclinic/api/vets` via existing JAX-RS `@Path` resources.

## In scope (roadmap)
- `BasicAuthenticationConfig`, `DisableSecurityConfig`, `Roles`
- `ApplicationSwaggerConfig`, `CallMonitoringAspect`
- Findings: `springboot-di-to-quarkus-00002`, `springboot-jmx-to-quarkus-00001`, `springboot-security-to-quarkus-00000`, `springboot-webmvc-to-quarkus-00000`, `springboot-metrics-to-quarkus-0200`

## Decided shapes
- Spring Security → Quarkus Security + JDBC identity store (not spring-security extension)
- Security default remains disabled via `petclinic.security.enable=false`
- Springfox → Quarkus SmallRye OpenAPI (remove Docket/BeanPostProcessor WebMvc coupling)
- JMX `@ManagedResource` aspect → Micrometer timers / MP Metrics annotations
- `@PreAuthorize` → `@RolesAllowed` (or equivalent Quarkus annotation) on REST resources when security is enabled
- Preserve `server.servlet.context-path` via existing `quarkus.http.root-path=/petclinic`

## Out of scope
- Legacy UI / frontend / web surface (REST-only petclinic; no UI migration)
- Re-harvesting domain model / repositories / services (prior stories)
- Inventing MinimalAcceptanceEndpoint or status-map placeholders

## Acceptance contract
- Path: `/petclinic/api/vets` (migration.yaml)
- Must remain a real `@Path` resource (`VetRestController`) returning 200 + vet array when security is disabled
