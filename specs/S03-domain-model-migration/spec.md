# S03: Domain Model Migration - Specification

## Overview

This specification documents the migration of all core domain entities and mapper interfaces from Spring PetClinic legacy application to Quarkus. These classes form the core domain boundary and are referenced by repositories, services, and controllers.

## Scope

### In-Scope Classes

**Domain Entities (HARVEST):**
- `org.springframework.samples.petclinic.model.Owner` → `com.demo.model.Owner`
- `org.springframework.samples.petclinic.model.Pet` → `com.demo.model.Pet`
- `org.springframework.samples.petclinic.model.Visit` → `com.demo.model.Visit`
- `org.springframework.samples.petclinic.model.Vet` → `com.demo.model.Vet`
- `org.springframework.samples.petclinic.model.Specialty` → `com.demo.model.Specialty`
- `org.springframework.samples.petclinic.model.PetType` → `com.demo.model.PetType`
- `org.springframework.samples.petclinic.model.User` → `com.demo.model.User`
- `org.springframework.samples.petclinic.model.Role` → `com.demo.model.Role`
- `org.springframework.samples.petclinic.model.BaseEntity` → `com.demo.model.BaseEntity`
- `org.springframework.samples.petclinic.model.NamedEntity` → `com.demo.model.NamedEntity`
- `org.springframework.samples.petclinic.model.Person` → `com.demo.model.Person`

**Mapper Interfaces (HARVEST):**
- `org.springframework.samples.petclinic.mapper.OwnerMapper` → `com.demo.mapper.OwnerMapper`
- `org.springframework.samples.petclinic.mapper.PetMapper` → `com.demo.mapper.PetMapper`
- `org.springframework.samples.petclinic.mapper.VisitMapper` → `com.demo.mapper.VisitMapper`
- `org.springframework.samples.petclinic.mapper.VetMapper` → `com.demo.mapper.VetMapper`
- `org.springframework.samples.petclinic.mapper.SpecialtyMapper` → `com.demo.mapper.SpecialtyMapper`
- `org.springframework.samples.petclinic.mapper.PetTypeMapper` → `com.demo.mapper.PetTypeMapper`
- `org.springframework.samples.petclinic.mapper.UserMapper` → `com.demo.mapper.UserMapper`

## Observed Legacy Behavior

### Domain Entity Structure

**Owner Entity** (src/main/java/org/springframework/samples/petclinic/model/Owner.java:36-53):
- Extends `Person` with address, city, telephone fields
- Uses `@NotEmpty`, `@Size`, `@Digits` validation constraints
- `@OneToMany` relationship with Pet (cascade = ALL, eager fetch)
- Custom getter methods for pets with sorted list behavior
- Business logic: `getPet(String name)` lookup with case-insensitive search

**Pet Entity** (src/main/java/org/springframework/samples/petclinic/model/Pet.java:34-50):
- Extends `NamedEntity` with birthDate, type (PetType), owner (Owner)
- `@ManyToOne` relationships with PetType and Owner
- `@OneToMany` relationship with Visit (cascade = ALL, eager fetch)
- Visits sorted chronologically by date in getter

**Visit Entity** (src/main/java/org/springframework/samples/petclinic/model/Visit.java:29-51):
- Extends `BaseEntity` with date (LocalDate), description, pet (Pet)
- `@NotEmpty` validation on description field
- Default constructor sets date to current date
- Many-to-one relationship with Pet

**Vet Entity** (src/main/java/org/springframework/samples/petclinic/model/Vet.java:34-41):
- Extends `Person` with many-to-many relationship to Specialty
- Eager fetch of specialties with sorted list behavior
- Business methods: `getNrOfSpecialties()`, `addSpecialty()`, `clearSpecialties()`

**Base Classes:**
- `BaseEntity`: Abstract base with Integer id, GenerationType.IDENTITY
- `NamedEntity`: Extends BaseEntity with String name
- `Person`: Extends NamedEntity with firstName, lastName fields

### Mapper Interface Contracts

**OwnerMapper** (src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java:12-22):
- MapStruct interface with PetMapper dependency
- Methods: `toOwnerDto(Owner)`, `toOwner(OwnerDto)`, collection variants

**PetMapper** (src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java:14-29):
- MapStruct interface without dependencies
- Methods for Pet/PetDto mapping and PetType/PetTypeDto mapping

All mapper interfaces use MapStruct `@Mapper` annotation and maintain exact method signatures.

## API Contracts

### Data Model Contracts

**JPA Relationship Contracts:**
- Owner → Pet: `@OneToMany(cascade = ALL, mappedBy = "owner")`
- Pet → Visit: `@OneToMany(cascade = ALL, mappedBy = "pet")`
- Pet → PetType: `@ManyToOne` with `@JoinColumn(name = "type_id")`
- Pet → Owner: `@ManyToOne` with `@JoinColumn(name = "owner_id")`
- Vet → Specialty: `@ManyToMany` with join table
- Visit → Pet: `@ManyToOne` with `@JoinColumn(name = "pet_id")`

**Validation Contracts:**
- Owner: `@NotEmpty` address, city, telephone; `@Digits` on telephone
- Visit: `@NotEmpty` description
- All constraints preserved exactly

**Collection Behavior Contracts:**
- Pets sorted by name (case-insensitive) using PropertyComparator
- Visits sorted chronologically by date
- Specialties sorted by name
- Collections returned as unmodifiable lists

## Legacy Evidence

All observed behaviors are evidenced by:
- Domain entity source files: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/model/*.java`
- Mapper interface source files: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/*.java`
- Existing OpenRewrite staging: `/projects/modernized/migration/staging/src/main/java/org/springframework/samples/petclinic/model/*.java`

## Out of Scope

- Repository implementations (handled in S04)
- Service layer components (handled in S05)
- REST controllers (handled in S06)
- Any business logic beyond data access patterns

## Behavioral Pins

**God-node entities** (architecture-profile §7) with highest fan-in:
- PetType: 18 references - must maintain exact legacy relationships
- Visit: 18 references - must maintain exact legacy relationships  
- Pet: 17 references - must maintain exact legacy relationships

**God-node characterization requirements:**
- PetType: Test extends NamedEntity, no additional fields
- Visit: Test extends BaseEntity with LocalDate and description fields
- Pet: Test extends NamedEntity with birthDate, type, owner, visits relationships

All entity relationships and validation constraints must be preserved exactly as in legacy code.

## Package Rename Rules

**Full prefix replacement:**
- `org.springframework.samples.petclinic.model.*` → `com.demo.model.*`
- `org.springframework.samples.petclinic.mapper.*` → `com.demo.mapper.*`

The `targetPackage` is `com.demo` (from migration.yaml), never `com.demo.petclinic` or similar specimens.
