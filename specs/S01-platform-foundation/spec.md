# S01 Platform Foundation - Specification

## Observed Legacy Behavior

This story establishes the Quarkus platform foundation by modernizing the Maven POM configuration and application properties. No application code is modified in this story.

## Legacy Configuration Analysis

### Maven POM Structure
The legacy `pom.xml` (`/projects/legacy/pom.xml:1-338`) defines the Spring Boot build configuration:

**Spring Boot Parent and Dependencies:**
- Parent POM: `org.springframework.boot:spring-boot-starter-parent:2.6.2` (`/projects/legacy/pom.xml:13-18`)
- Core Spring Boot starters: actuator, aop, cache, data-jpa, jdbc, web, security, validation (`/projects/legacy/pom.xml:39-70`)
- Database drivers: hsqldb, mysql-connector-java, postgresql (`/projects/legacy/pom.xml:72-85`)
- Spring Data JDBC: `spring-data-jdbc-core:1.2.1.RELEASE` (`/projects/legacy/pom.xml:88-97`)
- Testing: spring-boot-starter-test, spring-security-test (`/projects/legacy/pom.xml:100-108`)
- Jackson dependencies for JSON processing (`/projects/legacy/pom.xml:110-137`)
- MapStruct for DTO mapping: `mapstruct:1.4.1.Final` with processor (`/projects/legacy/pom.xml:139-147`)
- JAXB API: `javax.xml.bind:jaxb-api:2.3.0` (`/projects/legacy/pom.xml:155-159`)

**Spring Boot Plugins:**
- `spring-boot-maven-plugin` with build-info goal (`/projects/legacy/pom.xml:164-184`)
- Jacoco for test coverage (`/projects/legacy/pom.xml:186-236`)
- Jib for Docker containerization (`/projects/legacy/pom.xml:238-251`)
- OpenAPI generator for API documentation (`/projects/legacy/pom.xml:253-292`)

### Application Properties Configuration
The legacy `application.properties` (`/projects/legacy/src/main/resources/application.properties:1-42`) contains:

**Server Configuration:**
- `server.port=9966` (`/projects/legacy/src/main/resources/application.properties:23`)
- `server.servlet.context-path=/petclinic/` (`/projects/legacy/src/main/resources/application.properties:24`)

**Spring Profile Configuration:**
- Active profiles: `spring.profiles.active=hsqldb,spring-data-jpa` (`/projects/legacy/src/main/resources/application.properties:19`)
- Spring MVC path matching strategy (`/projects/legacy/src/main/resources/application.properties:28`)
- Messages basename (`/projects/legacy/src/main/resources/application.properties:30`)
- JPA configuration (`/projects/legacy/src/main/resources/application.properties:31`)

**Logging Configuration:**
- `logging.level.org.springframework=INFO` (`/projects/legacy/src/main/resources/application.properties:33`)

**Application-Specific Configuration:**
- `petclinic.security.enable=false` for security disablement (`/projects/legacy/src/main/resources/application.properties:41`)

## Contract Requirements

### Preserve Requirements (from migration.yaml:27-29)
The following exact configuration keys must be preserved:
- `petclinic.security.enable=false` - maintains security disabled state
- `server.servlet.context-path=/petclinic/` - maintains API base path

### Package Mapping
Full prefix replacement: `org.springframework.samples.petclinic.X` → `com.demo.X` (from migration.yaml:8)

### Target Platform Contract
- Quarkus 3.27 with Red Hat BOM (`com.redhat.quarkus.platform:3.27.3.SP1`)
- Java 21 compilation target
- Native Quarkus extensions only (no Spring compatibility extensions)
- Default CDI scope: `@ApplicationScoped` for services and repositories
- REST resources under `/api/`
- Health endpoints from `quarkus-smallrye-health` at `/q/health`

## Legacy Evidence

**POM Dependencies requiring transformation:**
- `/projects/legacy/pom.xml:40-70` - All Spring Boot starter dependencies
- `/projects/legacy/pom.xml:155-159` - javax.xml.bind dependencies (jakarta conversion)
- `/projects/legacy/pom.xml:164-184` - spring-boot-maven-plugin

**Properties requiring Quarkus migration:**
- `/projects/legacy/src/main/resources/application.properties:23-24` - Server configuration
- `/projects/legacy/src/main/resources/application.properties:33` - Logging levels
- `/projects/legacy/src/main/resources/application.properties:41` - Security configuration

**Out of scope for this story:**
- All Java source files under `/projects/legacy/src/main/java/`
- All Java test files under `/projects/legacy/src/test/java/`
- Application bootstrap class `PetClinicApplication.java` (handled in S02)
- REST controllers, services, and repositories (handled in subsequent stories)

This specification focuses solely on the foundation-level infrastructure changes required to establish the Quarkus build and runtime environment.