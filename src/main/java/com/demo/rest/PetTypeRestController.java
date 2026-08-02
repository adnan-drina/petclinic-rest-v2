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

import com.demo.security.Roles;
import jakarta.annotation.security.RolesAllowed;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import com.demo.dto.PetTypeDto;
import com.demo.mapper.PetTypeMapper;
import com.demo.model.PetType;
import com.demo.service.ClinicService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/api/pettypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetTypeRestController {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;

    @Inject
    public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }

    @GET
    @RolesAllowed({Roles.OWNER_ADMIN, Roles.VET_ADMIN})
    public Response getAllPetTypes() {
        Collection<PetTypeDto> petTypes = new ArrayList<>();
        petTypes.addAll(petTypeMapper.toPetTypeDtos(this.clinicService.findAllPetTypes()));
        if (petTypes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petTypes).build();
    }

    @GET
    @Path("/{petTypeId}")
    @RolesAllowed({Roles.OWNER_ADMIN, Roles.VET_ADMIN})
    public Response getPetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petTypeMapper.toPetTypeDto(petType)).build();
    }

    @POST
    @Transactional
    @RolesAllowed(Roles.VET_ADMIN)
    public Response addPetType(@Valid @NotNull PetTypeDto petTypeDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(null, petTypeDto.getId());
        if (bindingErrorsResponse.hasErrors()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        PetType type = petTypeMapper.toPetType(petTypeDto);
        this.clinicService.savePetType(type);
        petTypeDto.setId(type.getId());
        return Response.created(UriBuilder.fromUri("/pettypes/" + type.getId()).build()).entity(petTypeDto).build();
    }

    @PUT
    @Path("/{petTypeId}")
    @Transactional
    @RolesAllowed(Roles.VET_ADMIN)
    public Response updatePetType(@PathParam("petTypeId") int petTypeId, @Valid @NotNull PetTypeDto petTypeDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(petTypeId, petTypeDto.getId());
        if (bindingErrorsResponse.hasErrors()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentPetType.setName(petTypeDto.getName());
        this.clinicService.savePetType(currentPetType);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{petTypeId}")
    @Transactional
    @RolesAllowed(Roles.VET_ADMIN)
    public Response deletePetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deletePetType(petType);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
