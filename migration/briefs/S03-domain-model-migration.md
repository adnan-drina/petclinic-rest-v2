# S03: Domain Model Migration

## Goal & position

Modernize all core domain entities and their mapper interfaces as a single circular dependency group. This story must convert all models together due to their interdependencies through JPA relationships and shared CRUD patterns. These classes form the core domain boundary per the architecture profile and are referenced by repositories, services, and controllers.

## In scope

The exact legacy classes/files this story modernizes:

- Domain entities and their relationships:
  ```java
  package org.springframework.samples.petclinic.model;

  import javax.persistence.*;
  import javax.validation.constraints.Digits;
  import javax.validation.constraints.NotEmpty;

  @Entity
  @Table(name = "owners")
  public class Owner extends Person {
      @NotEmpty
      @Size(max = 80)
      private String address;
      
      @NotEmpty
      @Size(max = 40)
      private String city;
      
      @NotEmpty
      @Digits(fraction = 0, integer = 10)
      private String telephone;
      
      @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
      private Set<Pet> pets;
  }
  ```

- Mapper interfaces:
  ```java
  package org.springframework.samples.petclinic.mapper;

  import org.springframework.samples.petclinic.model.Owner;
  import org.springframework.samples.petclinic.dto.OwnerDto;

  @Mapping(target = "pets", source = "pets")
  OwnerDto toOwnerDto(Owner owner);

  @Mapping(target = "pets", source = "pets")
  Owner toOwner(OwnerDto ownerDto);
  ```

## Out of scope

Repository implementations, service layer, and REST controllers. These depend on the domain models and will be handled in S04-S06.

## Class roles & target contract

For each in-scope class, its role and target contract:

- All domain entities (`Owner`, `Pet`, `Visit`, `Vet`, `Specialty`, `PetType`, `User`, `Role`) — HARVEST: Preserve field structures, validation constraints, and JPA annotations exactly (will be migrated from javax.persistence.* to jakarta.persistence.*)
- All mapper interfaces (`OwnerMapper`, `PetMapper`, `VisitMapper`, `VetMapper`, `SpecialtyMapper`, `PetTypeMapper`, `UserMapper`) — HARVEST: MapStruct interface definitions preserved exactly

## Decided target shapes

The MAPPINGS.md rows that apply:

- **javax-to-jakarta-import-00001**: The package 'javax' has been replaced by 'jakarta' — decided target: jakarta.* imports

## Contracts owned by this story

- **Findings**: javax-to-jakarta-import-00001 for all domain entities and mappers
- **Preserve**: None - these are pure model classes with no configuration surfaces
- **Behavioral pins**: 
  - God-node characterization: PetType (18 references), Visit (18 references), Pet (17 references) must maintain exact legacy relationships
  - Entity validation constraints preserved exactly (@NotEmpty, @Digits, @Size)
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- All domain entities and mappers migrated with package rename to com.demo
- javax.persistence imports migrated to jakarta.persistence
- God-node entities characterized with tests (PetType, Visit, Pet)
- Project builds successfully
- No changes to entity relationships or validation constraints
- Circular dependency group converted together as required by dependency order
