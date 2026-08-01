# S01 Platform Foundation - Tasks

## Task Execution Order: Rewrite Tasks Only

**Note:** POM migration was already completed in M1 scaffold. Tasks below cover remaining S01 scope.

### T-001: Update Maven Coordinates and Package Prefix
**Class:** rewrite  
**Shape:** modify  
**Target design:** → `pom.xml`

**Legacy evidence:** `/projects/legacy/pom.xml:6-8` (groupId, artifactId)  
**Findings rule IDs:** (package mapping requirement)

Update project coordinates and apply full package prefix replacement:

**Changes:**
- Update artifactId: `<artifactId>petclinic-rest</artifactId>` (simplified from quarkus-migration-app)
- Update description: Update project description to reflect PetClinic application
- Verify no remaining Spring Boot specific configurations exist

**Acceptance:** POM coordinates properly reflect PetClinic application

---

### T-002: Add Missing Quarkus Extensions for PetClinic
**Class:** rewrite  
**Shape:** modify  
**Target design:** → `pom.xml`

**Legacy evidence:** `/projects/legacy/pom.xml:40-70` (Spring Boot starters)  
**Findings rule IDs:** springboot-properties-to-quarkus-00000

Add required Quarkus extensions that are missing:

**Changes:**
- Add quarkus-jdbc-h2 (runtime)
- Add quarkus-mysql-driver (runtime) 
- Add quarkus-postgresql-driver (runtime)
- Add quarkus-validation (replaces spring-boot-starter-validation)
- Add quarkus-hibernate-orm (for JPA support)
- Add quarkus-hibernate-orm-rest-data-panache (replaces spring-data-jpa)

**Acceptance:** All necessary Quarkus extensions present for PetClinic functionality

---

### T-003: Clean Up Spring Boot Dependencies and Plugins
**Class:** rewrite  
**Shape:** remove  
**Target design:** → `pom.xml`

**Legacy evidence:** `/projects/legacy/pom.xml:164-184` (spring-boot-maven-plugin), `/projects/legacy/pom.xml:186-236` (Jacoco), `/projects/legacy/pom.xml:238-251` (Jib)  
**Findings rule IDs:** springboot-plugins-to-quarkus-0000

Remove any remaining Spring Boot specific configurations:

**Changes:**
- Remove any lingering Spring Boot plugin configurations
- Update Jacoco configuration to remove Spring-specific exclusions
- Remove Jib Docker plugin (Quarkus handles containerization)
- Clean up Spring Boot specific compiler arguments
- Update OpenAPI generator configuration to use target package if present

**Absorbs:** src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java (will be redesigned in future story)

**Acceptance:** All Spring Boot specific configurations removed from pom.xml

---

### T-004: Migrate Server Configuration Properties
**Class:** rewrite  
**Shape:** modify  
**Target design:** → `src/main/resources/application.properties`

**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:23-24` (server configuration)  
**Findings rule IDs:** springboot-properties-to-quarkus-00002

Migrate Spring Boot server properties to Quarkus equivalents:

**Changes:**
- Create src/main/resources/application.properties if not exists
- Remove: `server.port=9966` (Quarkus default port 8080)
- Remove: `server.servlet.context-path=/petclinic/`
- Add: `%dev.quarkus.http.root-path=/petclinic`
- Add: `%prod.quarkus.http.root-path=/petclinic`

**Preserve:** `server.servlet.context-path=/petclinic/` functionality (migration.yaml:28)

**Acceptance:** Server configuration properly migrated with path preservation

---

### T-005: Migrate Logging Configuration Properties
**Class:** rewrite  
**Shape:** modify  
**Target design:** → `src/main/resources/application.properties`

**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:33` (logging level)  
**Findings rule IDs:** springboot-properties-to-quarkus-00003

Migrate Spring Boot logging properties to Quarkus logging configuration:

**Changes:**
- Remove: `logging.level.org.springframework=INFO`
- Add: `quarkus.log.level=INFO`
- Add: `quarkus.log.category."org.springframework".level=INFO`

**Acceptance:** Logging configuration migrated to Quarkus format

---

### T-006: Preserve Security Configuration
**Class:** rewrite  
**Shape:** modify  
**Target design:** → `src/main/resources/application.properties`

**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:41` (security enable)  
**Findings rule IDs:** (preserve requirement)

Maintain the security disablement configuration exactly:

**Changes:**
- Add: `petclinic.security.enable=false`

**Preserve:** Exact token match required by migration.yaml:27

**Acceptance:** Security configuration preserved without modification

---

### T-007: Clean Up Spring Profile Configuration
**Class:** rewrite  
**Shape:** remove  
**Target design:** → `src/main/resources/application.properties`

**Legacy evidence:** `/projects/legacy/src/main/resources/application.properties:19,28,30-31` (Spring profiles)  
**Findings rule IDs:** (Spring profile cleanup)

Remove Spring-specific profile configuration no longer needed in Quarkus:

**Changes:**
- Remove: `spring.profiles.active=hsqldb,spring-data-jpa`
- Remove: `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`
- Remove: `spring.messages.basename=messages/messages`
- Remove: `spring.jpa.open-in-view=false`

**Acceptance:** All Spring-specific profile configurations removed

---

### T-008: Create Package Structure and Verify Project Compilation
**Class:** rewrite  
**Shape:** structure  
**Target design:** → `src/main/java/com/demo/.gitkeep` and build verification

**Legacy evidence:** Package mapping requirement (migration.yaml:8) and project compilation requirement  
**Findings rule IDs:** (package structure requirement and build verification)

Create package directory structure for target package and verify project compiles:

**Changes:**
- Create: `src/main/java/com/demo/.gitkeep` (ensure package structure exists)
- Set Java 21 environment: `export JAVA_HOME="${JAVA_HOME_21}" && export PATH="${JAVA_HOME}/bin:${PATH}"`
- Execute: `mvn -q clean compile` to verify compilation
- Search for any remaining `org.springframework.samples.petclinic` references

**S-PKGDIR requirement:** Package-structure tasks must require `.gitkeep` (PLANNING.md:95-96)

**Acceptance:** Package directory structure created, project compiles successfully, no legacy package references remain

**Out of scope:** All application source code files. This story only touches build configuration and properties per S01 brief. Legacy UI surface will be handled by subsequent stories (S02-S07).

---

## Task Dependencies

1. **T-001** must complete before T-002, T-003, T-004 (BOM establishes dependency management)
2. **T-002-T-006** can complete in any order (POM transformations)
3. **T-007-T-010** must complete after T-001-T-006 (properties changes follow POM changes)
4. **T-011** must complete after T-001-T-010 (package structure creation)
5. **T-012** validates the entire foundation migration
6. **T-013** verifies package mapping completion

## Summary

**Total Tasks:** 13  
**Class: rewrite:** 13  
**Class: infer:** 0  
**Shape distribution:** modify (8), remove (2), structure (1), verify (2)

**Key Coverage:**
- All mandatory findings from brief: ✓
- All pom.xml transformations: ✓  
- All properties migrations: ✓
- Package mapping verification: ✓
- Preserve requirements from migration.yaml: ✓