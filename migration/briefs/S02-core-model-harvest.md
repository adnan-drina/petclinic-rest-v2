# S02: Core Model Harvest

## Goal & position

Harvest the god-node foundation classes with highest fan-in that are referenced by all other domain classes. These classes form the base of the entity hierarchy and utilities. This story enables all subsequent model and service work by providing the foundational entities and utilities that other classes depend on.

## In scope

The exact legacy classes/files this story modernizes:

- `src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java` — Base entity class with id property
  ```java
  package org.springframework.samples.petclinic.model;

  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.MappedSuperclass;
  ```

- `src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java` — Named entity base class
  ```java
  package org.springframework.samples.petclinic.model;

  import javax.persistence.Column;
  import javax.persistence.MappedSuperclass;
  ```

- `src/main/java/org/springframework/samples/petclinic/model/Person.java` — Person base class
  ```java
  package org.springframework.samples.petclinic.model;

  import javax.persistence.Column;
  import javax.persistence.MappedSuperclass;
  ```

- `src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java` — Entity utility methods
  ```java
  package org.springframework.samples.petclinic.util;

  import org.springframework.samples.petclinic.model.BaseEntity;
  import org.springframework.samples.petclinic.model.Pet;
  ```

- `src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java` — Error response DTO
  ```java
  package org.springframework.samples.petclinic.rest;

  import java.util.ArrayList;
  import java.util.List;
  ```

- `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java` — Spring Boot application bootstrap
  ```java
  package org.springframework.samples.petclinic;

  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  ```

## Out of scope

All domain entities (Owner, Pet, Visit, etc.), repositories, services, and REST controllers. These depend on the base entities and will be handled in subsequent stories S03-S06.

## Class roles & target contract

For each in-scope class, its role and target contract:

- `BaseEntity`, `NamedEntity`, `Person` — HARVEST: Preserve field structures, validation constraints, and JPA annotations exactly (will be migrated from javax.persistence.* to jakarta.persistence.*)
- `EntityUtils` — HARVEST: Static utility methods for entity operations preserved exactly
- `BindingErrorsResponse` — HARVEST: Error response DTO preserved exactly
- `PetClinicApplication` — REDESIGN: Removed (Quarkus auto-discovery replaces `@SpringBootApplication`)

## Decided target shapes

The MAPPINGS.md rows that apply:

- **javax-to-jakarta-import-00001**: The package 'javax' has been replaced by 'jakarta' — decided target: jakarta.* imports
- **springboot-annotations-to-quarkus-00002**: Replace Spring ComponentScan with CDI bean discovery conventions — decided target: delete `@SpringBootApplication` + main class

## Contracts owned by this story

- **Findings**: javax-to-jakarta-import-00001, springboot-annotations-to-quarkus-00002
- **Preserve**: None - these are pure model/utility classes with no configuration surfaces
- **Behavioral pins**: None - base entities and utilities preserve exact legacy behavior
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- All base entities and utilities harvested with package rename from org.springframework.samples.petclinic to com.demo
- PetClinicApplication removed (Quarkus auto-discovery)
- javax.persistence imports migrated to jakarta.persistence
- Project builds successfully
- Base entity characterization tests pass
- No remaining Spring-specific bootstrap or utility classes
