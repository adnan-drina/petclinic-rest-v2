# S03: Domain Model Migration - Plan

## Findings Mapping

### javax-to-jakarta-import-00001 [RECIPE - ALREADY EXECUTED]
- **Status**: OpenRewrite recipe executed in M1
- **Target**: jakarta.* imports
- **Impact**: All javax.persistence, javax.validation imports already migrated
- **Action**: None required - harvest from staging directory

### Package Rename Mapping

**Rule**: Full prefix replacement `org.springframework.samples.petclinic` → `com.demo`

#### Domain Entities (HARVEST - rewrite)
**Legacy Path**: `org.springframework.samples.petclinic.model.*`
**Target Path**: `com.demo.model.*`

1. **Owner** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Owner.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Owner.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

2. **Pet** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Pet.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Pet.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

3. **Visit** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Visit.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Visit.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

4. **Vet** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Vet.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Vet.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

5. **Specialty** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Specialty.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Specialty.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

6. **PetType** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/PetType.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/PetType.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

7. **User** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/User.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/User.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

8. **Role** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/Role.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Role.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

9. **BaseEntity** (rewrite)
   - **Target design**: → `src/main/java/com/demo/model/BaseEntity.java`
   - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`
   - **Findings**: javax-to-jakarta-import-00001 (already applied)

10. **NamedEntity** (rewrite)
    - **Target design**: → `src/main/java/com/demo/model/NamedEntity.java`
    - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java`
    - **Findings**: javax-to-jakarta-import-00001 (already applied)

11. **Person** (rewrite)
    - **Target design**: → `src/main/java/com/demo/model/Person.java`
    - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/Person.java`
    - **Findings**: javax-to-jakarta-import-00001 (already applied)

12. **package-info** (rewrite)
    - **Target design**: → `src/main/java/com/demo/model/package-info.java`
    - **Source**: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/package-info.java`
    - **Findings**: None

#### Mapper Interfaces (HARVEST - rewrite)

13. **OwnerMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/OwnerMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

14. **PetMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/PetMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

15. **VisitMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/VisitMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

16. **VetMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/VetMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

17. **SpecialtyMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/SpecialtyMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

18. **PetTypeMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/PetTypeMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

19. **UserMapper** (rewrite)
    - **Target design**: → `src/main/java/com/demo/mapper/UserMapper.java`
    - **Source**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java`
    - **Findings**: javax-to-jakarta-import-00001 (if any imports exist)

## Package Structure

### Target Directory Structure
```
src/main/java/com/demo/
├── model/
│   ├── BaseEntity.java (HARVEST)
│   ├── NamedEntity.java (HARVEST)
│   ├── Person.java (HARVEST)
│   ├── Owner.java (HARVEST)
│   ├── Pet.java (HARVEST)
│   ├── Visit.java (HARVEST)
│   ├── Vet.java (HARVEST)
│   ├── Specialty.java (HARVEST)
│   ├── PetType.java (HARVEST)
│   ├── User.java (HARVEST)
│   ├── Role.java (HARVEST)
│   └── package-info.java (HARVEST)
└── mapper/
    ├── OwnerMapper.java (HARVEST)
    ├── PetMapper.java (HARVEST)
    ├── VisitMapper.java (HARVEST)
    ├── VetMapper.java (HARVEST)
    ├── SpecialtyMapper.java (HARVEST)
    ├── PetTypeMapper.java (HARVEST)
    └── UserMapper.java (HARVEST)
```

## Circular Dependency Handling

All domain entities and mappers form a circular dependency group (dependency-order.md:42-56) and must be converted together in one task to maintain compilation integrity. This includes:

- All 11 entity classes (Owner, Pet, Visit, Vet, Specialty, PetType, User, Role, BaseEntity, NamedEntity, Person)
- All 7 mapper interfaces (OwnerMapper, PetMapper, VisitMapper, VetMapper, SpecialtyMapper, PetTypeMapper, UserMapper)

## Testing Strategy

### God-Node Characterization (infer)
Architecture profile §7 identifies god-node entities that require characterization tests:

- **PetType**: 18 references - test extends NamedEntity behavior
- **Visit**: 18 references - test extends BaseEntity with LocalDate/description
- **Pet**: 17 references - test extends NamedEntity with relationships

### Test Coverage Requirements
- All entities: Constructor, getter/setter behavior
- Relationship integrity: Bidirectional relationships (Owner↔Pet, Pet↔Visit)
- Validation constraints: @NotEmpty, @Digits, @Size
- Collection sorting: Pets by name, visits by date, specialties by name

## Conversion Order (dependency-order.md)

1. **Base Classes First**: BaseEntity, NamedEntity, Person
2. **God Nodes Next**: PetType, Visit, Pet (with characterization tests)
3. **Related Entities**: Owner, Specialty, Vet, User, Role
4. **Mapper Interfaces**: All mappers as a group

This ensures compilation at each step and proper dependency resolution.

## Acceptance Path

**Story deploy flag**: false
**Acceptance path**: `/petclinic/api/vets` (deferred to S-AC1 deploy story)
**Action**: No endpoint tasks required in this story

## Preserve Items

**From migration.yaml**:
- None - these are pure model classes with no configuration surfaces

**Behavioral preservation**:
- All JPA relationships preserved exactly
- All validation constraints preserved exactly
- All collection sorting behavior preserved exactly
- All business logic methods preserved exactly

## Forbidden Changes

- No entity relationship modifications
- No validation constraint changes
- No business logic alterations
- No package structure changes beyond rename
