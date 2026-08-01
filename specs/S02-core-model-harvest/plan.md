# S02: Core Model Harvest Plan

## Migration Strategy
This story performs a **HARVEST** operation on foundation classes that form the base entity hierarchy and utilities. All classes are converted using mechanical transformations with no architectural redesign decisions.

## Package Transformation
- **Source package**: `org.springframework.samples.petclinic`
- **Target package**: `com.demo` 
- **Transformation type**: Full prefix replacement (rewrite)
- **Scope**: All in-scope classes get package rename

## Jakarta EE Migration
- **javax.persistence.*** → **jakarta.persistence.***
- **javax.validation.*** → **jakarta.validation.***
- **javax.transaction.*** → **jakarta.transaction.***
- **javax.servlet.*** → **jakarta.servlet.***
- **Transformation type**: Import statement replacement (rewrite)
- **Coverage**: All JPA entities, validation constraints, and EE imports

## Class-Specific Mappings

### BaseEntity (HARVEST)
- **Target design**: → `src/main/java/com/demo/model/BaseEntity.java`
- **Shape**: structure
- **Actions**: 
  - Package rename: `org.springframework.samples.petclinic.model` → `com.demo.model`
  - Import migration: `javax.persistence.*` → `jakarta.persistence.*`
  - Field preservation: `protected Integer id` with IDENTITY generation
  - Method preservation: `getId()`, `setId()`, `isNew()`
  - Jackson annotation preservation: `@JsonIgnore`
- **Class**: rewrite
- **Dependencies**: Referenced by all domain entities

### NamedEntity (HARVEST)
- **Target design**: → `src/main/java/com/demo/model/NamedEntity.java`
- **Shape**: structure  
- **Actions**:
  - Package rename: `org.springframework.samples.petclinic.model` → `com.demo.model`
  - Import migration: `javax.persistence.*`, `javax.validation.*` → `jakarta.*`
  - Field preservation: `private String name` with @NotEmpty validation
  - Method preservation: `getName()`, `setName()`, `toString()`
  - Inheritance preservation: extends BaseEntity
- **Class**: rewrite
- **Dependencies**: Extends BaseEntity

### Person (HARVEST)  
- **Target design**: → `src/main/java/com/demo/model/Person.java`
- **Shape**: structure
- **Actions**:
  - Package rename: `org.springframework.samples.petclinic.model` → `com.demo.model`
  - Import migration: `javax.persistence.*`, `javax.validation.*` → `jakarta.*`
  - Field preservation: `firstName`, `lastName` with @NotEmpty validation
  - Method preservation: getters/setters for both name fields
  - Inheritance preservation: extends BaseEntity
- **Class**: rewrite
- **Dependencies**: Extends BaseEntity

### EntityUtils (HARVEST)
- **Target design**: → `src/main/java/com/demo/util/EntityUtils.java`
- **Shape**: structure
- **Actions**:
  - Package rename: `org.springframework.samples.petclinic.util` → `com.demo.util`
  - Import updates: `ObjectRetrievalFailureException` reference updates
  - Method preservation: `getById()` generic utility method
  - Exception handling preservation: throws `ObjectRetrievalFailureException`
  - Dependency updates: BaseEntity reference to new package
- **Class**: rewrite
- **Dependencies**: References BaseEntity

### BindingErrorsResponse (HARVEST)
- **Target design**: → `src/main/java/com/demo/rest/BindingErrorsResponse.java`
- **Shape**: structure
- **Actions**:
  - Package rename: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
  - Import updates: Spring validation references if needed
  - Field preservation: `List<BindingError> bindingErrors`
  - Method preservation: `addError()`, `addAllErrors()`, `toJSON()`
  - Nested class preservation: `BindingError` static inner class
- **Class**: rewrite
- **Dependencies**: Referenced by REST controllers

### PetClinicApplication (REDESIGN - DELETE)
- **Target design**: → removed (file deletion)
- **Shape**: remove
- **Actions**:
  - Complete file removal from project
  - No replacement needed - Quarkus auto-discovery replaces @SpringBootApplication
  - Remove Spring Boot bootstrap dependency
- **Class**: rewrite
- **Dependencies**: None (removal)

## Test Strategy
- **Characterization tests**: Port existing model validation tests to verify field structures
- **Package verification**: Verify correct package structure after rename
- **Import verification**: Confirm all javax.* imports converted to jakarta.*
- **Build verification**: Ensure project compiles successfully after transformations

## Build & Validation
- **Compilation**: All classes must compile without errors
- **Package integrity**: Verify no remaining references to legacy packages
- **Jakarta migration**: Confirm all javax.* imports successfully converted
- **Dependency verification**: Ensure all internal references updated

## Success Criteria
1. All base entity classes harvested with correct package structure
2. All javax.* imports successfully migrated to jakarta.*
3. EntityUtils utility class preserved with functional methods
4. BindingErrorsResponse DTO preserved with validation handling
5. PetClinicApplication removed (Quarkus auto-discovery enabled)
6. Project builds successfully without compilation errors
7. Package structure matches com.demo.* target convention