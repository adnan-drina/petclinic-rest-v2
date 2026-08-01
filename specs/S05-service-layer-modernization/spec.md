# S05: Service Layer Modernization - Specification

## Overview

This specification documents the migration of the service layer (ClinicService, UserService) from Spring @Service to Quarkus @ApplicationScoped CDI beans. Services depend on repositories migrated in S04 and must be converted before REST controllers in S06.

## Scope

### In-Scope Classes

**Service Interfaces (HARVEST):**
- `org.springframework.samples.petclinic.service.ClinicService` → `com.demo.service.ClinicService`
- `org.springframework.samples.petclinic.service.UserService` → `com.demo.service.UserService`

**Service Implementations (REDESIGN):**
- `ClinicServiceImpl` → `com.demo.service.ClinicServiceImpl`
- `UserServiceImpl` → `com.demo.service.UserServiceImpl`

## Observed Legacy Behavior

### ClinicService Interface

Facade for all Petclinic controllers, providing CRUD operations across all domain entities.

**Legacy file**: `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java:35-71`

| Method | Return | Notes |
|---|---|---|
| `findPetById(int)` | `Pet` | throws `DataAccessException` |
| `findAllPets()` | `Collection<Pet>` | throws `DataAccessException` |
| `savePet(Pet)` | `void` | throws `DataAccessException` |
| `deletePet(Pet)` | `void` | throws `DataAccessException` |
| `findVisitsByPetId(int)` | `Collection<Visit>` | |
| `findVisitById(int)` | `Visit` | throws `DataAccessException` |
| `findAllVisits()` | `Collection<Visit>` | throws `DataAccessException` |
| `saveVisit(Visit)` | `void` | throws `DataAccessException` |
| `deleteVisit(Visit)` | `void` | throws `DataAccessException` |
| `findVetById(int)` | `Vet` | throws `DataAccessException` |
| `findVets()` | `Collection<Vet>` | throws `DataAccessException`; **@Cacheable(value="vets")** |
| `findAllVets()` | `Collection<Vet>` | throws `DataAccessException` |
| `saveVet(Vet)` | `void` | throws `DataAccessException` |
| `deleteVet(Vet)` | `void` | throws `DataAccessException` |
| `findOwnerById(int)` | `Owner` | throws `DataAccessException` |
| `findAllOwners()` | `Collection<Owner>` | throws `DataAccessException` |
| `saveOwner(Owner)` | `void` | throws `DataAccessException` |
| `deleteOwner(Owner)` | `void` | throws `DataAccessException` |
| `findOwnerByLastName(String)` | `Collection<Owner>` | throws `DataAccessException` |
| `findPetTypeById(int)` | `PetType` | |
| `findAllPetTypes()` | `Collection<PetType>` | throws `DataAccessException` |
| `findPetTypes()` | `Collection<PetType>` | delegates to `petRepository.findPetTypes()` |
| `savePetType(PetType)` | `void` | throws `DataAccessException` |
| `deletePetType(PetType)` | `void` | throws `DataAccessException` |
| `findSpecialtyById(int)` | `Specialty` | |
| `findAllSpecialties()` | `Collection<Specialty>` | throws `DataAccessException` |
| `saveSpecialty(Specialty)` | `void` | throws `DataAccessException` |
| `deleteSpecialty(Specialty)` | `void` | throws `DataAccessException` |

### ClinicServiceImpl Implementation

**Legacy file**: `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47-291`

- `@Service` + `@Transactional` (class-level, but per-method overrides apply)
- Constructor injection via `@Autowired` of 6 repository interfaces
- All read methods annotated `@Transactional(readOnly = true)`
- All write methods annotated `@Transactional`
- `findVets()` additionally annotated with `@Cacheable(value = "vets")` for caching
- Find-by-id methods catch `ObjectRetrievalFailureException` and `EmptyResultDataAccessException`, returning `null` (not throwing)

### UserService Interface

**Legacy file**: `src/main/java/org/springframework/samples/petclinic/service/UserService.java:5-8`

| Method | Return | Notes |
|---|---|---|
| `saveUser(User)` | `void` | throws `Exception` |

### UserServiceImpl Implementation

**Legacy file**: `src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java:10-36`

- `@Service` annotation
- Field injection via `@Autowired private UserRepository userRepository`
- `saveUser()` annotated with `@Transactional`
- Business logic: validates roles are non-empty, prefixes role names with "ROLE_", sets bidirectional `role.setUser(user)` reference, then persists

## Target Contracts

### DI Conversion (springboot-di-to-quarkus-00003)

Per MAPPINGS.md and architecture-profile §7:
- `@Service` → `@ApplicationScoped`
- `@Autowired` constructor → CDI constructor injection (no annotation needed)
- `@Autowired` field → constructor injection
- Spring `@Transactional` → `jakarta.transaction.Transactional`
- `@Cacheable(value = "vets")` → thread-safe caching with **ConcurrentHashMap** and **refresh-guard** (refresh only when key absent AND no refresh in last 60s)

### Thread-Safe Singleton State (architecture-profile §7)

All service instances are **stateless** and **thread-safe**:
- No shared mutable state
- Any caching must use **ConcurrentHashMap** with compute-based mutations
- All operations are stateless (no per-request instance state)

### Exception Handling

- `org.springframework.dao.DataAccessException` → use a dedicated application exception or Jakarta persistence equivalent
- `ObjectRetrievalFailureException` / `EmptyResultDataAccessException` → Quarkus/Hibernate equivalents
- `null` return on not-found (preserved from legacy behavior)

## Preserve Items

- `petclinic.security.enable` — security configuration preserved as Quarkus property
- `server.servlet.context-path` — context path preserved as `quarkus.http.root-path`

## Acceptance Path

The acceptance path `/petclinic/api/vets` is deferred to the deploy story per O-M3ACCEPT / S-AC1 / G-OK. This story does not task that path with a Java @Path/endpoint.

## Out of Scope

- REST controllers (S06)
- Security configuration (separate story)
- Swagger/OpenAPI configuration (separate story)
- Repository layer (S04, already migrated)
- Model entities (S02/S03, already migrated)

## Package Rename Rules

Full prefix replacement:
- `org.springframework.samples.petclinic.service.*` → `com.demo.service.*`
- All model imports: `org.springframework.samples.petclinic.model.*` → `com.demo.model.*`
- All repository imports: `org.springframework.samples.petclinic.repository.*` → `com.demo.repository.*`
