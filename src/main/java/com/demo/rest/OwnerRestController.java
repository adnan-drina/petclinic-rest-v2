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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.transaction.Transactional;

import com.demo.dto.OwnerDto;
import com.demo.mapper.OwnerMapper;
import com.demo.model.Owner;
import com.demo.service.ClinicService;

import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OwnerRestController {

    private final ClinicService clinicService;
    private final OwnerMapper ownerMapper;

    @Inject
    public OwnerRestController(ClinicService clinicService, OwnerMapper ownerMapper) {
        this.clinicService = clinicService;
        this.ownerMapper = ownerMapper;
    }

    @GET
    @Path("/*/lastname/{lastName}")
    public Response getOwnersList(@PathParam("lastName") String ownerLastName) {
        if (ownerLastName == null) {
            ownerLastName = "";
        }
        Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
        if (owners.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDtoCollection(owners)).build();
    }

    @GET
    public Response getOwners() {
        Collection<Owner> owners = this.clinicService.findAllOwners();
        if (owners.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDtoCollection(owners)).build();
    }

    @GET
    @Path("/{ownerId}")
    public Response getOwner(@PathParam("ownerId") int ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDto(owner)).build();
    }

    @POST
    @Transactional
    public Response addOwner(@Valid @NotNull OwnerDto ownerDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse();
        if (ownerDto.getId() != null) {
            bindingErrorsResponse.addBodyIdError(null, ownerDto.getId());
        }
        if (bindingErrorsResponse.hasErrors() || ownerDto.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Owner owner = ownerMapper.toOwner(ownerDto);
        this.clinicService.saveOwner(owner);
        ownerDto.setId(owner.getId());
        return Response.created(UriBuilder.fromUri("/owners/" + owner.getId()).build()).entity(ownerDto).build();
    }

    @PUT
    @Path("/{ownerId}")
    @Transactional
    public Response updateOwner(@PathParam("ownerId") int ownerId, @Valid @NotNull OwnerDto ownerDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(ownerId, ownerDto.getId());
        boolean bodyIdMatchesPathId = ownerDto.getId() == null || ownerId == ownerDto.getId();
        if (bindingErrorsResponse.hasErrors() || !bodyIdMatchesPathId) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Owner currentOwner = this.clinicService.findOwnerById(ownerId);
        if (currentOwner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentOwner.setAddress(ownerDto.getAddress());
        currentOwner.setCity(ownerDto.getCity());
        currentOwner.setFirstName(ownerDto.getFirstName());
        currentOwner.setLastName(ownerDto.getLastName());
        currentOwner.setTelephone(ownerDto.getTelephone());
        this.clinicService.saveOwner(currentOwner);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{ownerId}")
    @Transactional
    public Response deleteOwner(@PathParam("ownerId") int ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteOwner(owner);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
