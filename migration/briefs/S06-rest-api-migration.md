# S06: REST API Migration

## Goal & position

Modernize all REST controllers from Spring @RestController to JAX-RS resources with proper error handling. This story provides the external API surface that depends on services. After this story, the application will be ready for deployment with functional REST endpoints. This is the final core migration story before infrastructure modernization in S07.

## In scope

The exact legacy classes/files this story modernizes:

- `OwnerRestController` - Main REST controller for Owner entity:
  ```java
  package org.springframework.samples.petclinic.rest;

  import org.springframework.http.HttpHeaders;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.samples.petclinic.dto.OwnerDto;
  import org.springframework.samples.petclinic.mapper.OwnerMapper;
  import org.springframework.samples.petclinic.model.Owner;
  import org.springframework.samples.petclinic.service.ClinicService;
  import org.springframework.validation.BindingResult;
  import org.springframework.web.bind.annotation.*;

  @RestController
  @CrossOrigin(exposedHeaders = "errors, content-type")
  @RequestMapping("/api/owners")
  public class OwnerRestController {

      private final ClinicService clinicService;
      private final OwnerMapper ownerMapper;

      public OwnerRestController(ClinicService clinicService, OwnerMapper ownerMapper) {
          this.clinicService = clinicService;
          this.ownerMapper = ownerMapper;
      }

      @RequestMapping(value = "/*/lastname/{lastName}", method = RequestMethod.GET, produces = "application/json")
      public ResponseEntity<Collection<OwnerDto>> getOwnersList(@PathVariable("lastName") String ownerLastName) {
          if (ownerLastName == null) {
              ownerLastName = "";
          }
          Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
          if (owners.isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);
      }

      @RequestMapping(value = "/{ownerId}", method = RequestMethod.GET, produces = "application/json")
      public ResponseEntity<OwnerDto> getOwner(@PathVariable("ownerId") int ownerId) {
          Owner owner = null;
          owner = this.clinicService.findOwnerById(ownerId);
          if (owner == null) {
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          return new ResponseEntity<>(ownerMapper.toOwnerDto(owner), HttpStatus.OK);
      }

      @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
      public ResponseEntity<OwnerDto> addOwner(@RequestBody @Valid OwnerDto ownerDto, BindingResult bindingResult,
                                               UriComponentsBuilder ucBuilder) {
          HttpHeaders headers = new HttpHeaders();
          if (bindingResult.hasErrors() || ownerDto.getId() != null) {
              BindingErrorsResponse errors = new BindingErrorsResponse(ownerDto.getId());
              errors.addAllErrors(bindingResult);
              headers.add("errors", errors.toJSON());
              return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
          }
          Owner owner = ownerMapper.toOwner(ownerDto);
          this.clinicService.saveOwner(owner);
          ownerDto.setId(owner.getId());
          headers.setLocation(ucBuilder.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
          return new ResponseEntity<>(ownerDto, headers, HttpStatus.CREATED);
      }

      @RequestMapping(value = "/{ownerId}", method = RequestMethod.DELETE, produces = "application/json")
      @Transactional
      public ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") int ownerId) {
          Owner owner = this.clinicService.findOwnerById(ownerId);
          if (owner == null) {
              return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
          }
          this.clinicService.deleteOwner(owner);
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
  }
  ```

- All other REST controllers (PetRestController, VisitRestController, VetRestController, SpecialtyRestController, PetTypeRestController, UserRestController, RootRestController) following the same pattern

## Out of scope

Security configuration, Swagger/OpenAPI configuration, and JMX monitoring aspects. These infrastructure components will be handled in S07.

## Class roles & target contract

For each in-scope class, its role and target contract:

- All REST controllers (OwnerRestController, PetRestController, VisitRestController, VetRestController, SpecialtyRestController, PetTypeRestController, UserRestController, RootRestController) — REDESIGN: Spring `@RestController` → JAX-RS `@Path` with **404** on missing (never creates), **400** on invalid input, **503** via `ExceptionMapper`

**Target contract from architecture-profile §7**:
- **404 on missing resources**: GET endpoints return 404 NOT_FOUND when resources don't exist (never creates on GET)
- **400 on invalid input**: POST/PUT endpoints return 400 BAD_REQUEST when input validation fails
- **503 via ExceptionMapper**: Service unavailable errors mapped to 503 via ExceptionMapper
- **Idempotent GET**: GET operations must be idempotent and side-effect free

## Decided target shapes

The MAPPINGS.md rows that apply:

- **springboot-di-to-quarkus-00003**: Apply Quarkus Spring DI conversion guidance — decided target: native CDI constructor injection (NOT the spring-di extension)
- **springboot-web-to-quarkus-00000**: Replace the Spring Web artifact with Quarkus 'spring-web' extension — decided target: native JAX-RS resources (NOT the spring-web extension)
- **oracle2openjdk-00006**: Oracle JDK JPEG image encoder/decoder usage — decided target: Standard Java image handling

## Contracts owned by this story

- **Findings**: springboot-di-to-quarkus-00003, springboot-web-to-quarkus-00000, oracle2openjdk-00006
- **Preserve**: None - REST endpoints have no configuration surfaces
- **Behavioral pins**: 
  - GET endpoints return 404 NOT_FOUND when resources don't exist (deliberate departure from legacy behavior of creating resources)
  - POST endpoints with validation errors return 400 BAD_REQUEST
  - All CRUD operations maintain exact JSON contract with proper HTTP status codes
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- All REST controllers converted to JAX-RS @Path resources
- Spring @RestController/@RequestMapping replaced with JAX-RS @GET/@POST/@PUT/@DELETE
- Error handling implemented per target contract (404 on missing, 400 on invalid, 503 via ExceptionMapper)
- Security annotations removed (will be handled in S07)
- All endpoints functional with proper HTTP status codes
- Application serves API on /api/* endpoints
- Deploy milestone: application deploys and serves acceptance path `/petclinic/api/vets`
