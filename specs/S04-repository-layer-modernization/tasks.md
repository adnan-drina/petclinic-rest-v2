# S04: Repository Layer Modernization - Tasks

## T-001: Create repository package structure
**Shape**: structure
**Class**: rewrite
**Findings**: springboot-di-to-quarkus-00003

Create target package directories for repository interfaces and profile-specific implementations.

**Target design**: → `src/main/java/com/demo/repository/.gitkeep`, `src/main/java/com/demo/repository/jpa/.gitkeep`, `src/main/java/com/demo/repository/jdbc/.gitkeep`, `src/main/java/com/demo/repository/springdatajpa/.gitkeep`

**Actions**:
```bash
mkdir -p src/main/java/com/demo/repository/jpa
mkdir -p src/main/java/com/demo/repository/jdbc
mkdir -p src/main/java/com/demo/repository/springdatajpa
touch src/main/java/com/demo/repository/.gitkeep
touch src/main/java/com/demo/repository/jpa/.gitkeep
touch src/main/java/com/demo/repository/jdbc/.gitkeep
touch src/main/java/com/demo/repository/springdatajpa/.gitkeep
```

**Acceptance**: Directories exist; structure sensor green

**Owns**:
- src/main/java/com/demo/repository/

---

## T-002: Harvest repository interfaces
**Shape**: create
**Class**: rewrite
**Findings**: springboot-di-to-quarkus-00003

Harvest repository interfaces from staging/legacy with package rename. Preserve method signatures exactly.

**Target design**:
- `.../repository/OwnerRepository.java` → `src/main/java/com/demo/repository/OwnerRepository.java`
- `.../repository/PetRepository.java` → `src/main/java/com/demo/repository/PetRepository.java`
- `.../repository/VisitRepository.java` → `src/main/java/com/demo/repository/VisitRepository.java`
- `.../repository/VetRepository.java` → `src/main/java/com/demo/repository/VetRepository.java`
- `.../repository/PetTypeRepository.java` → `src/main/java/com/demo/repository/PetTypeRepository.java`
- `.../repository/SpecialtyRepository.java` → `src/main/java/com/demo/repository/SpecialtyRepository.java`
- `.../repository/UserRepository.java` → `src/main/java/com/demo/repository/UserRepository.java`

**Actions**:
1. Copy interfaces from staging (or legacy if staging absent)
2. Package rename `org.springframework.samples.petclinic.repository` → `com.demo.repository`
3. Update model imports to `com.demo.model.*`
4. Do not change method signatures

**Acceptance**: Seven interfaces compile under `com.demo.repository`

**Owns**:
- src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/VisitRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/VetRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/PetTypeRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/SpecialtyRepository.java
- src/main/java/org/springframework/samples/petclinic/repository/UserRepository.java

---

## T-003: Redesign JPA repository implementations to CDI
**Shape**: create
**Class**: rewrite
**Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003

Redesign JPA repository implementations: `@Repository`/`@Autowired` → `@ApplicationScoped` + constructor injection; keep `@Transactional` semantics (readOnly vs write). Use `jakarta.persistence.EntityManager`.

**Target design**:
- `.../jpa/JpaOwnerRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaOwnerRepositoryImpl.java`
- `.../jpa/JpaPetRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaPetRepositoryImpl.java`
- `.../jpa/JpaVisitRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaVisitRepositoryImpl.java`
- `.../jpa/JpaVetRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaVetRepositoryImpl.java`
- `.../jpa/JpaPetTypeRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaPetTypeRepositoryImpl.java`
- `.../jpa/JpaSpecialtyRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaSpecialtyRepositoryImpl.java`
- `.../jpa/JpaUserRepositoryImpl.java` → `src/main/java/com/demo/repository/jpa/JpaUserRepositoryImpl.java`

**Actions**:
1. Harvest from staging/legacy jpa package
2. Package rename + model/interface imports to `com.demo.*`
3. Replace Spring stereotypes with CDI (`@ApplicationScoped`, constructor inject EntityManager)
4. Preserve `@Transactional` / add explicit `@Transactional` on mutating methods (MAPPINGS transaction-to-quarkus-00003)
5. Drop Spring `@Profile("jpa")` or map to Quarkus `@IfBuildProperty` / `@LookupIfProperty` only if required for multi-impl; default active impl = JPA for Quarkus

**Target-trace note**: architecture-profile §7 MapStruct refresh shapes do not apply to repositories; use EntityManager + `@Transactional` on save/delete (no mapper refresh).

**Acceptance**: JPA impls compile as CDI beans implementing the harvested interfaces

**Owns**:
- src/main/java/org/springframework/samples/petclinic/repository/jpa/

---

## T-004: Redesign JDBC repository implementations to CDI
**Shape**: create
**Class**: rewrite
**Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003

Redesign JDBC repository implementations and helpers to CDI. Prefer Agroal `DataSource` injection over Spring `NamedParameterJdbcTemplate` where a thin redesign is required; preserve SQL behavior.

**Target design**:
- `.../jdbc/JdbcOwnerRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcOwnerRepositoryImpl.java`
- `.../jdbc/JdbcPetRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcPetRepositoryImpl.java`
- `.../jdbc/JdbcVisitRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcVisitRepositoryImpl.java`
- `.../jdbc/JdbcVetRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcVetRepositoryImpl.java`
- `.../jdbc/JdbcPetTypeRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcPetTypeRepositoryImpl.java`
- `.../jdbc/JdbcSpecialtyRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcSpecialtyRepositoryImpl.java`
- `.../jdbc/JdbcUserRepositoryImpl.java` → `src/main/java/com/demo/repository/jdbc/JdbcUserRepositoryImpl.java`
- helpers (`JdbcPet.java`, row mappers, extractors) → `src/main/java/com/demo/repository/jdbc/`

**Acceptance**: JDBC impls compile under `com.demo.repository.jdbc` with CDI constructors

**Owns**:
- src/main/java/org/springframework/samples/petclinic/repository/jdbc/

---

## T-005: Redesign Spring Data JPA repository layer
**Shape**: create
**Class**: rewrite
**Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003

Modernize Spring Data JPA repositories/overrides. Prefer Quarkus-compatible shapes (CDI beans + EntityManager or quarkus-spring-data-jpa only if already on classpath); do not pull spring-di extension. Preserve custom override methods.

**Target design**:
- `.../springdatajpa/SpringDataOwnerRepository.java` → `src/main/java/com/demo/repository/springdatajpa/SpringDataOwnerRepository.java`
- `.../springdatajpa/SpringDataPetRepository.java` (+ Impl/Override) → `src/main/java/com/demo/repository/springdatajpa/`
- `.../springdatajpa/SpringDataVisitRepository.java` (+ Impl/Override) → `src/main/java/com/demo/repository/springdatajpa/`
- remaining SpringData* + *Override types → `src/main/java/com/demo/repository/springdatajpa/`

**Acceptance**: springdatajpa package compiles; interfaces still satisfy `com.demo.repository.*` contracts where applicable

**Owns**:
- src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/

---

## T-006: Repository characterization tests and package verify
**Shape**: verify
**Class**: infer
**Findings**: transaction-to-quarkus-00003

Add focused unit/characterization tests for at least one JPA repository (Owner or Pet) covering find/save transactional behavior; run package verify. REST controllers remain S06 — this story only ensures repository beans exist for later wiring.

**Target design**:
- → `src/test/java/com/demo/repository/jpa/JpaOwnerRepositoryTest.java`
- package/verify green with existing model tests
- Preserve untouched this story: `petclinic.security.enable`, `server.servlet.context-path` → `quarkus.http.root-path` (already in application.properties from prior stories)

**Acceptance**: New repository test(s) with real asserts; `mvn -DskipITs package` green; no Spring `@Repository` left under `com.demo.repository`

**Owns**:
- src/test/java/com/demo/repository/

---

## Legacy UI Surface Waiver

**Waiver**: Legacy UI surface (web interface / Thymeleaf / static UI) is explicitly out of scope for repository-layer modernization. UI and REST surfaces are handled in later stories (S06).
