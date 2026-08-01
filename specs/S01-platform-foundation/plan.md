# S01 Platform Foundation - Migration Plan

## Quarkus Platform Mapping

This plan maps the legacy Spring Boot platform configuration to Quarkus equivalents following the standards path (native Quarkus APIs, no Spring compatibility extensions).

## Migration Strategy

**Class Classification:**
- **Class: rewrite** - All POM and configuration transformations are mechanical changes covered by OpenRewrite recipes
- **Class: infer** - None required for this story (no application code modernization)

## Platform Foundation Tasks

### T-001: Replace Spring Boot Parent with Quarkus BOM
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** springboot-parent-pom-to-quarkus-00000, javaee-pom-to-quarkus-00010

Replace the Spring Boot parent POM with Quarkus BOM to establish the Quarkus dependency management:

**Target design:** → `pom.xml`  
**Legacy evidence:** `/projects/legacy/pom.xml:13-18` (Spring Boot parent), `/projects/legacy/pom.xml:4` (POM declaration)

Changes:
- Remove Spring Boot parent: `<parent><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-parent</artifactId></parent>`
- Add Quarkus BOM: `<dependencyManagement><dependencies><dependency><groupId>com.redhat.quarkus.platform</groupId><artifactId>quarkus-bom</artifactId><version>3.27.3.SP1</version><type>pom</type><scope>import</scope></dependency></dependencies></dependencyManagement>`

### T-002: Update Maven Coordinates and Package Prefix
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** (package mapping requirement)

Update project coordinates and apply full package prefix replacement:

**Target design:** → `pom.xml`  
**Legacy evidence:** `/projects/legacy/pom.xml:6-8` (groupId, artifactId)

Changes:
- Update groupId: `<groupId>com.demo</groupId>` (from migration.yaml:8 targetPackage)
- Update artifactId: `<artifactId>petclinic-rest</artifactId>` (simplified)
- Update version: `<version>1.0.0</version>` (new versioning)

### T-003: Replace Spring Boot Maven Plugin with Quarkus Maven Plugin
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** springboot-plugins-to-quarkus-0000, javaee-pom-to-quarkus-00020

Replace the Spring Boot Maven plugin configuration with Quarkus Maven plugin:

**Target design:** → `pom.xml`  
**Legacy evidence:** `/projects/legacy/pom.xml:164-184` (spring-boot-maven-plugin)

Changes:
- Remove: `<plugin><groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId></plugin>`
- Add: `<plugin><groupId>io.quarkus</groupId><artifactId>quarkus-maven-plugin</artifactId><version>3.27.3.SP1</version><executions><execution><goals><goal>build</goal></goals></execution></executions></plugin>`

### T-004: Convert Spring Boot Dependencies to Quarkus Extensions
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** springboot-properties-to-quarkus-00000

Replace Spring Boot starter dependencies with Quarkus extension dependencies:

**Target design:** → `pom.xml`  
**Legacy evidence:** `/projects/legacy/pom.xml:40-70` (Spring Boot starters)

Changes:
- Remove Spring Boot starters: `spring-boot-starter-actuator`, `spring-boot-starter-aop`, `spring-boot-starter-cache`, `spring-boot-starter-data-jpa`, `spring-boot-starter-jdbc`, `spring-boot-starter-web`, `spring-boot-starter-security`, `spring-boot-starter-validation`
- Add Quarkus equivalents:
  ```xml
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-health</artifactId>
  </dependency>
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-rest-jackson</artifactId>
  </dependency>
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-jdbc-h2</artifactId>
      <scope>runtime</scope>
  </dependency>
  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-hibernate-orm-rest-data-panache</artifactId>
  </dependency>
  ```

### T-005: Migrate javax Dependencies to jakarta
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** javax-to-jakarta-dependencies-00001, javax-to-jakarta-dependencies-00003

Replace javax dependencies with jakarta equivalents:

**Target design:** → `pom.xml`  
**Legacy evidence:** `/projects/legacy/pom.xml:155-159` (javax.xml.bind)

Changes:
- Remove: `<groupId>javax.xml.bind</groupId><artifactId>jaxb-api</artifactId>`
- Add: `<groupId>jakarta.xml.bind</groupId><artifactId>jakarta.xml.bind-api</artifactId>`

### T-006: Update Maven Compiler Configuration for Java 21
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** (compiler target update)

Update Maven compiler plugin to target Java 21:

**Target design:** → `pom.xml`  
**Legacy evidence:** (implicit compiler configuration)

Changes:
- Add/Update compiler plugin:
  ```xml
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.11.0</version>
      <configuration>
          <source>21</source>
          <target>21</target>
      </configuration>
  </plugin>
  ```

### T-007: Migrate Server Configuration Properties
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** springboot-properties-to-quarkus-00002

Migrate Spring Boot server properties to Quarkus equivalents:

**Target design:** → `src/main/resources/application.properties`  
**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:23-24` (server configuration)

Changes:
- Remove: `server.port=9966` → Quarkus uses port 8080 by default
- Remove: `server.servlet.context-path=/petclinic/` → Quarkus uses `%dev.quarkus.http.root-path=/petclinic`
- Add: `%dev.quarkus.http.root-path=/petclinic`
- Add: `%prod.quarkus.http.root-path=/petclinic`

### T-008: Migrate Logging Configuration Properties
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** springboot-properties-to-quarkus-00003

Migrate Spring Boot logging properties to Quarkus logging configuration:

**Target design:** → `src/main/resources/application.properties`  
**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:33` (logging level)

Changes:
- Remove: `logging.level.org.springframework=INFO`
- Add: `quarkus.log.level=INFO`
- Add: `quarkus.log.category."org.springframework".level=INFO`

### T-009: Preserve Security Configuration
**Class:** rewrite  
**Shape:** modify  
**Finding Rule IDs:** (preserve requirement from migration.yaml:27-28)

Maintain the security disablement configuration:

**Target design:** → `src/main/resources/application.properties`  
**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:41` (security enable)

Changes:
- Preserve: `petclinic.security.enable=false` (exact token match required by migration.yaml:27-28)

### T-010: Clean Up Spring Profile Configuration
**Class:** rewrite  
**Shape:** remove  
**Finding Rule IDs:** (Spring profile cleanup)

Remove Spring-specific profile configuration no longer needed in Quarkus:

**Target design:** → `src/main/resources/application.properties`  
**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:19,28,30-31` (Spring profiles)

Changes:
- Remove: `spring.profiles.active=hsqldb,spring-data-jpa`
- Remove: `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`
- Remove: `spring.messages.basename=messages/messages`
- Remove: `spring.jpa.open-in-view=false`

### T-011: Verify Project Compilation
**Class:** rewrite  
**Shape:** verify  
**Finding Rule IDs:** (build verification)

Verify that the migrated project compiles successfully:

**Target design:** → Build verification  
**Legacy evidence:** (project compilation check)

Changes:
- Run: `export JAVA_HOME="${JAVA_HOME_21}" && export PATH="${JAVA_HOME}/bin:${PATH}"`
- Execute: `mvn -q clean compile`
- Expected: Successful compilation with no errors

## Task Dependencies

1. T-001 must complete before T-003 and T-004 (BOM establishes dependency management)
2. T-007-T-009 must complete after T-001-T-006 (properties changes follow POM changes)
3. T-011 validates the entire foundation migration

## Package Rename Verification

All file paths and Java packages must use the target package prefix `com.demo` instead of legacy `org.springframework.samples.petclinic` (migration.yaml:8).

## Acceptance Criteria

- Maven POM updated with Quarkus BOM and proper plugin configuration ✓
- Properties migrated to Quarkus equivalents where applicable ✓  
- Project builds successfully with `mvn clean compile` ✓
- No Spring Boot parent or Spring Boot dependencies remain ✓
- Recipe-executed findings (javax→jakarta) resolved for dependencies ✓
- Security and API path configuration preserved per migration.yaml requirements ✓