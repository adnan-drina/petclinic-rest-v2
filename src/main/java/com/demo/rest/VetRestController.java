/*
 * Copyright 2016-2018 the original author or authors.
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

import com.demo.dto.VetDto;
import com.demo.mapper.SpecialtyMapper;
import com.demo.mapper.VetMapper;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.service.ClinicService;

import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/vets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VetRestController {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @Inject
    public VetRestController(ClinicService clinicService, VetMapper vetMapper, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.vetMapper = vetMapper;
        this.specialtyMapper = specialtyMapper;
    }

    @GET
    public Response getAllVets() {
        Collection<Vet> vets = this.clinicService.findAllVets();
        if (vets.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vetMapper.toVetDtos(vets)).build();
    }

    @GET
    @Path("/{vetId}")
    public Response getVet(@PathParam("vetId") int vetId) {
        Vet vet = this.clinicService.findVetById(vetId);
        if (vet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vetMapper.toVetDto(vet)).build();
    }

    @POST
    @Transactional
    public Response addVet(@Valid @NotNull VetDto vetDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse();
        if (vetDto.getId() != null) {
            bindingErrorsResponse.addBodyIdError(null, vetDto.getId());
        }
        if (bindingErrorsResponse.hasErrors() || vetDto.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Vet vet = vetMapper.toVet(vetDto);
        this.clinicService.saveVet(vet);
        vetDto.setId(vet.getId());
        return Response.created(UriBuilder.fromUri("/vets/" + vet.getId()).build()).entity(vetDto).build();
    }

    @PUT
    @Path("/{vetId}")
    @Transactional
    public Response updateVet(@PathParam("vetId") int vetId, @Valid @NotNull VetDto vetDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(vetId, vetDto.getId());
        boolean bodyIdMatchesPathId = vetDto.getId() == null || vetId == vetDto.getId();
        if (bindingErrorsResponse.hasErrors() || !bodyIdMatchesPathId) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Vet currentVet = this.clinicService.findVetById(vetId);
        if (currentVet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentVet.setFirstName(vetDto.getFirstName());
        currentVet.setLastName(vetDto.getLastName());
        currentVet.clearSpecialties();
        for (Specialty spec : specialtyMapper.toSpecialtys(vetDto.getSpecialties())) {
            currentVet.addSpecialty(spec);
        }
        this.clinicService.saveVet(currentVet);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{vetId}")
    @Transactional
    public Response deleteVet(@PathParam("vetId") int vetId) {
        Vet vet = this.clinicService.findVetById(vetId);
        if (vet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteVet(vet);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
