# Modernization roadmap

## S01: Platform Foundation
- scope: pom.xml, src/main/resources/application.properties
- findings: javaee-pom-to-quarkus-00010, javaee-pom-to-quarkus-00020, javaee-pom-to-quarkus-00030, javaee-pom-to-quarkus-00040, javaee-pom-to-quarkus-00050, javaee-pom-to-quarkus-00060, springboot-parent-pom-to-quarkus-00000, springboot-plugins-to-quarkus-0000, springboot-annotations-to-quarkus-00000, springboot-properties-to-quarkus-00002, springboot-properties-to-quarkus-00003, springboot-properties-to-quarkus-00000, springboot-properties-to-quarkus-00001, springboot-actuator-to-quarkus-0100, springboot-cache-to-quarkus-00000, springboot-jpa-to-quarkus-00000, springboot-metrics-to-quarkus-0100, springboot-di-to-quarkus-00000, springboot-di-to-quarkus-00002, spring-components-00001, spring-components-00002, localhost-jdbc-00002
- depends: -
- deploy: false
- done: Quarkus platform configured with proper BOM, plugins, and basic properties migrated
- rationale: Foundation work that enables all subsequent migrations; must be done first per dependency order (Maven/BOM before class changes)

## S02: Core Model Harvest
- scope: src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java, src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java, src/main/java/org/springframework/samples/petclinic/model/Person.java, src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java, src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java, src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java
- findings: springboot-annotations-to-quarkus-00002
- depends: S01
- deploy: false
- done: All base entities and utilities harvested with package rename from org.springframework.samples.petclinic to com.demo, PetClinicApplication removed, Spring annotations replaced
- rationale: God nodes with highest fan-in must be characterized first per dependency order; these foundation classes are referenced by all other domain classes

## S03: Domain Model Migration
- scope: src/main/java/org/springframework/samples/petclinic/model/Owner.java, src/main/java/org/springframework/samples/petclinic/model/Pet.java, src/main/java/org/springframework/samples/petclinic/model/Visit.java, src/main/java/org/springframework/samples/petclinic/model/Vet.java, src/main/java/org/springframework/samples/petclinic/model/Specialty.java, src/main/java/org/springframework/samples/petclinic/model/PetType.java, src/main/java/org/springframework/samples/petclinic/model/User.java, src/main/java/org/springframework/samples/petclinic/model/Role.java, src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java, src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java
- findings: -
- depends: S02
- deploy: false
- done: All domain entities and mappers migrated with package rename from org.springframework.samples.petclinic to com.demo, characterizations added for god nodes
- rationale: Circular dependency group must convert together - all models depend on each other through relationships and share uniform CRUD patterns; core domain boundary per architecture profile

## S04: Repository Layer Modernization
- scope: src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetTypeRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcSpecialtyRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcUserRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVetRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVisitRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetTypeRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaSpecialtyRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaUserRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVetRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVisitRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetTypeRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataSpecialtyRepositoryImpl.java, src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVisitRepositoryImpl.java
- findings: transaction-to-quarkus-00003
- depends: S03
- deploy: false
- done: All repositories converted to CDI with constructor injection, Spring @Repository removed, package rename from org.springframework.samples.petclinic to com.demo complete
- rationale: Repository layer provides data access foundation needed by services; follows models in dependency order

## S05: Service Layer Modernization  
- scope: src/main/java/org/springframework/samples/petclinic/service/ClinicService.java, src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java, src/main/java/org/springframework/samples/petclinic/service/UserService.java, src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java
- findings: springboot-di-to-quarkus-00003
- depends: S04
- deploy: false
- done: Services converted to @ApplicationScoped CDI beans, business logic contracts maintained, package rename from org.springframework.samples.petclinic to com.demo complete
- rationale: Services depend on repositories; main business facade that coordinates operations per architecture profile

## S06: REST API Migration
- scope: src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java, src/main/java/org/springframework/samples/petclinic/rest/PetRestController.java, src/main/java/org/springframework/samples/petclinic/rest/VisitRestController.java, src/main/java/org/springframework/samples/petclinic/rest/VetRestController.java, src/main/java/org/springframework/samples/petclinic/rest/SpecialtyRestController.java, src/main/java/org/springframework/samples/petclinic/rest/PetTypeRestController.java, src/main/java/org/springframework/samples/petclinic/rest/UserRestController.java, src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java
- findings: springboot-web-to-quarkus-00000, oracle2openjdk-00006
- depends: S05
- deploy: true
- done: All REST controllers converted to JAX-RS with proper error handling (404 on missing, 400 on invalid), Spring @RestController removed
- rationale: API surface depends on services; final story that enables API serving and deployment milestone

## S07: Security & Infrastructure
- scope: src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java, src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java, src/main/java/org/springframework/samples/petclinic/security/Roles.java, src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java, src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java
- findings: springboot-jmx-to-quarkus-00001, springboot-security-to-quarkus-00000, springboot-webmvc-to-quarkus-00000, springboot-metrics-to-quarkus-0200
- depends: S06
- deploy: true
- done: Security converted to Quarkus Security/JDBC auth, JMX replaced with Micrometer, Swagger replaced with SmallRye OpenAPI
- rationale: Infrastructure components can be modernized independently but depend on core application being functional first

## Non-mandatory decisions

- hibernate-00005: defer (low priority sequence improvement, no functional impact)
- persistence-to-quarkus-00010: defer (not critical for migration, @PersistenceContext works fine in Quarkus)
- springboot-devservices-to-quarkus-00000: adopt (improves developer experience, worth the effort)
- springboot-cache-to-quarkus-00000: defer (no caching currently implemented, add when cache is needed)
- springboot-jpa-to-quarkus-00000: defer (Spring Data JPA works fine in Quarkus via compatibility mode)
