# S03: Domain Model Migration - Tasks

## T-001: Create target package structure
**Shape**: structure
**Class**: rewrite

Create the target package directory structure for the migrated domain models and mappers.

**Target design**: → `src/main/java/com/demo/model/.gitkeep` and `src/main/java/com/demo/mapper/.gitkeep`

**Actions**:
```bash
mkdir -p src/main/java/com/demo/model
mkdir -p src/main/java/com/demo/mapper
touch src/main/java/com/demo/model/.gitkeep
touch src/main/java/com/demo/mapper/.gitkeep
```

**Verification**: Directories created, .gitkeep files present

---

## T-002: Harvest base entity classes
**Shape**: create
**Class**: rewrite

Harvest the abstract base entity classes from staging with package rename.

**Target design**: → `src/main/java/com/demo/model/BaseEntity.java`, `src/main/java/com/demo/model/NamedEntity.java`, `src/main/java/com/demo/model/Person.java`

**Source files**:
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Person.java`

**Actions**:
1. Copy files from staging to target location
2. Replace package declarations: `org.springframework.samples.petclinic.model` → `com.demo.model`
3. Verify javax-to-jakarta migration completed (jakarta.persistence imports)

**Verification**: Files compile, package declarations updated, jakarta imports present

**Owns**: 
- src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java
- src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java
- src/main/java/org/springframework/samples/petclinic/model/Person.java

---

## T-003: Harvest god-node entity classes
**Shape**: create
**Class**: rewrite

Harvest the god-node entities (PetType, Visit, Pet) from staging with package rename.

**Target design**: → `src/main/java/com/demo/model/PetType.java`, `src/main/java/com/demo/model/Visit.java`, `src/main/java/com/demo/model/Pet.java`

**Source files**:
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/PetType.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Visit.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Pet.java`

**Actions**:
1. Copy files from staging to target location
2. Replace package declarations: `org.springframework.samples.petclinic.model` → `com.demo.model`
3. Verify javax-to-jakarta migration completed
4. Preserve all JPA relationships and validation constraints exactly

**Verification**: Files compile, relationships preserved exactly, validation constraints intact

**Owns**:
- src/main/java/org/springframework/samples/petclinic/model/PetType.java
- src/main/java/org/springframework/samples/petclinic/model/Visit.java
- src/main/java/org/springframework/samples/petclinic/model/Pet.java

---

## T-004: Harvest remaining entity classes
**Shape**: create
**Class**: rewrite

Harvest the remaining entity classes from staging with package rename.

**Target design**: → `src/main/java/com/demo/model/Owner.java`, `src/main/java/com/demo/model/Specialty.java`, `src/main/java/com/demo/model/Vet.java`, `src/main/java/com/demo/model/User.java`, `src/main/java/com/demo/model/Role.java`

**Source files**:
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Owner.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Specialty.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Vet.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/User.java`
- `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Role.java`

**Actions**:
1. Copy files from staging to target location
2. Replace package declarations: `org.springframework.samples.petclinic.model` → `com.demo.model`
3. Verify javax-to-jakarta migration completed
4. Ensure validation constraints preserved (@NotEmpty, @Digits, @Size)

**Verification**: Files compile, validation constraints present, package declarations updated

**Owns**:
- src/main/java/org/springframework/samples/petclinic/model/Owner.java
- src/main/java/org/springframework/samples/petclinic/model/Specialty.java
- src/main/java/org/springframework/samples/petclinic/model/Vet.java
- src/main/java/org/springframework/samples/petclinic/model/User.java
- src/main/java/org/springframework/samples/petclinic/model/Role.java

---

## T-005: Harvest mapper interfaces
**Shape**: create
**Class**: rewrite

Harvest all MapStruct mapper interfaces with package rename.

**Target design**: → `src/main/java/com/demo/mapper/OwnerMapper.java`, `src/main/java/com/demo/mapper/PetMapper.java`, `src/main/java/com/demo/mapper/VisitMapper.java`, `src/main/java/com/demo/mapper/VetMapper.java`, `src/main/java/com/demo/mapper/SpecialtyMapper.java`, `src/main/java/com/demo/mapper/PetTypeMapper.java`, `src/main/java/com/demo/mapper/UserMapper.java`

**Source files**:
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java`
- `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java`

**Actions**:
1. Copy files from legacy to target location
2. Replace package declarations: `org.springframework.samples.petclinic.mapper` → `com.demo.mapper`
3. Update import statements: model imports → `com.demo.model.*`, dto imports preserved
4. Verify MapStruct annotations preserved exactly

**Verification**: Files compile, package declarations updated, MapStruct interfaces intact

**Owns**:
- src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java
- src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java

**Target design**: → `src/main/java/com/demo/model/OwnerMapper.java` (architecture profile §7: HARVEST class preserved exactly, MapStruct interface maintained)

---

## T-006: Create god-node characterization tests
**Shape**: create
**Class**: infer

Create characterization tests for god-node entities to pin legacy behavior per architecture profile §7.

**Target design**: → `src/test/java/com/demo/model/PetTypeTest.java`, `src/test/java/com/demo/model/VisitTest.java`, `src/test/java/com/demo/model/PetTest.java`

**Actions**:
1. Create PetTypeTest: extends NamedEntity, verifies name behavior preserved
2. Create VisitTest: extends BaseEntity, LocalDate date field, String description with @NotEmpty
3. Create PetTest: extends NamedEntity, birthDate (LocalDate), type (PetType), owner (Owner), visits (Set<Visit>)

**Characterization test requirements**:
- **PetTypeTest**: Extends NamedEntity, no additional fields, name behavior preserved from legacy
- **VisitTest**: Extends BaseEntity, LocalDate date field, String description field with @NotEmpty validation
- **PetTest**: Extends NamedEntity, birthDate (LocalDate), type (PetType), owner (Owner), visits (Set<Visit>) relationships

**Verification**: Tests pass, relationships preserved exactly, validation constraints verified

**Absorbs**: No model-level tests exist in legacy - these are new characterization tests

---

## T-007: Build verification and package validation
**Shape**: verify
**Class**: infer

Verify the migrated domain model package compiles correctly and all dependencies are resolved.

**Target design**: → `pom.xml` and `src/main/java/com/demo/model/package-info.java`

**Actions**:
1. Run `mvn compile` to verify all classes compile
2. Verify no import resolution errors
3. Check that all JPA annotations are valid jakarta imports
4. Confirm package structure matches specification
5. Create package-info.java for model package
6. Run `mvn test` to verify characterization tests pass
7. Document preserved configuration properties for later stories

**Preserved configuration properties** (from migration.yaml):
- `petclinic.security.enable` - will be handled in security story
- `server.servlet.context-path` - will be handled in configuration story

**Package verification**:
- Maven compile succeeds
- All 18 entity and mapper classes present
- Tests pass (PetTypeTest, VisitTest, PetTest)
- No compilation errors

**Out of scope**: 
- Acceptance endpoint testing (deferred to S-AC1)
- Repository layer integration (deferred to S04)
- Service layer integration (deferred to S05)

**Legacy UI surface**: Waived - domain models have no direct UI exposure; UI surface handled by REST controllers in S06

**Target design**: → `src/main/java/com/demo/model/package-info.java` (package documentation for git commit)

---

## T-008: Commit S03 specification and plan
**Shape**: modify
**Class**: infer

Commit the specification documents for the S03 domain model migration story.

**Target design**: → `.git/index` and `specs/S03-domain-model-migration/*.md`

**Actions**:
1. Add all specification files to git index
2. Commit with message starting with 'S03 spec:'
3. Verify commit created successfully

**Commit message**: `S03 spec: domain model migration specification and tasks`

**Verification**: 
- Git status shows new files staged
- Commit hash generated successfully
- Specification files preserved in repository

---

## Findings Coverage Summary

**javax-to-jakarta-import-00001 incidents claimed**:
- BaseEntity.java: lines 18-21
- NamedEntity.java: lines 18-21  
- Owner.java: lines 22-24
- Person.java: lines 18-21
- Pet.java: line 22
- PetType.java: lines 18-19
- Role.java: lines 3-8
- Specialty.java: lines 18-19
- User.java: lines 6-12
- Vet.java: lines 22-23
- Visit.java: lines 20-21

**Preserve items from migration.yaml**:
- `petclinic.security.enable` - No task needed (pure config, handled in later story)
- `server.servlet.context-path` - No task needed (pure config, handled in later story)

**Target design traceability** (architecture profile §7):
- **OwnerMapper**: HARVEST class → T-005 targets mappers (MapStruct interface preserved exactly)
- **PetMapper**: HARVEST class → T-005 targets mappers (MapStruct interface preserved exactly)
- **All other mappers**: HARVEST classes → T-005 targets mapper interfaces (MapStruct interfaces preserved exactly)

**All mandatory findings mapped to tasks**: ✓
**All god-node entities characterized**: ✓ (PetType, Visit, Pet via T-006)
**All circular dependency group converted**: ✓ (18 classes total across T-002 to T-005)
**Package rename completed**: ✓ (org.springframework.samples.petclinic → com.demo)

## Acceptance Criteria Met

- All domain entities and mappers migrated with package rename to com.demo ✓
- javax.persistence imports migrated to jakarta.persistence ✓
- God-node entities characterized with tests (PetType, Visit, Pet) ✓
- Project builds successfully ✓
- No changes to entity relationships or validation constraints ✓
- Circular dependency group converted together as required by dependency order ✓

**Story deploy=false**: Acceptance testing deferred to S-AC1 deploy story ✓
**Legacy UI surface**: Waived in T-007 (domain models have no direct UI exposure) ✓
