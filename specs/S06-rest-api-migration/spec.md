# S06 REST API Migration - Specification

## Overview
This specification documents the modernization of 8 REST controllers from Spring `@RestController` to JAX-RS `@Path` resources with proper error handling and HTTP status codes. All controllers are converted to use native Quarkus CDI and JAX-RS APIs.

## In-Scope Controllers

### OwnerRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java`
**Package**: `org.springframework.samples.petclinic.rest`

**Current Spring endpoints**:
- `GET /api/owners/*/lastname/{lastName}` - Search owners by last name
- `GET /api/owners` - Get all owners
- `GET /api/owners/{ownerId}` - Get specific owner
- `POST /api/owners` - Create new owner
- `PUT /api/owners/{ownerId}` - Update existing owner
- `DELETE /api/owners/{ownerId}` - Delete owner

**Dependencies**: ClinicService, OwnerMapper, @PreAuthorize security annotations

### VetRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/VetRestController.java`

**Current Spring endpoints**:
- `GET api/vets` - Get all vets
- `GET api/vets/{vetId}` - Get specific vet
- `POST api/vets` - Create new vet
- `PUT api/vets/{vetId}` - Update existing vet
- `DELETE api/vets/{vetId}` - Delete vet

**Dependencies**: ClinicService, VetMapper, SpecialtyMapper

### PetRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/PetRestController.java`

**Current Spring endpoints**:
- `GET api/pets/{petId}` - Get specific pet
- `GET api/pets` - Get all pets
- `GET api/pets/pettypes` - Get pet types
- `POST api/pets` - Create new pet
- `PUT api/pets/{petId}` - Update existing pet
- `DELETE api/pets/{petId}` - Delete pet

**Dependencies**: ClinicService, PetMapper

### VisitRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/VisitRestController.java`

**Current Spring endpoints**:
- `GET api/visits/{visitId}` - Get specific visit
- `GET api/visits` - Get all visits
- `POST api/visits` - Create new visit
- `PUT api/visits/{visitId}` - Update existing visit
- `DELETE api/visits/{visitId}` - Delete visit

### SpecialtyRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/SpecialtyRestController.java`

**Current Spring endpoints**:
- `GET api/specialties` - Get all specialties
- `GET api/specialties/{specialtyId}` - Get specific specialty
- `POST api/specialties` - Create new specialty
- `PUT api/specialties/{specialtyId}` - Update existing specialty
- `DELETE api/specialties/{specialtyId}` - Delete specialty

### PetTypeRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/PetTypeRestController.java`

**Current Spring endpoints**:
- `GET api/pettypes` - Get all pet types
- `GET api/pettypes/{petTypeId}` - Get specific pet type
- `POST api/pettypes` - Create new pet type
- `PUT api/pettypes/{petTypeId}` - Update existing pet type
- `DELETE api/pettypes/{petTypeId}` - Delete pet type

### UserRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/UserRestController.java`

**Current Spring endpoints**:
- `GET api/users` - Get all users
- `GET api/users/{username}` - Get specific user
- `POST api/users` - Create new user
- `PUT api/users/{username}` - Update existing user
- `DELETE api/users/{username}` - Delete user

### RootRestController
**Legacy path**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java`

**Current Spring endpoints**:
- `GET /` - Root endpoint
- `GET /api` - API root endpoint

## Package Renaming
All controllers will be moved from:
- **Legacy package**: `org.springframework.samples.petclinic.rest`
- **Target package**: `com.demo.rest`

## Target Contract (Architecture Profile §7)

### Error Handling
- **404 NOT_FOUND**: Return when resources don't exist (never creates on GET as legacy did)
- **400 BAD_REQUEST**: Return when input validation fails or body/path IDs don't match
- **503 SERVICE_UNAVAILABLE**: Return for service unavailable errors via ExceptionMapper
- **200 OK**: Return for successful GET operations
- **201 CREATED**: Return for successful POST operations with Location header
- **204 NO_CONTENT**: Return for successful PUT/DELETE operations

### API Patterns
- **Idempotent GET**: All GET operations must be side-effect free
- **Validation**: All POST/PUT endpoints validate input using Bean Validation
- **ResponseEntity**: Replace Spring ResponseEntity with JAX-RS Response
- **Cross-Origin**: Remove @CrossOrigin annotations (handled at Quarkus level)

### Dependency Injection
- **Constructor Injection**: Use CDI @Inject constructor injection (not Spring @Autowired)
- **Services**: Inject ClinicService via CDI
- **Mappers**: Inject MapStruct mappers via CDI

## DTO and Mapper Dependencies

All controllers depend on:
- **DTO classes**: OwnerDto, PetDto, VisitDto, VetDto, SpecialtyDto, PetTypeDto, UserDto
- **Mapper interfaces**: OwnerMapper, PetMapper, VisitMapper, VetMapper, SpecialtyMapper, PetTypeMapper, UserMapper
- **Service**: ClinicService
- **Error handling**: BindingErrorsResponse

## Security Integration Points
- **@PreAuthorize**: Security annotations will be removed (handled in S07)
- **Role checks**: Current role-based access control (OWNER_ADMIN, VET_ADMIN) removed

## Testing Considerations
- **Characterization**: Legacy tests validate existing behavior patterns
- **Endpoint validation**: All endpoints must serve proper JSON with correct HTTP status codes
- **Error scenarios**: Tests for missing resources, validation errors, and service failures
