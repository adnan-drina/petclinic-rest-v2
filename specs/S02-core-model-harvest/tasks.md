# S02: Core Model Harvest Tasks

## Package Structure Setup

#### T-001: Create package directory structure with git placeholders
**Class**: rewrite  
**Shape**: structure  
**Findings**: N/A  
**Target design**: → `src/main/java/com/demo/model/`, `src/main/java/com/demo/util/`, `src/main/java/com/demo/rest/`  
**Actions**:
Create the target package directory structure to support the package rename from `org.springframework.samples.petclinic` to `com.demo`. Create directories: `src/main/java/com/demo/model/`, `src/main/java/com/demo/util/`, and `src/main/java/com/demo/rest/`. Add `.gitkeep` files to each directory to ensure git can commit empty directories.

## Base Entity Harvest (dependency order #1)

#### T-002: Harvest BaseEntity
**Class**: rewrite  
**Shape**: structure  
**Findings**: javax-to-jakarta-import-00001  
**Target design**: → `src/main/java/com/demo/model/BaseEntity.java`  
**Owns**: `src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`  
**Actions**:
Harvest BaseEntity with full package rename and Jakarta migration. Transform `org.springframework.samples.petclinic.model` to `com.demo.model`. Replace all `javax.persistence.*` imports with `jakarta.persistence.*` (lines 18-21). Preserve field structure: `protected Integer id` with `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`. Preserve methods: `getId()`, `setId()`, `isNew()` with `@JsonIgnore` annotation. Maintain exact behavioral contract for identity-based new entity detection.

## Binding Errors DTO Harvest (dependency order #2)

#### T-003: Harvest BindingErrorsResponse
**Class**: rewrite  
**Shape**: structure  
**Findings**: javax-to-jakarta-import-00001  
**Target design**: → `src/main/java/com/demo/rest/BindingErrorsResponse.java`  
**Owns**: `src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java`  
**Actions**:
Harvest BindingErrorsResponse with full package rename and import updates. Transform `org.springframework.samples.petclinic.rest` to `com.demo.rest`. Replace `javax.validation.*` imports with `jakarta.validation.*` (lines 22-23). Preserve field structure: `List<BindingError> bindingErrors`. Preserve methods: `addError()`, `addAllErrors(BindingResult)`, `toJSON()` with Jackson ObjectMapper. Preserve nested `BindingError` class with field validation details. Maintain exact behavioral contract for validation error aggregation and JSON serialization.

## Application Bootstrap Removal (dependency order #4)

#### T-004: Remove PetClinicApplication bootstrap
**Class**: rewrite  
**Shape**: remove  
**Findings**: springboot-annotations-to-quarkus-00002  
**Target design**: → removed  
**Absorbs**: `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`  
**Actions**:
Remove PetClinicApplication bootstrap class entirely as Quarkus auto-discovery replaces Spring Boot's `@SpringBootApplication`. Delete the file `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`. Remove Spring Boot application bootstrap dependency. No replacement needed as Quarkus will auto-discover beans and configuration.

## Infrastructure Classes Handling (dependency order #14)

#### T-005: Mark ApplicationSwaggerConfig as out of scope
**Class**: rewrite  
**Shape**: structure  
**Findings**: springboot-annotations-to-quarkus-00002  
**Target design**: → deferred to infrastructure story  
**Owns**: `src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java`  
**Actions**:
Mark ApplicationSwaggerConfig as out of scope for this story as it belongs to infrastructure modernization (Swagger/OpenAPI configuration) rather than core model harvest. This class will be handled in a subsequent infrastructure modernization story.

## Named Entity Harvest (dependency order #16)

#### T-006: Harvest NamedEntity
**Class**: rewrite  
**Shape**: structure  
**Findings**: javax-to-jakarta-import-00001  
**Target design**: → `src/main/java/com/demo/model/NamedEntity.java`  
**Owns**: `src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java`  
**Actions**:
Harvest NamedEntity with full package rename and Jakarta migration. Transform `org.springframework.samples.petclinic.model` to `com.demo.model`. Replace `javax.persistence.*` imports with `jakarta.persistence.*` (lines 18-19). Replace `javax.validation.constraints.NotEmpty` with `jakarta.validation.constraints.NotEmpty` (line 21). Preserve field structure: `private String name` with `@Column(name = "name")` and `@NotEmpty` validation. Preserve inheritance: extends BaseEntity. Preserve methods: `getName()`, `setName()`, `toString()` override.

## Entity Utility Harvest (dependency order #17)

#### T-007: Harvest EntityUtils
**Class**: rewrite  
**Shape**: structure  
**Findings**: javax-to-jakarta-import-00001  
**Target design**: → `src/main/java/com/demo/util/EntityUtils.java`  
**Owns**: `src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java`  
**Actions**:
Harvest EntityUtils with full package rename and dependency updates. Transform `org.springframework.samples.petclinic.util` to `com.demo.util`. Update import for `ObjectRetrievalFailureException` to use Spring framework equivalent. Update import for BaseEntity reference to point to new package `com.demo.model.BaseEntity`. Preserve static utility method: `getById(Collection<T> entities, Class<T> entityClass, int entityId)` with generic type bounds. Preserve exception handling: throws `ObjectRetrievalFailureException`. Maintain exact behavioral contract for entity lookup operations.

## Person Entity Harvest (dependency order #18)

#### T-008: Harvest Person  
**Class**: rewrite  
**Shape**: structure  
**Findings**: javax-to-jakarta-import-00001  
**Target design**: → `src/main/java/com/demo/model/Person.java`  
**Owns**: `src/main/java/org/springframework/samples/petclinic/model/Person.java`  
**Actions**:
Harvest Person with full package rename and Jakarta migration. Transform `org.springframework.samples.petclinic.model` to `com.demo.model`. Replace `javax.persistence.*` imports with `jakarta.persistence.*` (lines 18-19). Replace `javax.validation.constraints.NotEmpty` with `jakarta.validation.constraints.NotEmpty` (line 21). Preserve field structure: `protected String firstName`, `protected String lastName` with `@Column(name = "first_name")`, `@Column(name = "last_name")` and `@NotEmpty` validation. Preserve inheritance: extends BaseEntity. Preserve getter/setter methods for both name fields.

## Model Characterization Tests

#### T-009: Create characterization tests for harvested base entities
**Class**: rewrite  
**Shape**: structure  
**Findings**: N/A  
**Target design**: → `src/test/java/com/demo/model/BaseEntityTest.java`, `src/test/java/com/demo/model/NamedEntityTest.java`, `src/test/java/com/demo/model/PersonTest.java`  
**Actions**:
Create characterization tests for harvested base entities to verify behavioral contracts preserved exactly. BaseEntityTest: verify isNew() behavior with null/valid ID, verify getId/setId functionality. NamedEntityTest: verify name field validation, toString() returns name, extends BaseEntity correctly. PersonTest: verify firstName/lastName fields with validation, extends BaseEntity correctly. All tests verify fields have correct annotations and constraints after Jakarta migration. Run `mvn clean compile` after creating tests to verify all harvested classes compile successfully with package rename from `org.springframework.samples.petclinic` to `com.demo`. Verify no remaining `javax.*` imports in harvested files - all converted to `jakarta.*`. Document that preserved items `petclinic.security.enable` and `server.servlet.context-path` will be handled in subsequent infrastructure modernization story as they relate to security and web configuration, not core model harvest.

## Legacy UI Surface Waiver

**Waiver**: Legacy UI surface (web interface, user interactions) is explicitly out of scope for this core model harvest story. This story focuses exclusively on base entity classes, utilities, and DTOs that form the foundation for subsequent domain model and service layer work. No UI components, templates, or web interfaces are modified in this story.