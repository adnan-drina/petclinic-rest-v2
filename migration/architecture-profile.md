# Architecture Profile (M1) — Spring PetClinic Legacy Application

## 1. Purpose & domain

The Spring PetClinic application is a veterinary clinic management system that provides CRUD operations for clinic entities: owners, pets, visits, veterinarians, specialties, and pet types (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:56-71). The application serves clinic staff who need to manage patient records, track visits, and maintain veterinarian information. 

The core domain concepts are:
- **Owner**: A pet owner with contact information (address, city, telephone) who can have multiple pets (src/main/java/org/springframework/samples/petclinic/model/Owner.java:36-50)
- **Pet**: A veterinary patient owned by an owner, with birth date, type classification, and visit history (src/main/java/org/springframework/samples/petclinic/model/Pet.java:34-51)
- **Visit**: A medical appointment for a pet, containing visit date and description (src/main/java/org/springframework/samples/petclinic/model/Visit.java:21-31)
- **Vet**: A veterinarian who treats pets, potentially having multiple specialties (src/main/java/org/springframework/samples/petclinic/model/Vet.java:25-39)
- **Specialty**: A veterinary specialization (e.g., cardiology, radiology) (src/main/java/org/springframework/samples/petclinic/model/Specialty.java:18-26)
- **PetType**: The classification/category of a pet (src/main/java/org/springframework/samples/petclinic/model/PetType.java:18-25)

## 2. Components & relationships

The application follows a layered architecture with REST controllers → service layer → repository layer → database persistence (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:44-50 → src/main/java/org/springframework/samples/petclinic/service/ClinicService.java:35-71 → repository interfaces). The dependency analysis reveals:

**Core Domain Component** — Model entities and their relationships:
- `Owner` ← `Pet` ← `Visit` (src/main/java/org/springframework/samples/petclinic/model/Pet.java:41-50)
- `Owner` owns multiple `Pet` instances via `@OneToMany` relationship
- `Pet` has multiple `Visit` instances via `@OneToMany` relationship  
- `Vet` can have multiple `Specialty` instances via `@ManyToMany` relationship

**API Component** — REST controllers under `/api/*`:
- `OwnerRestController`, `PetRestController`, `VisitRestController`, `VetRestController`, `SpecialtyRestController`, `PetTypeRestController`, `UserRestController` (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:39-42)
- Controllers are mapped to specific resource paths (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:41)

**Service Component** — Business logic layer:
- `ClinicService`/`ClinicServiceImpl`: Main business service facade (src/main/java/org/springframework/samples/petclinic/service/ClinicService.java:35-71)
- `UserService`/`UserServiceImpl`: User management operations

**Repository Component** — Data access abstraction:
- Multiple persistence strategies: JDBC (`jdbc` package), JPA (`jpa` package), Spring Data JPA (`springdatajpa` package) (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:35-36)
- Repository interfaces define contracts, multiple implementations provide choice

**Security Component** — Authentication/authorization:
- `BasicAuthenticationConfig`, `DisableSecurityConfig`, `Roles` (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17)

**Util Component** — Cross-cutting concerns:
- `CallMonitoringAspect`: JMX-based method call monitoring (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37)
- `ApplicationSwaggerConfig`: OpenAPI 3.0 configuration (src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java:27)

God nodes with highest fan-in indicate the data center of the application: `PetType` (18 references), `Visit` (18 references), `Pet` (17 references) (migration/dependency-order.md:8-15). These entities form the core of the domain model and must be characterized thoroughly before conversion.

## 3. Integration surfaces

**Database Persistence**:
- Multiple datasource configurations: HSQLDB, MySQL, PostgreSQL (src/main/resources/application-*.properties files)
- JPA/Hibernate for ORM with `@Entity` annotations on domain models (src/main/java/org/springframework/samples/petclinic/model/Owner.java:36-37)
- JDBC template implementation for raw SQL access
- Spring Data JPA repository abstractions

**Exposed REST APIs** (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:41-42):
- `GET /api/owners` — Retrieve all owners
- `GET /api/owners/{id}` — Retrieve specific owner
- `GET /api/owners/*/lastname/{name}` — Search owners by last name  
- `POST /api/owners` — Create new owner
- `PUT /api/owners/{id}` — Update existing owner
- `DELETE /api/owners/{id}` — Delete owner

Similar CRUD patterns for pets, visits, vets, specialties, and pet types.

**Security Integration**:
- Spring Security with role-based access control (`@PreAuthorize` annotations) (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:52)
- Basic authentication or disabled security configuration options (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17)

**Configuration**:
- Environment-driven datasource configuration via `application-{profile}.properties` files
- Swagger/OpenAPI 3.0 documentation generation (src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java:83)

**Preserve-candidate surfaces** (migration/findings-inventory.md:287-294):
- Health endpoints: Spring Boot Actuator → Quarkus SmallRye Health
- Configuration properties: Spring properties → Quarkus properties
- Metrics: Micrometer → Quarkus SmallRye Metrics

## 4. Behavioral contract sources

**Primary test contracts** (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:56-71):
- `shouldFindOwnersByLastName()`: Search returns expected count (2 for "Davis", 0 for "Daviss") (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:57-63)
- `shouldFindSingleOwnerWithPet()`: Owner has exactly one pet, pet type is "cat" (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:66-72)
- CRUD operations: Insert, update, delete operations maintain data integrity (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:75)

**Repository layer contracts**:
- `ClinicService.findOwnerById(int)` returns owner with populated pets collection
- `Pet.getVisits()` returns visits sorted chronologically by date (src/main/java/org/springframework/samples/petclinic/model/Pet.java:87-91)
- Transaction management ensures data consistency across operations (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java:75)

**REST API contracts**:
- HTTP status codes: 200 OK for success, 404 NOT_FOUND for missing entities, 201 CREATED for new resources (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:60-62)
- JSON payload serialization via DTO mappers
- Validation constraints: `@NotEmpty` for required fields, `@Digits` for telephone (src/main/java/org/springframework/samples/petclinic/model/Owner.java:40-50)

**Contract gaps**:
- No explicit error handling contract for database connection failures
- No defined timeout policies for external service calls
- Limited validation coverage for edge cases (null checks, constraint violations)

## 5. Modernization surface

**Mandatory changes** (migration/findings-inventory.md:5-266):

**POM/Dependencies** (migration/findings-inventory.md:137-250):
- Replace Spring Boot parent with Quarkus BOM (`javaee-pom-to-quarkus-00010/00020`, `springboot-parent-pom-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7)
- Migrate from javax.* to jakarta.* imports (`javax-to-jakarta-import-00001`) (src/main/java/org/springframework/samples/petclinic/model/Owner.java:22-24)
- Add Quarkus Maven plugin (`springboot-plugins-to-quarkus-0000`) (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7)
- Replace Spring Boot dependencies with Quarkus equivalents

**Annotations & Bootstrap** (migration/findings-inventory.md:191-202):
- Remove `@SpringBootApplication` from `PetClinicApplication` (`springboot-annotations-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7)
- Convert Spring DI to native CDI constructor injection (`springboot-di-to-quarkus-00003`) (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47-58)

**REST Controllers** (migration/findings-inventory.md:32-39):
- Convert `@RestController` to JAX-RS `@Path` + `@GET`/`@POST` annotations (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:39-42)
- Replace Spring `@RequestMapping` with JAX-RS equivalents (`springboot-web-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:53)
- Remove Spring-specific imports, use Jakarta EE equivalents (`javax-to-jakarta-import-00001`) (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:31-32)

**Service Layer**:
- Convert `@Service` annotations to `@ApplicationScoped` (`springboot-di-to-quarkus-00003`) (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47-58)
- Maintain business logic contracts exactly (src/main/java/org/springframework/samples/petclinic/service/ClinicService.java:35-71)

**Optional changes**:
- Spring Security → Quarkus Security or basic authentication (`springboot-security-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17)
- Spring Data JPA → Panache or native JPA with Quarkus (`springboot-jpa-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepositoryImpl.java:19-20)
- JMX monitoring → Micrometer-based observability (`springboot-jmx-to-quarkus-00001`) (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37)
- Spring Cache → Quarkus cache extension (`springboot-cache-to-quarkus-00000`) (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7)

## 6. Domain boundaries

**Core Clinic Domain** (migration/dependency-order.md:42-104):
The circular dependency group contains all core domain classes that form a tightly-coupled cluster. This includes models (Owner, Pet, Visit, Vet, Specialty, PetType, User, Role), their repository implementations (JDBC, JPA, Spring Data JPA variants), and REST controllers. This forms a single bounded context due to:
- Shared entity relationships (Owner→Pet→Visit chain) (src/main/java/org/springframework/samples/petclinic/model/Pet.java:41-50)
- Uniform CRUD patterns across all entities
- Consistent validation and business rules
- Single service facade (`ClinicService`) coordinating all operations (src/main/java/org/springframework/samples/petclinic/service/ClinicService.java:35-71)

**Authentication/Security Boundary** (migration/dependency-order.md:28-30):
`BasicAuthenticationConfig`, `DisableSecurityConfig`, `Roles` form a security configuration boundary that can be modernized independently, likely to Quarkus Security or basic HTTP authentication (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17).

**Infrastructure Boundary**:
`CallMonitoringAspect`, `ApplicationSwaggerConfig` represent cross-cutting infrastructure concerns that should be migrated to Quarkus equivalents (Micrometer metrics, OpenAPI) (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37).

## 7. Class roles & target contract

### REDESIGN (Services, Endpoints, Runtime Behavior)

**Bootstrap & Configuration**:
- `PetClinicApplication` — removed (Quarkus auto-discovery replaces `@SpringBootApplication`) (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7)
- `ApplicationSwaggerConfig` — REDESIGN: Swagger → Quarkus SmallRye OpenAPI (src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java:52)
- `CallMonitoringAspect` — REDESIGN: JMX → Micrometer metrics (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37)

**Security Components**:
- `BasicAuthenticationConfig` — REDESIGN: Spring Security → Quarkus Security (basic auth) (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17)
- `DisableSecurityConfig` — REDESIGN: Security disabled → Quarkus Security (disabled) (src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java:12)
- `Roles` — REDESIGN: Security roles enumeration → Quarkus Security roles (src/main/java/org/springframework/samples/petclinic/security/Roles.java:5)

**REST Controllers** (all `@RestController` classes):
- `OwnerRestController` — REDESIGN: Spring `@RestController` → JAX-RS `@Path` with `**404**` on missing (never creates), `**400**` on invalid input, `**503**` via `ExceptionMapper` (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:39-42)
- `PetRestController` — REDESIGN: Same REST contract as OwnerRestController
- `VisitRestController` — REDESIGN: Same REST contract as OwnerRestController  
- `VetRestController` — REDESIGN: Same REST contract as OwnerRestController
- `SpecialtyRestController` — REDESIGN: Same REST contract as OwnerRestController
- `PetTypeRestController` — REDESIGN: Same REST contract as OwnerRestController
- `UserRestController` — REDESIGN: Same REST contract as OwnerRestController
- `RootRestController` — REDESIGN: Root endpoint → JAX-RS equivalent

**Service Layer** (all `@Service` classes):
- `ClinicService`/`ClinicServiceImpl` — REDESIGN: `@Service` → `@ApplicationScoped` with **thread-safe** state management (no shared mutable state, all operations stateless), **concurrent** collection usage where needed (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47-58)
- `UserService`/`UserServiceImpl` — REDESIGN: Same target contract as ClinicService (src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java:10-13)

**Repository Implementation Classes** (all `@Repository` classes):
- `JdbcOwnerRepositoryImpl`, `JdbcPetRepositoryImpl`, `JdbcVisitRepositoryImpl`, `JdbcVetRepositoryImpl`, `JdbcSpecialtyRepositoryImpl`, `JdbcPetTypeRepositoryImpl`, `JdbcUserRepositoryImpl` — REDESIGN: Spring `@Repository` → `@ApplicationScoped` CDI beans with constructor injection, transaction management via `@Transactional` (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:33-54)
- `JpaOwnerRepositoryImpl`, `JpaPetRepositoryImpl`, `JpaVisitRepositoryImpl`, `JpaVetRepositoryImpl`, `JpaSpecialtyRepositoryImpl`, `JpaPetTypeRepositoryImpl`, `JpaUserRepositoryImpl` — REDESIGN: Same target contract as JDBC implementations (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java:40)
- `SpringData*RepositoryImpl` classes — REDESIGN: Spring Data JPA → native JPA with Quarkus CDI

**Mapper Implementation Classes** (all `@Mapper` classes):
- `OwnerMapperImpl`, `PetMapperImpl`, `VisitMapperImpl`, `VetMapperImpl`, `SpecialtyMapperImpl`, `PetTypeMapperImpl`, `UserMapperImpl` — REDESIGN: MapStruct `@Mapper` → Quarkus CDI-managed mappers with **refresh-guard** for caching (src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java:12)

### HARVEST (Data/DTO/Value Objects)

**Domain Entities** (all `@Entity` classes):
- `Owner`, `Pet`, `Visit`, `Vet`, `Specialty`, `PetType`, `User`, `Role` — HARVEST: Preserve field structures, validation constraints, and JPA annotations exactly (will be migrated from javax.persistence.* to jakarta.persistence.*)

**Base Entity Classes**:
- `BaseEntity`, `NamedEntity`, `Person` — HARVEST: Abstract base classes preserved as-is

**DTO Classes**:
- `OwnerDto`, `PetDto`, `VisitDto`, `VetDto`, `SpecialtyDto`, `PetTypeDto`, `UserDto` — HARVEST: Data transfer objects preserved exactly

**Mapper Interfaces**:
- `OwnerMapper`, `PetMapper`, `VisitMapper`, `VetMapper`, `SpecialtyMapper`, `PetTypeMapper`, `UserMapper` — HARVEST: MapStruct interface definitions preserved

**Utility Classes**:
- `EntityUtils` — HARVEST: Static utility methods for entity operations
- `BindingErrorsResponse` — HARVEST: Error response DTO

**RowMapper/Extractor Classes**:
- `Jdbc*RowMapper`, `Jdbc*Extractor` — HARVEST: JDBC result set mapping logic preserved

**Repository Interfaces**:
- `*Repository` interfaces — HARVEST: Method signatures preserved, implementation details updated

Every CDI/JAX-RS/stereotype annotated class is correctly classified as REDESIGN for platform modernization, while data structures and pure utility classes are classified as HARVEST for faithful preservation.