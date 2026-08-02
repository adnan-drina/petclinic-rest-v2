# S06 REST API Migration - Plan

## Overview
Migrate 8 REST controllers from Spring `@RestController` to JAX-RS `@Path` resources with proper error handling following the target contract in architecture profile §7.

## Mapping Summary

### Package Transformation
- **Legacy**: `org.springframework.samples.petclinic.rest`
- **Target**: `com.demo.rest`

### Annotation Mapping
| Spring | JAX-RS/Quarkus | Notes |
|--------|----------------|-------|
| `@RestController` | `@Path` | Main controller annotation |
| `@RequestMapping` | `@GET`, `@POST`, `@PUT`, `@DELETE` | HTTP method specific annotations |
| `@PathVariable` | `@PathParam` | Path parameter extraction |
| `@RequestBody` | `@Consumes` + parameter | Request body handling |
| `@Valid` | `@Valid` | Bean Validation (javax.validation → jakarta.validation) |
| `@CrossOrigin` | Remove | Quarkus handles CORS natively |
| `@PreAuthorize` | Remove | Security handled in S07 |

## Detailed Task Breakdown

### T-001: Convert OwnerRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/OwnerRestController.java`

Convert `OwnerRestController` from Spring to JAX-RS:
- Replace `@RestController` + `@RequestMapping` with `@Path("/owners")`
- Convert HTTP method mappings: `@RequestMapping(GET)` → `@GET`, etc.
- Replace `@PathVariable` with `@PathParam`
- Replace Spring `ResponseEntity` with JAX-RS `Response`
- Implement 404/400/503 error handling per target contract
- Remove security annotations (`@PreAuthorize`)
- Constructor injection via CDI `@Inject`
- Package: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Endpoints to migrate**:
- `GET /api/owners/*/lastname/{lastName}` → `GET /owners/*/lastname/{lastName}`
- `GET /api/owners` → `GET /owners`
- `GET /api/owners/{ownerId}` → `GET /owners/{ownerId}`
- `POST /api/owners` → `POST /owners`
- `PUT /api/owners/{ownerId}` → `PUT /owners/{ownerId}`
- `DELETE /api/owners/{ownerId}` → `DELETE /owners/{ownerId}`

### T-002: Convert VetRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/VetRestController.java`

Convert `VetRestController` following same pattern as OwnerRestController:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/vets")` → `@Path("/vets")`
- Error handling: 404, 400, 503 per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection
- Spring `ResponseEntity` → JAX-RS `Response`

**Endpoints to migrate**:
- `GET api/vets` → `GET /vets`
- `GET api/vets/{vetId}` → `GET /vets/{vetId}`
- `POST api/vets` → `POST /vets`
- `PUT api/vets/{vetId}` → `PUT /vets/{vetId}`
- `DELETE api/vets/{vetId}` → `DELETE /vets/{vetId}`

### T-003: Convert PetRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/PetRestController.java`

Convert `PetRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/pets")` → `@Path("/pets")`
- Error handling per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection

**Endpoints to migrate**:
- `GET api/pets/{petId}` → `GET /pets/{petId}`
- `GET api/pets` → `GET /pets`
- `GET api/pets/pettypes` → `GET /pets/pettypes`
- `POST api/pets` → `POST /pets`
- `PUT api/pets/{petId}` → `PUT /pets/{petId}`
- `DELETE api/pets/{petId}` → `DELETE /pets/{petId}`

### T-004: Convert VisitRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/VisitRestController.java`

Convert `VisitRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/visits")` → `@Path("/visits")`
- Error handling per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection

**Endpoints to migrate**:
- `GET api/visits/{visitId}` → `GET /visits/{visitId}`
- `GET api/visits` → `GET /visits`
- `POST api/visits` → `POST /visits`
- `PUT api/visits/{visitId}` → `PUT /visits/{visitId}`
- `DELETE api/visits/{visitId}` → `DELETE /visits/{visitId}`

### T-005: Convert SpecialtyRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/SpecialtyRestController.java`

Convert `SpecialtyRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/specialties")` → `@Path("/specialties")`
- Error handling per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection

**Endpoints to migrate**:
- `GET api/specialties` → `GET /specialties`
- `GET api/specialties/{specialtyId}` → `GET /specialties/{specialtyId}`
- `POST api/specialties` → `POST /specialties`
- `PUT api/specialties/{specialtyId}` → `PUT /specialties/{specialtyId}`
- `DELETE api/specialties/{specialtyId}` → `DELETE /specialties/{specialtyId}`

### T-006: Convert PetTypeRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/PetTypeRestController.java`

Convert `PetTypeRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/pettypes")` → `@Path("/pettypes")`
- Error handling per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection

**Endpoints to migrate**:
- `GET api/pettypes` → `GET /pettypes`
- `GET api/pettypes/{petTypeId}` → `GET /pettypes/{petTypeId}`
- `POST api/pettypes` → `POST /pettypes`
- `PUT api/pettypes/{petTypeId}` → `PUT /pettypes/{petTypeId}`
- `DELETE api/pettypes/{petTypeId}` → `DELETE /pettypes/{petTypeId}`

### T-007: Convert UserRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/UserRestController.java`

Convert `UserRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations: `@Path("api/users")` → `Path("/users")`
- Error handling per target contract
- Remove `@PreAuthorize` security annotations
- CDI constructor injection

**Endpoints to migrate**:
- `GET api/users` → `GET /users`
- `GET api/users/{username}` → `GET /users/{username}`
- `POST api/users` → `POST /users`
- `PUT api/users/{username}` → `PUT /users/{username}`
- `DELETE api/users/{username}` → `DELETE /users/{username}`

### T-008: Convert RootRestController
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/RootRestController.java`

Convert `RootRestController` following same pattern:
- Package migration: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- JAX-RS annotations conversion
- Error handling per target contract
- Remove `@PreAuthorize` security annotations if present
- CDI constructor injection

**Endpoints to migrate**:
- `GET /` → `GET /`
- `GET /api` → `GET /api`

### T-009: Create ExceptionMapper for Error Handling
**Class**: infer  
**Shape**: create  
**Target design**: `src/main/java/com/demo/rest/exception/PetClinicExceptionMapper.java`

Create JAX-RS `ExceptionMapper` to handle service layer exceptions per target contract:
- Map service unavailable errors to 503 SERVICE_UNAVAILABLE
- Map validation errors to 400 BAD_REQUEST
- Map not found errors to 404 NOT_FOUND
- Ensure proper JSON error responses

### T-010: Create REST API Acceptance Test
**Class**: infer  
**Shape**: create  
**Target design**: `src/test/java/com/demo/rest/RestApiAcceptanceTest.java`

Create acceptance test for the acceptance path `/petclinic/api/vets`:
- Test that application deploys and serves the acceptance path
- Verify proper HTTP status codes
- Test basic CRUD operations
- Verify JSON response format
- Database integration: true (per migration.yaml needsDatabase: true)

## MAPPINGS Applied
- **springboot-di-to-quarkus-00003**: Native CDI constructor injection (NOT spring-di extension)
- **springboot-web-to-quarkus-00000**: Native JAX-RS resources (NOT spring-web extension)  
- **oracle2openjdk-00006**: Standard Java image handling if needed

## Package Rename (O-M3ACCEPT)
- **Full prefix replace**: `org.springframework.samples.petclinic.X` → `com.demo.X`
- Never invent `com.demo.coolstore` or other specimen leftovers

## Acceptance Criteria (O-M3ACCEPT)
- **deploy=true** (from migration.yaml): Task the full literal acceptance.path `/petclinic/api/vets` with real @Path substance
- All REST controllers converted to JAX-RS @Path resources
- Spring @RestController/@RequestMapping replaced with JAX-RS @GET/@POST/@PUT/@DELETE
- Error handling implemented per target contract (404 on missing, 400 on invalid, 503 via ExceptionMapper)
- Security annotations removed (handled in S07)
- All endpoints functional with proper HTTP status codes
- Application serves API on /api/* endpoints
