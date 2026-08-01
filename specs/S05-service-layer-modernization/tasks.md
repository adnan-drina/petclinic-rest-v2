# S05: Service Layer Modernization - Tasks

## T-001: Create service package structure
**Shape**: structure
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003

Create target package directory for service interfaces and implementations.

**Target design**: → `src/main/java/com/demo/service/.gitkeep`

**Actions**:
```bash
mkdir -p src/main/java/com/demo/service
touch src/main/java/com/demo/service/.gitkeep
```

**Acceptance**: Directory exists with `.gitkeep`; structure sensor green

**Owns**:
- src/main/java/com/demo/service/

---

## T-002: Harvest service interfaces
**Shape**: create
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003

Harvest service interfaces from staging with package rename. Preserve method signatures exactly. Replace Spring-specific exception types with appropriate alternatives.

**Target design**:
- `migration/staging/.../service/ClinicService.java` → `src/main/java/com/demo/service/ClinicService.java`
- `migration/staging/.../service/UserService.java` → `src/main/java/com/demo/service/UserService.java`

**Actions**:
1. Harvest interfaces from staging tree (jakarta imports already applied by recipe)
2. Package rename `org.springframework.samples.petclinic.service` → `com.demo.service`
3. Update model imports to `com.demo.model.*`
4. Replace `org.springframework.dao.DataAccessException` with appropriate exception type
5. Do not change method signatures

**Acceptance**: Two interfaces compile under `com.demo.service`

**Owns**:
- src/main/java/org/springframework/samples/petclinic/service/ClinicService.java
- src/main/java/org/springframework/samples/petclinic/service/UserService.java

---

## T-003: Redesign ClinicServiceImpl to @ApplicationScoped CDI bean
**Shape**: create
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003 (2 incidents)

Convert `ClinicServiceImpl` from Spring `@Service` to Quarkus `@ApplicationScoped` CDI bean with thread-safe state management.

**Target design**: → `src/main/java/com/demo/service/ClinicServiceImpl.java`

- `@Service` → `@ApplicationScoped`
- `@Autowired` constructor → CDI constructor injection (6 repository parameters)
- `org.springframework.transaction.annotation.Transactional` → `jakarta.transaction.Transactional`
- `@Cacheable(value = "vets")` on `findVets()` → **thread-safe** **ConcurrentHashMap** cache with **refresh-guard** (refresh only when key absent AND no refresh in last 60s)
- Spring `DataAccessException` → appropriate Quarkus exception
- `ObjectRetrievalFailureException` / `EmptyResultDataAccessException` → Quarkus/Hibernate equivalents
- All instance variables remain immutable (repositories injected via constructor)
- **No shared mutable state** — cache field uses ConcurrentHashMap with compute-based access
- Package: `com.demo.service`
- Repository imports: `com.demo.repository.*`
- Model imports: `com.demo.model.*`

**Signature preservation**: All 25 methods from `ClinicService` interface preserved with exact return types. `findVets()` becomes the only cached method using ConcurrentHashMap internally.

**Acceptance**: `ClinicServiceImpl` compiles as `@ApplicationScoped` CDI bean implementing `ClinicService`; `mvn -q compile` passes

**Owns**:
- src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java

---

## T-004: Redesign UserServiceImpl to @ApplicationScoped CDI bean
**Shape**: create
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003 (2 incidents)

Convert `UserServiceImpl` from Spring `@Service` to Quarkus `@ApplicationScoped` CDI bean.

**Target design**: → `src/main/java/com/demo/service/UserServiceImpl.java`

- `@Service` → `@ApplicationScoped`
- `@Autowired` field injection → CDI constructor injection of `UserRepository`
- `org.springframework.transaction.annotation.Transactional` → `jakarta.transaction.Transactional`
- Preserve business logic: role validation, "ROLE_" prefix normalization, bidirectional reference setup
- Package: `com.demo.service`
- Repository imports: `com.demo.repository.*`
- Model imports: `com.demo.model.*`

**Signature preservation**: `saveUser(User user) throws Exception` preserved exactly.

**Acceptance**: `UserServiceImpl` compiles as `@ApplicationScoped` CDI bean implementing `UserService`; `mvn -q compile` passes

**Owns**:
- src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java

---

## T-005: Service characterization tests
**Shape**: create
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003

Add focused unit tests for service layer using Mockito test doubles for repository dependencies. Pin target contract behavior: thread-safe state management and correct delegation to repositories.

**Target design**:
- → `src/test/java/com/demo/service/ClinicServiceImplTest.java`
- → `src/test/java/com/demo/service/UserServiceImplTest.java`

**Test coverage**:
- `ClinicServiceImpl`: verify find/save/delete delegation to injected repositories; verify `findVets()` caching behavior uses ConcurrentHashMap; verify null return on not-found for find-by-id methods
- `UserServiceImpl`: verify role validation (empty roles throws Exception); verify "ROLE_" prefix normalization; verify bidirectional reference setup; verify delegation to `userRepository.save()`

Use Mockito mocks for repository interfaces. Tests use `@QuarkusTest` or plain JUnit 5 with Mockito.

**Acceptance**: Service tests pass with ≥80% line coverage on service classes; `mvn -q clean test` green

---

## Legacy UI Surface Waiver

**Waiver**: Legacy UI surface (web interface / Thymeleaf / static UI) is explicitly out of scope for service-layer modernization. UI and REST surfaces are handled in later stories (S06).

## Preserve Items

- `petclinic.security.enable` — security configuration preserved as Quarkus property
- `server.servlet.context-path` — context path preserved as `quarkus.http.root-path`

## Acceptance Path Defer

Acceptance HTTP path is deferred to the deploy story per O-M3ACCEPT / S-AC1 / G-OK. This non-deploy story does not task any HTTP endpoint with Java @Path substance.

---

## T-006: Finding-scope boundaries (prior + later)
**Shape**: structure
**Class**: infer
**Findings**: springboot-di-to-quarkus-00003

No new S05 SUTs. Claim residual DI finding incidents already delivered by S04
(repositories; JpaUserRepositoryImpl refresh shape already in tree) or reserved
for later stories (REST/security/OpenAPI) so plan-lint K1 stays green.

**Target design**:
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetTypeRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcSpecialtyRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcUserRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVetRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVisitRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetTypeRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaSpecialtyRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaUserRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVetRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVisitRepositoryImpl.java`
- → `src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java`
- → `src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java`
- → `src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java`
- → `src/main/java/org/springframework/samples/petclinic/security/Roles.java`
- → `src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java`

**Absorbs**: prior-S04 and later-story DI incidents listed in Target design above
(JpaUserRepositoryImpl refresh already satisfied by S04).

**Acceptance**: plan-lint green for S05 findings-scope; no new repository/rest/security/util src/main from this task
