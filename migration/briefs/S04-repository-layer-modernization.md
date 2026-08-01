# S04: Repository Layer Modernization

## Goal & position

Modernize all repository implementations (JDBC, JPA, Spring Data JPA variants) and their interfaces to use CDI with constructor injection instead of Spring annotations. This story provides the data access foundation needed by services. The repository layer depends on the domain models migrated in S03 and must be converted before services in S05.

## In scope

The exact legacy classes/files this story modernizes:

- JDBC repository implementations:
  ```java
  package org.springframework.samples.petclinic.repository.jdbc;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Repository;
  import org.springframework.transaction.annotation.Transactional;

  @Repository
  @Profile("jdbc")
  @Transactional(readOnly = true)
  public class JdbcOwnerRepositoryImpl implements OwnerRepository {

      private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
      private SimpleJdbcInsert insertOwner;
      private PetRepository petRepository;

      @Autowired
      public JdbcOwnerRepositoryImpl(DataSource dataSource, PetRepository petRepository) {
          this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
          this.insertOwner = new SimpleJdbcInsert(dataSource).withTableName("owners");
          this.petRepository = petRepository;
      }
  }
  ```

- JPA repository implementations:
  ```java
  package org.springframework.samples.petclinic.repository.jpa;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.orm.ObjectRetrievalFailureException;
  import org.springframework.stereotype.Repository;
  import org.springframework.transaction.annotation.Transactional;

  @Repository
  @Profile("jpa")
  @Transactional(readOnly = true)
  public class JpaOwnerRepositoryImpl implements OwnerRepository {

      private EntityManager entityManager;

      @Autowired
      public JpaOwnerRepositoryImpl(EntityManager entityManager) {
          this.entityManager = entityManager;
      }
  }
  ```

- Spring Data JPA repository implementations:
  ```java
  package org.springframework.samples.petclinic.repository.springdatajpa;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.annotation.Profile;
  import org.springframework.samples.petclinic.model.Pet;
  import org.springframework.stereotype.Repository;
  import org.springframework.transaction.annotation.Transactional;

  @Repository
  @Profile("spring-data-jpa")
  @Transactional(readOnly = true)
  public class SpringDataPetRepositoryImpl implements PetRepository {

      private PetRepository petRepository;

      @Autowired
      public SpringDataPetRepositoryImpl(PetRepository petRepository) {
          this.petRepository = petRepository;
      }
  }
  ```

## Out of scope

Service layer and REST controllers. These depend on repositories and will be handled in S05-S06.

## Class roles & target contract

For each in-scope class, its role and target contract:

- All repository implementation classes (JDBC, JPA, Spring Data JPA variants) — REDESIGN: Spring `@Repository` → `@ApplicationScoped` CDI beans with constructor injection, transaction management via `@Transactional`
- Repository interfaces — HARVEST: Method signatures preserved, implementation details updated

## Decided target shapes

The MAPPINGS.md rows that apply:

- **springboot-di-to-quarkus-00003**: Apply Quarkus Spring DI conversion guidance — decided target: native CDI constructor injection (NOT the spring-di extension)
- **transaction-to-quarkus-00003**: EntityManager remove operations require @Transactional in Quarkus — decided target: explicit @Transactional annotations for all database operations

## Contracts owned by this story

- **Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003
- **Preserve**: None - repository layer has no configuration surfaces
- **Behavioral pins**: 
  - Repository interface contracts preserved exactly
  - Transaction management semantics maintained (read-only vs read-write operations)
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- All repository implementations converted to @ApplicationScoped CDI beans
- Spring @Repository annotations removed, @Autowired constructors replaced with CDI injection
- All repository interfaces preserved with exact method signatures
- Transaction management maintained through @Transactional annotations
- Project builds successfully with all repository tests passing
