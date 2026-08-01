# S02: Core Model Harvest Specification

## Goal & Position
Harvest the god-node foundation classes with highest fan-in that are referenced by all other domain classes. These classes form the base of the entity hierarchy and utilities. This story enables all subsequent model and service work by providing the foundational entities and utilities that other classes depend on.

## In-Scope Legacy Classes

### Base Entity Classes (HARVEST)

**BaseEntity** (`src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java:18-21`)
```java
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    @JsonIgnore
    public boolean isNew() { return this.id == null; }
}
```
- **Legacy imports**: `javax.persistence.*` (lines 18-21)
- **Field structure**: `protected Integer id` with IDENTITY generation strategy
- **Jackson annotation**: `@JsonIgnore` on `isNew()` method
- **Behavioral contract**: Identity-based new entity detection

**NamedEntity** (`src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java:18-21`)
```java
@MappedSuperclass
public class NamedEntity extends BaseEntity {
    @Column(name = "name")
    @NotEmpty
    private String name;
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    
    @Override
    public String toString() { return this.getName(); }
}
```
- **Legacy imports**: `javax.persistence.*` (lines 18-19), `javax.validation.constraints.NotEmpty` (line 21)
- **Field structure**: Extends BaseEntity, adds `private String name` with @NotEmpty validation
- **Behavioral contract**: Name-based toString() override

**Person** (`src/main/java/org/springframework/samples/petclinic/model/Person.java:18-21`)
```java
@MappedSuperclass
public class Person extends BaseEntity {
    @Column(name = "first_name")
    @NotEmpty
    protected String firstName;
    
    @Column(name = "last_name")
    @NotEmpty
    protected String lastName;
    
    // getters/setters for firstName, lastName
}
```
- **Legacy imports**: `javax.persistence.*` (lines 18-19), `javax.validation.constraints.NotEmpty` (line 21)
- **Field structure**: Extends BaseEntity, adds `firstName` and `lastName` with @NotEmpty validation
- **Behavioral contract**: Separate first/last name fields with validation

### Utility Classes (HARVEST)

**EntityUtils** (`src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java:16-23`)
```java
public abstract class EntityUtils {
    public static <T extends BaseEntity> T getById(Collection<T> entities, Class<T> entityClass, int entityId)
        throws ObjectRetrievalFailureException {
        for (T entity : entities) {
            if (entity.getId() == entityId && entityClass.isInstance(entity)) {
                return entity;
            }
            }
        throw new ObjectRetrievalFailureException(entityClass, entityId);
    }
}
```
- **Legacy imports**: `org.springframework.orm.ObjectRetrievalFailureException` (line 21), `org.springframework.samples.petclinic.model.BaseEntity` (line 22)
- **Behavioral contract**: Generic utility method for entity lookup by ID with exception on not found
- **Dependencies**: References BaseEntity class, Pet class (line 43 in legacy file)

### DTO Classes (HARVEST)

**BindingErrorsResponse** (`src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java:17-29`)
```java
public class BindingErrorsResponse {
    private final List<BindingError> bindingErrors = new ArrayList<BindingError>();
    
    public void addError(BindingError bindingError) { this.bindingErrors.add(bindingError); }
    public void addAllErrors(BindingResult bindingResult) { /* field error conversion */ }
    public String toJSON() { /* Jackson ObjectMapper serialization */ }
    
    protected static class BindingError {
        private String objectName, fieldName, fieldValue, errorMessage;
        // setters and toString()
    }
}
```
- **Legacy imports**: `org.springframework.validation.BindingResult` (line 22), `org.springframework.validation.FieldError` (line 23), Jackson imports (lines 25-28)
- **Behavioral contract**: Validation error aggregation and JSON serialization
- **Nested class**: `BindingError` with field validation details

### Bootstrap Class (REDESIGN)

**PetClinicApplication** (`src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:1-8`)
```java
@SpringBootApplication
public class PetClinicApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
}
```
- **Legacy imports**: `org.springframework.boot.SpringApplication` (line 3), `org.springframework.boot.autoconfigure.SpringBootApplication` (line 4), `org.springframework.boot.web.servlet.support.SpringBootServletInitializer` (line 5)
- **Behavioral contract**: Spring Boot application bootstrap (to be removed)

## Package Rename Mapping
- **Source**: `org.springframework.samples.petclinic.*` 
- **Target**: `com.demo.*`
- **Scope**: Full prefix replacement for all in-scope classes

## API Contract Preservation
- All HARVEST classes preserve exact field structures and validation constraints
- All HARVEST classes preserve exact behavioral contracts (equals, toString, utility methods)
- JPA annotations preserved (migrated from javax.persistence.* to jakarta.persistence.*)
- Validation annotations preserved (migrated from javax.validation.* to jakarta.validation.*)

## Dependencies & References
- **BaseEntity**: Referenced by all domain entities (Owner, Pet, Visit, Vet, etc.)
- **EntityUtils**: Referenced by repository implementations for entity lookup operations  
- **BindingErrorsResponse**: Referenced by REST controllers for validation error handling
- **Package dependencies**: All classes reference legacy package structure internally

## Out-of-Scope
All domain entities (Owner, Pet, Visit, etc.), repositories, services, and REST controllers. These depend on the base entities and will be handled in subsequent stories S03-S06.