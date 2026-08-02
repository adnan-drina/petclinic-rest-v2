/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import com.demo.dto.PetDto;
import com.demo.dto.PetTypeDto;
import com.demo.mapper.PetMapper;
import com.demo.model.Pet;
import com.demo.service.ClinicService;

import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetRestController {

    private final ClinicService clinicService;
    private final PetMapper petMapper;

    @Inject
    public PetRestController(ClinicService clinicService, PetMapper petMapper) {
        this.clinicService = clinicService;
        this.petMapper = petMapper;
    }

    @GET
    @Path("/{petId}")
    public Response getPet(@PathParam("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petMapper.toPetDto(pet)).build();
    }

    @GET
    public Response getPets() {
        Collection<Pet> pets = this.clinicService.findAllPets();
        if (pets.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petMapper.toPetsDto(pets)).build();
    }

    @GET
    @Path("/pettypes")
    public Response getPetTypes() {
        return Response.ok(petMapper.toPetTypeDtos(this.clinicService.findPetTypes())).build();
    }

    @POST
    @Transactional
    public Response addPet(@Valid @NotNull PetDto petDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse();
        if (petDto.getId() != null) {
            bindingErrorsResponse.addBodyIdError(null, petDto.getId());
        }
        if (bindingErrorsResponse.hasErrors() || petDto.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Pet pet = petMapper.toPet(petDto);
        this.clinicService.savePet(pet);
        petDto.setId(pet.getId());
        return Response.created(UriBuilder.fromUri("/pets/" + pet.getId()).build()).entity(petDto).build();
    }

    @PUT
    @Path("/{petId}")
    @Transactional
    public Response updatePet(@PathParam("petId") int petId, @Valid @NotNull PetDto petDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(petId, petDto.getId());
        boolean bodyIdMatchesPathId = petDto.getId() == null || petId == petDto.getId();
        if (bindingErrorsResponse.hasErrors() || !bodyIdMatchesPathId) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Pet currentPet = this.clinicService.findPetById(petId);
        if (currentPet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentPet.setBirthDate(petDto.getBirthDate());
        currentPet.setName(petDto.getName());
        currentPet.setType(petMapper.toPetType(petDto.getType()));
        this.clinicService.savePet(currentPet);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{petId}")
    @Transactional
    public Response deletePet(@PathParam("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deletePet(pet);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
