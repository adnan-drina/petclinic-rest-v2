# S05: Service Layer Modernization

## Goal & position

Modernize the service layer (ClinicService, UserService) from Spring @Service to Quarkus @ApplicationScoped CDI beans. This story provides the business logic facade that coordinates all repository operations. Services depend on repositories migrated in S04 and must be converted before REST controllers in S06.

## In scope

The exact legacy classes/files this story modernizes:

- `ClinicService` interface:
  ```java
  package org.springframework.samples.petclinic.service;

  import java.util.Collection;
  import org.springframework.dao.DataAccessException;
  import org.springframework.samples.petclinic.model.Owner;
  import org.springframework.samples.petclinic.model.Pet;
  import org.springframework.samples.petclinic.model.PetType;
  import org.springframework.samples.petclinic.model.Specialty;
  import org.springframework.samples.petclinic.model.Vet;
  import org.springframework.samples.petclinic.model.Visit;

  public interface ClinicService {
      Pet findPetById(int id) throws DataAccessException;
      Collection<Pet> findAllPets() throws DataAccessException;
      void savePet(Pet pet) throws DataAccessException;
      void deletePet(Pet pet) throws DataAccessException;
      Collection<Visit> findAllVisits() throws DataAccessException;
      Vet findVetById(int id) throws DataAccessException;
      Collection<Vet> findAllVets() throws DataAccessException;
      void saveVet(Vet vet) throws DataAccessException;
      void deleteVet(Vet vet) throws DataAccessException;
      Owner findOwnerById(int id) throws DataAccessException;
      Collection<Owner> findAllOwners() throws DataAccessException;
      void saveOwner(Owner owner) throws DataAccessException;
      void deleteOwner(Owner owner) throws DataAccessException;
      Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException;
      void saveVisit(Visit visit) throws DataAccessException;
      Visit findVisitById(int visitId) throws DataAccessException;
      void deleteVisit(Visit visit) throws DataAccessException;
      Collection<Specialty> findAllSpecialties() throws DataAccessException;
      void saveSpecialty(Specialty specialty) throws DataAccessException;
      void deleteSpecialty(Specialty specialty) throws DataAccessException;
      Specialty findSpecialtyById(int id) throws DataAccessException;
      Collection<PetType> findAllPetTypes() throws DataAccessException;
      void savePetType(PetType petType) throws DataAccessException;
      void deletePetType(PetType petType) throws DataAccessException;
      PetType findPetTypeById(int id) throws DataAccessException;
  }
  ```

- `ClinicServiceImpl` implementation:
  ```java
  package org.springframework.samples.petclinic.service;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.cache.annotation.Cacheable;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;

  @Service
  @Transactional
  public class ClinicServiceImpl implements ClinicService {

      private PetRepository petRepository;
      private VetRepository vetRepository;
      private OwnerRepository ownerRepository;
      private VisitRepository visitRepository;
      private SpecialtyRepository specialtyRepository;
      private PetTypeRepository petTypeRepository;

      @Autowired
      public ClinicServiceImpl(
              PetRepository petRepository,
              VetRepository vetRepository,
              OwnerRepository ownerRepository,
              VisitRepository visitRepository,
              SpecialtyRepository specialtyRepository,
              PetTypeRepository petTypeRepository) {
          this.petRepository = petRepository;
          this.vetRepository = vetRepository;
          this.ownerRepository = ownerRepository;
          this.visitRepository = visitRepository;
          this.specialtyRepository = specialtyRepository;
          this.petTypeRepository = petTypeRepository;
      }
  }
  ```

- `UserService` interface and implementation following the same pattern

## Out of scope

REST controllers and infrastructure components. These depend on services and will be handled in S06-S07.

## Class roles & target contract

For each in-scope class, its role and target contract:

- `ClinicService`/`ClinicServiceImpl` — REDESIGN: `@Service` → `@ApplicationScoped` with **thread-safe** state management (no shared mutable state, all operations stateless), **concurrent** collection usage where needed
- `UserService`/`UserServiceImpl` — REDESIGN: Same target contract as ClinicService

**Target contract from architecture-profile §7**:
- **Thread-safe singleton state**: All service instances are stateless; any caching must be thread-safe
- **No shared mutable state**: All instance variables are immutable or properly synchronized
- **Concurrent collection usage**: Use concurrent collections when caching is required

## Decided target shapes

The MAPPINGS.md rows that apply:

- **springboot-di-to-quarkus-00003**: Apply Quarkus Spring DI conversion guidance — decided target: native CDI constructor injection (NOT the spring-di extension)

## Contracts owned by this story

- **Findings**: springboot-di-to-quarkus-00003
- **Preserve**: None - service layer has no configuration surfaces
- **Behavioral pins**: 
  - Service interface contracts preserved exactly
  - All business logic methods maintain exact return types and exception behavior
  - Transaction management semantics maintained through @Transactional
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- All services converted to @ApplicationScoped CDI beans
- Spring @Service annotations removed, @Autowired constructors replaced with CDI injection
- All service interfaces preserved with exact method signatures
- Business logic contracts maintained exactly
- Thread-safe singleton pattern implemented
- Project builds successfully with service-level tests passing
