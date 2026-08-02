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

import com.demo.dto.SpecialtyDto;
import com.demo.mapper.SpecialtyMapper;
import com.demo.model.Specialty;
import com.demo.service.ClinicService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/api/specialties")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed(Roles.VET_ADMIN)
public class SpecialtyRestController {

    private final ClinicService clinicService;
    private final SpecialtyMapper specialtyMapper;

    @Inject
    public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.specialtyMapper = specialtyMapper;
    }

    @GET
    public Response getAllSpecialtys() {
        Collection<SpecialtyDto> specialties = new ArrayList<>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialties).build();
    }

    @GET
    @Path("/{specialtyId}")
    public Response getSpecialty(@PathParam("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialtyMapper.toSpecialtyDto(specialty)).build();
    }

    @POST
    @Transactional
    public Response addSpecialty(@Valid @NotNull SpecialtyDto specialtyDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(null, specialtyDto.getId());
        if (bindingErrorsResponse.hasErrors()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        this.clinicService.saveSpecialty(specialty);
        specialtyDto.setId(specialty.getId());
        return Response.created(UriBuilder.fromUri("/specialties/" + specialty.getId()).build()).entity(specialtyDto).build();
    }

    @PUT
    @Path("/{specialtyId}")
    @Transactional
    public Response updateSpecialty(@PathParam("specialtyId") int specialtyId, @Valid @NotNull SpecialtyDto specialtyDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(specialtyId, specialtyDto.getId());
        if (bindingErrorsResponse.hasErrors()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentSpecialty.setName(specialtyDto.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{specialtyId}")
    @Transactional
    public Response deleteSpecialty(@PathParam("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteSpecialty(specialty);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
