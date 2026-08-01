# S04: Repository Layer Modernization - Specification

## Overview

This specification documents the migration of all repository interfaces, JDBC implementations, JPA implementations, and Spring Data JPA implementations from Spring PetClinic legacy application to Quarkus. Repository layer depends on domain models migrated in S03 and must be converted before services in S05.

## Scope

### In-Scope Classes

**Repository Interfaces (HARVEST):**
- `org.springframework.samples.petclinic.repository.OwnerRepository` → `com.demo.repository.OwnerRepository`
- `org.springframework.samples.petclinic.repository.PetRepository` → `com.demo.repository.PetRepository`
- `org.springframework.samples.petclinic.repository.VisitRepository` → `com.demo.repository.VisitRepository`
- `org.springframework.samples.petclinic.repository.VetRepository` → `com.demo.repository.VetRepository`
- `org.springframework.samples.petclinic.repository.SpecialtyRepository` → `com.demo.repository.SpecialtyRepository`
- `org.springframework.samples.petclinic.repository.PetTypeRepository` → `com.demo.repository.PetTypeRepository`
- `org.springframework.samples.petclinic.repository.UserRepository` → `com.demo.repository.UserRepository`

**JDBC Repository Implementations (REDESIGN):**
- `JdbcOwnerRepositoryImpl` → `com.demo.repository.jdbc.JdbcOwnerRepositoryImpl`
- `JdbcPetRepositoryImpl` → `com.demo.repository.jdbc.JdbcPetRepositoryImpl`
- `JdbcVisitRepositoryImpl` → `com.demo.repository.jdbc.JdbcVisitRepositoryImpl`
- `JdbcVetRepositoryImpl` → `com.demo.repository.jdbc.JdbcVetRepositoryImpl`
- `JdbcSpecialtyRepositoryImpl` → `com.demo.repository.jdbc.JdbcSpecialtyRepositoryImpl`
- `JdbcPetTypeRepositoryImpl` → `com.demo.repository.jdbc.JdbcPetTypeRepositoryImpl`
- `JdbcUserRepositoryImpl` → `com.demo.repository.jdbc.JdbcUserRepositoryImpl`

**JDBC Helper Classes (HARVEST):**
- `JdbcPet`, `JdbcPetRowMapper`, `JdbcPetVisitExtractor`, `JdbcVisitRowMapper`

**JPA Repository Implementations (REDESIGN):**
- `JpaOwnerRepositoryImpl` → `com.demo.repository.jpa.JpaOwnerRepositoryImpl`
- `JpaPetRepositoryImpl` → `com.demo.repository.jpa.JpaPetRepositoryImpl`
- `JpaVisitRepositoryImpl` → `com.demo.repository.jpa.JpaVisitRepositoryImpl`
- `JpaVetRepositoryImpl` → `com.demo.repository.jpa.JpaVetRepositoryImpl`
- `JpaSpecialtyRepositoryImpl` → `com.demo.repository.jpa.JpaSpecialtyRepositoryImpl`
- `JpaPetTypeRepositoryImpl` → `com.demo.repository.jpa.JpaPetTypeRepositoryImpl`
- `JpaUserRepositoryImpl` → `com.demo.repository.jpa.JpaUserRepositoryImpl`

**Spring Data JPA Implementations (REDESIGN):**
- `SpringDataPetRepositoryImpl` → `com.demo.repository.springdatajpa.SpringDataPetRepositoryImpl`
- `SpringDataVisitRepositoryImpl` → `com.demo.repository.springdatajpa.SpringDataVisitRepositoryImpl`
- `SpringDataPetTypeRepositoryImpl` → `com.demo.repository.springdatajpa.SpringDataPetTypeRepositoryImpl`
- `SpringDataSpecialtyRepositoryImpl` → `com.demo.repository.springdatajpa.SpringDataSpecialtyRepositoryImpl`
- Override interfaces: `PetRepositoryOverride`, `VisitRepositoryOverride`, `PetTypeRepositoryOverride`, `SpecialtyRepositoryOverride`
- Spring Data JPA repository interfaces: `SpringDataOwnerRepository`, `SpringDataPetRepository`, etc.

## Observed Legacy Behavior

### Repository Interface Contracts

All repository interfaces use `org.springframework.dao.DataAccessException` as the thrown exception type. Method signatures are preserved exactly.

**OwnerRepository** (src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java:34-79):
- `findByLastName(String)` → `Collection<Owner>`
- `findById(int)` → `Owner`
- `save(Owner)` → `void`
- `findAll()` → `Collection<Owner>`
- `delete(Owner)` → `void`

**PetRepository** (src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java:36-76):
- `findPetTypes()` → `List<PetType>`
- `findById(int)` → `Pet`
- `save(Pet)` → `void`
- `findAll()` → `Collection<Pet>`
- `delete(Pet)` → `void`

**VisitRepository** (src/main/java/org/springframework/samples/petclinic/repository/VisitRepository.java:35-51):
- `save(Visit)` → `void`
- `findByPetId(Integer)` → `List<Visit>`
- `findById(int)` → `Visit`
- `findAll()` → `Collection<Visit>`
- `delete(Visit)` → `void`

**VetRepository** (src/main/java/org/springframework/samples/petclinic/repository/VetRepository.java:33-46):
- `findAll()` → `Collection<Vet>`
- `findById(int)` → `Vet`
- `save(Vet)` → `void`
- `delete(Vet)` → `void`

**SpecialtyRepository** (src/main/java/org/springframework/samples/petclinic/repository/SpecialtyRepository.java:29-37):
- `findById(int)` → `Specialty`
- `findAll()` → `Collection<Specialty>`
- `save(Specialty)` → `void`
- `delete(Specialty)` → `void`

**PetTypeRepository** (src/main/java/org/springframework/samples/petclinic/repository/PetTypeRepository.java:29-37):
- `findById(int)` → `PetType`
- `findAll()` → `Collection<PetType>`
- `save(PetType)` → `void`
- `delete(PetType)` → `void`

**UserRepository** (src/main/java/org/springframework/samples/petclinic/repository/UserRepository.java:6-8):
- `save(User)` → `void`

### JDBC Implementation Patterns

All JDBC implementations use `@Repository` + `@Profile("jdbc")` annotations, with `@Autowired` constructor injection of `DataSource` and other repositories.

**JdbcOwnerRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:54-62):
- `@Repository` + `@Profile("jdbc")`
- Constructor: `@Autowired JdbcOwnerRepositoryImpl(DataSource dataSource)`
- Uses `NamedParameterJdbcTemplate` and `SimpleJdbcInsert`
- `delete()` annotated with `javax.transaction.Transactional` (line 174)

**JdbcPetRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java:54-67):
- Constructor: `@Autowired JdbcPetRepositoryImpl(DataSource, OwnerRepository, VisitRepository)`
- Injects other repository interfaces

### JPA Implementation Patterns

All JPA implementations use `@Repository` + `@Profile("jpa")` annotations, with `@PersistenceContext` field injection of `EntityManager`.

**JpaOwnerRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java:40-45):
- `@Repository` + `@Profile("jpa")`
- `@PersistenceContext private EntityManager em`
- JPQL queries for CRUD operations

**JpaPetRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java:80):
- `delete()` uses `em.remove()` — requires `@Transactional` in Quarkus (transaction-to-quarkus-00003)

### Spring Data JPA Patterns

**SpringDataPetRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepositoryImpl.java:30-42):
- `@Profile("spring-data-jpa")` (no `@Repository`)
- `@PersistenceContext private EntityManager em`
- Implements `PetRepositoryOverride` interface
- `delete()` uses `em.remove()` — requires `@Transactional` in Quarkus (transaction-to-quarkus-00003)

**SpringDataVisitRepositoryImpl** (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVisitRepositoryImpl.java:31-42):
- Same pattern, implements `VisitRepositoryOverride`
- `delete()` uses `em.remove()` — requires `@Transactional` in Quarkus (transaction-to-quarkus-00003)

## Target Contracts

### DI Conversion (springboot-di-to-quarkus-00003)

- `@Repository` → `@ApplicationScoped`
- `@Autowired` constructor → CDI constructor injection
- `@PersistenceContext` → `@Inject EntityManager`
- `@Profile` → removed (Quarkus does not use Spring profiles; profile-specific beans handled via `%dev`/`%test`/`%prod` config prefixes in `application.properties`)
- Spring `@Transactional` → `jakarta.transaction.Transactional`

### Transaction Management (transaction-to-quarkus-00003)

All `EntityManager.remove()` operations require explicit `@Transactional` annotations in Quarkus:
- `JpaPetRepositoryImpl.delete()` (line 80)
- `SpringDataPetRepositoryImpl.delete()` (line 42)
- `SpringDataVisitRepositoryImpl.delete()` (line 42)

### Transactional annotations on all write operations

Per architecture profile §7: all database write operations (save, delete) on repository implementations must carry `@Transactional` annotations to ensure proper transaction management in Quarkus.

## Preserve Items

None — repository layer has no configuration surfaces. `petclinic.security.enable` and `server.servlet.context-path` are handled in other stories.

## Out of Scope

- Service layer (S05)
- REST controllers (S06)
- Security configuration (separate story)
- JDBC helper classes RowMapper/Extractor behavior changes (HARVEST — preserve exactly)

## Package Rename Rules

Full prefix replacement:
- `org.springframework.samples.petclinic.repository.*` → `com.demo.repository.*`
- `org.springframework.samples.petclinic.repository.jdbc.*` → `com.demo.repository.jdbc.*`
- `org.springframework.samples.petclinic.repository.jpa.*` → `com.demo.repository.jpa.*`
- `org.springframework.samples.petclinic.repository.springdatajpa.*` → `com.demo.repository.springdatajpa.*`
