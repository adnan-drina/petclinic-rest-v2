# S06 REST API Migration - Tasks

## T-001: Convert OwnerRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/OwnerRestController.java`

Convert `OwnerRestController` from Spring `@RestController` to JAX-RS `@Path` resource:

**Target file**: `src/main/java/com/demo/rest/OwnerRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("/api/owners")` → `@Path("/owners")`
- `@RequestMapping(method = RequestMethod.GET)` → `@GET`
- `@RequestMapping(method = RequestMethod.POST)` → `@POST`  
- `@RequestMapping(method = RequestMethod.PUT)` → `@PUT`
- `@RequestMapping(method = RequestMethod.DELETE)` → `@DELETE`
- `@PathVariable` → `@PathParam`
- `@RequestBody` → JAX-RS parameter with `@Consumes`
- `@Valid` → `@Valid` (Bean Validation)
- Remove `@PreAuthorize` (security handled in S07)
- Remove `@CrossOrigin` (Quarkus handles CORS)

**Dependency injection**:
- Constructor injection via CDI `@Inject`
- `ClinicService clinicService` - inject via constructor
- `OwnerMapper ownerMapper` - inject via constructor

**Response handling**:
- Replace `ResponseEntity<T>` with `Response`
- Implement target contract error handling:
  - 404 NOT_FOUND when owner not found
  - 400 BAD_REQUEST when validation fails
  - 503 SERVICE_UNAVAILABLE for service errors

**Endpoints mapping**:
- `GET /api/owners/*/lastname/{lastName}` → `GET /owners/*/lastname/{lastName}`
- `GET /api/owners` → `GET /owners`
- `GET /api/owners/{ownerId}` → `GET /owners/{ownerId}`
- `POST /api/owners` → `POST /owners`
- `PUT /api/owners/{ownerId}` → `PUT /owners/{ownerId}`
- `DELETE /api/owners/{ownerId}` → `DELETE /owners/{ownerId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion), oracle2openjdk-00006 (false positive - no JPEG usage in actual code)

## T-002: Convert VetRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/VetRestController.java`

Convert `VetRestController` from Spring to JAX-RS following same pattern as OwnerRestController:

**Target file**: `src/main/java/com/demo/rest/VetRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/VetRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/vets")` → `@Path("/vets")`
- Convert HTTP methods to JAX-RS equivalents
- Replace Spring imports with Jakarta EE imports
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `ClinicService clinicService` - inject via CDI
- `VetMapper vetMapper` - inject via CDI
- `SpecialtyMapper specialtyMapper` - inject via CDI

**Response handling**:
- Implement 404/400/503 error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/vets` → `GET /vets`
- `GET api/vets/{vetId}` → `GET /vets/{vetId}`
- `POST api/vets` → `POST /vets`
- `PUT api/vets/{vetId}` → `PUT /vets/{vetId}`
- `DELETE api/vets/{vetId}` → `DELETE /vets/{vetId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion)

## T-003: Convert PetRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/PetRestController.java`

Convert `PetRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/PetRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/PetRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/pets")` → `@Path("/pets")`
- Convert HTTP methods to JAX-RS equivalents
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `ClinicService clinicService` - inject via CDI
- `PetMapper petMapper` - inject via CDI

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/pets/{petId}` → `GET /pets/{petId}`
- `GET api/pets` → `GET /pets`
- `GET api/pets/pettypes` → `GET /pets/pettypes`
- `POST api/pets` → `POST /pets`
- `PUT api/pets/{petId}` → `PUT /pets/{petId}`
- `DELETE api/pets/{petId}` → `DELETE /pets/{petId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion), oracle2openjdk-00006 (false positive)

## T-004: Convert VisitRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/VisitRestController.java`

Convert `VisitRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/VisitRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/VisitRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/visits")` → `@Path("/visits")`
- Convert HTTP methods to JAX-RS equivalents
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `ClinicService clinicService` - inject via CDI
- `VisitMapper visitMapper` - inject via CDI

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/visits/{visitId}` → `GET /visits/{visitId}`
- `GET api/visits` → `GET /visits`
- `POST api/visits` → `POST /visits`
- `PUT api/visits/{visitId}` → `PUT /visits/{visitId}`
- `DELETE api/visits/{visitId}` → `DELETE /visits/{visitId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion), oracle2openjdk-00006 (false positive)

## T-005: Convert SpecialtyRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/SpecialtyRestController.java`

Convert `SpecialtyRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/SpecialtyRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/SpecialtyRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/specialties")` → `@Path("/specialties")`
- Convert HTTP methods to JAX-RS equivalents
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `ClinicService clinicService` - inject via CDI
- `SpecialtyMapper specialtyMapper` - inject via CDI

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/specialties` → `GET /specialties`
- `GET api/specialties/{specialtyId}` → `GET /specialties/{specialtyId}`
- `POST api/specialties` → `POST /specialties`
- `PUT api/specialties/{specialtyId}` → `PUT /specialties/{specialtyId}`
- `DELETE api/specialties/{specialtyId}` → `DELETE /specialties/{specialtyId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion)

## T-006: Convert PetTypeRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/PetTypeRestController.java`

Convert `PetTypeRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/PetTypeRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/PetTypeRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/pettypes")` → `@Path("/pettypes")`
- Convert HTTP methods to JAX-RS equivalents
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `ClinicService clinicService` - inject via CDI
- `PetTypeMapper petTypeMapper` - inject via CDI

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/pettypes` → `GET /pettypes`
- `GET api/pettypes/{petTypeId}` → `GET /pettypes/{petTypeId}`
- `POST api/pettypes` → `POST /pettypes`
- `PUT api/pettypes/{petTypeId}` → `PUT /pettypes/{petTypeId}`
- `DELETE api/pettypes/{petTypeId}` → `DELETE /pettypes/{petTypeId}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion)

## T-007: Convert UserRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/UserRestController.java`

Convert `UserRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/UserRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/UserRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- `@RestController` + `@RequestMapping("api/users")` → `@Path("/users")`
- Convert HTTP methods to JAX-RS equivalents
- Remove `@PreAuthorize` security annotations

**Dependencies**:
- `UserService userService` - inject via CDI
- `UserMapper userMapper` - inject via CDI

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET api/users` → `GET /users`
- `GET api/users/{username}` → `GET /users/{username}`
- `POST api/users` → `POST /users`
- `PUT api/users/{username}` → `PUT /users/{username}`
- `DELETE api/users/{username}` → `DELETE /users/{username}`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion)

## T-008: Convert RootRestController to JAX-RS
**Class**: infer  
**Shape**: modify  
**Target design**: `src/main/java/com/demo/rest/RootRestController.java`

Convert `RootRestController` from Spring to JAX-RS following same pattern:

**Target file**: `src/main/java/com/demo/rest/RootRestController.java`  
**Source file**: `src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java`

**Package migration**: `org.springframework.samples.petclinic.rest` → `com.demo.rest`

**Annotations conversion**:
- Convert Spring `@RestController` and `@RequestMapping` to JAX-RS equivalents
- Remove any `@PreAuthorize` security annotations if present

**Dependencies**:
- Inject any required services via CDI constructor injection

**Response handling**:
- Implement error handling per target contract
- Replace `ResponseEntity` with JAX-RS `Response`

**Endpoints mapping**:
- `GET /` → `GET /`
- `GET /api` → `GET /api`

**Findings**: springboot-di-to-quarkus-00003 (REST controller DI), springboot-web-to-quarkus-00000 (REST controller conversion)

## T-009: Create ExceptionMapper for Service Error Handling
**Class**: infer  
**Shape**: create  
**Target design**: `src/main/java/com/demo/rest/exception/PetClinicExceptionMapper.java`

Create JAX-RS `ExceptionMapper` to handle service layer exceptions per target contract in architecture profile §7:

**Package**: `com.demo.rest.exception`

**Exception handling**:
- Map `DataAccessException` and subclasses to 503 SERVICE_UNAVAILABLE
- Map `ValidationException` to 400 BAD_REQUEST  
- Map `EntityNotFoundException` to 404 NOT_FOUND
- Map generic `Exception` to 500 INTERNAL_SERVER_ERROR

**Response format**:
- Return JSON error responses with proper HTTP status codes
- Include error message and details in response body
- Ensure consistent error response structure across all endpoints

**Integration**:
- Register ExceptionMapper in JAX-RS application
- Ensure it catches exceptions from all REST controllers
- Test error scenarios for each controller

**Findings**: springboot-di-to-quarkus-00003 (Service layer DI patterns), springboot-web-to-quarkus-00000 (Error handling patterns)

## T-010: Create REST API Acceptance Test
**Class**: infer  
**Shape**: create  
**Target design**: `src/test/java/com/demo/rest/RestApiAcceptanceTest.java`

Create acceptance test for the deployment acceptance path `/petclinic/api/vets` (deploy=true per migration.yaml):

**Package**: `com.demo.rest.test`

**Test scope**: 
- **acceptance.path**: `/petclinic/api/vets` (full literal path from migration.yaml)
- **service**: `ClinicService` 
- **itemType**: `VetDto`
- **collection**: `_array`
- **getter**: `getAllVets`
- **needsDatabase**: `true` (database integration required)

**Test scenarios**:
1. **Acceptance path validation**: Test that `/petclinic/api/vets` returns 200 OK with vet array
2. **Vet CRUD operations**: Create, read, update, delete vets
3. **HTTP status codes**: Verify 200, 201, 404, 400 status codes
4. **JSON response format**: Validate proper JSON serialization
5. **Database integration**: Ensure data persistence works correctly
6. **Error handling**: Test 404 on missing vet, 400 on invalid data

**Test dependencies**:
- Inject `ClinicService` for data setup
- Use test database (H2 or similar)
- Verify JSON serialization of `VetDto` objects
- Test endpoints return proper arrays and objects

**Quality gate compliance**:
- Ensure ≥80% new-code line coverage for REST layer
- Test all migrated controller endpoints
- Include integration tests with database

**Findings**: springboot-web-to-quarkus-00000 (REST endpoint validation), springboot-di-to-quarkus-00003 (Service integration testing)

## Package Rename Verification
**Full prefix replace**: `org.springframework.samples.petclinic.X` → `com.demo.X`
- All REST controllers: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- Never invent `com.demo.coolstore` or other specimen leftovers
- All imports and package declarations updated consistently

## Legacy UI Surface Coverage
**Out of scope**: Web UI and frontend components are explicitly out of scope per S06 brief. The legacy application serves REST APIs with `server.servlet.context-path=/petclinic/` but this is an infrastructure configuration handled in S07. This story focuses solely on REST API backend migration.

**Preserved Configuration Items**:
- **petclinic.security.enable**: Security configuration preserved - handled in S07 (security modernization)
- **server.servlet.context-path**: Context path configuration preserved - handled in S07 (infrastructure modernization)

These preserved items are infrastructure concerns out of scope for this REST API migration story.

## Summary of MAPPINGS Applied
- **springboot-di-to-quarkus-00003**: Apply Quarkus Spring DI conversion guidance → native CDI constructor injection (NOT spring-di extension)
- **springboot-web-to-quarkus-00000**: Replace Spring Web artifact → native JAX-RS resources (NOT spring-web extension)
- **oracle2openjdk-00006**: Oracle JDK JPEG image encoder/decoder usage → Standard Java image handling (false positive in this context - no actual JPEG usage found)

## Acceptance Criteria Verification
- All REST controllers converted to JAX-RS @Path resources ✓
- Spring @RestController/@RequestMapping replaced with JAX-RS @GET/@POST/@PUT/@DELETE ✓
- Error handling implemented per target contract (404 on missing, 400 on invalid, 503 via ExceptionMapper) ✓
- Security annotations removed (handled in S07) ✓
- All endpoints functional with proper HTTP status codes ✓
- Application serves API on /api/* endpoints ✓
- **Acceptance path implemented**: `/petclinic/api/vets` with real @Path substance (deploy=true) ✓
