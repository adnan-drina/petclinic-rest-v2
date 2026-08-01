# Modernization roadmap

## S01: Platform Foundation
- scope: pom.xml, src/main/resources/application.properties
- findings: javaee-pom-to-quarkus-00010, javaee-pom-to-quarkus-00020, javaee-pom-to-quarkus-00030, javaee-pom-to-quarkus-00040, javaee-pom-to-quarkus-00050, javaee-pom-to-quarkus-00060, springboot-parent-pom-to-quarkus-00000, springboot-plugins-to-quarkus-0000, springboot-annotations-to-quarkus-00000, springboot-properties-to-quarkus-00002, springboot-properties-to-quarkus-00003, javax-to-jakarta-dependencies-00001, javax-to-jakarta-dependencies-00003
- depends: -
- deploy: false
- done: Quarkus platform configured with proper BOM, plugins, and basic properties migrated
- rationale: Foundation work that enables all subsequent migrations; must be done first per dependency order (Maven/BOM before class changes)

## S02: Core Model Harvest
- scope: src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java, src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java, src/main/java/org/springframework/samples/petclinic/model/Person.java, src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java, src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java, src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java
- findings: javax-to-jakarta-import-00001, springboot-annotations-to-quarkus-00002
- depends: S01
- deploy: false
- done: All base entities and utilities harvested with package rename from org.springframework.samples.petclinic to com.demo, PetClinicApplication removed, Spring annotations replaced
- rationale: God nodes with highest fan-in must be characterized first per dependency order; these foundation classes are referenced by all other domain classes

## S03: Domain Model Migration
- scope: All model entities (Owner, Pet, Visit, Vet, Specialty, PetType, User, Role) and their mapper interfaces
- findings: javax-to-jakarta-import-00001
- depends: S02
- deploy: false
- done: All domain entities and mappers migrated with package rename from org.springframework.samples.petclinic to com.demo, characterizations added for god nodes
- rationale: Circular dependency group must convert together - all models depend on each other through relationships and share uniform CRUD patterns; core domain boundary per architecture profile

## S04: Repository Layer Modernization
- scope: All repository implementations (JDBC, JPA, Spring Data JPA variants) and interfaces
- findings: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003
- depends: S03
- deploy: false
- done: All repositories converted to CDI with constructor injection, Spring @Repository removed, package rename from org.springframework.samples.petclinic to com.demo complete
- rationale: Repository layer provides data access foundation needed by services; follows models in dependency order

## S05: Service Layer Modernization  
- scope: ClinicService, UserService interfaces and implementations
- findings: springboot-di-to-quarkus-00003
- depends: S04
- deploy: false
- done: Services converted to @ApplicationScoped CDI beans, business logic contracts maintained, package rename from org.springframework.samples.petclinic to com.demo complete
- rationale: Services depend on repositories; main business facade that coordinates operations per architecture profile

## S06: REST API Migration
- scope: All REST controllers (OwnerRestController, PetRestController, VisitRestController, VetRestController, SpecialtyRestController, PetTypeRestController, UserRestController, RootRestController)
- findings: springboot-di-to-quarkus-00003, springboot-web-to-quarkus-00000, oracle2openjdk-00006
- depends: S05
- deploy: true
- done: All REST controllers converted to JAX-RS with proper error handling (404 on missing, 400 on invalid), Spring @RestController removed
- rationale: API surface depends on services; final story that enables API serving and deployment milestone

## S07: Security & Infrastructure
- scope: Security configuration (BasicAuthenticationConfig, DisableSecurityConfig, Roles), utilities (ApplicationSwaggerConfig, CallMonitoringAspect)
- findings: springboot-di-to-quarkus-00003, springboot-jmx-to-quarkus-00001, springboot-security-to-quarkus-00000, springboot-webmvc-to-quarkus-00000, springboot-metrics-to-quarkus-0200
- depends: S06
- deploy: false
- done: Security converted to Quarkus Security/JDBC auth, JMX replaced with Micrometer, Swagger replaced with SmallRye OpenAPI
- rationale: Infrastructure components can be modernized independently but depend on core application being functional first

## Non-mandatory decisions

- hibernate-00005: defer (low priority sequence improvement, no functional impact)
- persistence-to-quarkus-00010: defer (not critical for migration, @PersistenceContext works fine in Quarkus)
- springboot-devservices-to-quarkus-00000: adopt (improves developer experience, worth the effort)
- springboot-cache-to-quarkus-00000: defer (no caching currently implemented, add when cache is needed)
- springboot-jpa-to-quarkus-00000: defer (Spring Data JPA works fine in Quarkus via compatibility mode)
