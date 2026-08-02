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

import com.demo.dto.VisitDto;
import com.demo.mapper.VisitMapper;
import com.demo.model.Visit;
import com.demo.service.ClinicService;

import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RequestScoped
@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitRestController {

    private final ClinicService clinicService;
    private final VisitMapper visitMapper;

    @Inject
    public VisitRestController(ClinicService clinicService, VisitMapper visitMapper) {
        this.clinicService = clinicService;
        this.visitMapper = visitMapper;
    }

    @GET
    public Response getAllVisitDtos() {
        Collection<Visit> visits = this.clinicService.findAllVisits();
        if (visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(visitMapper.toVisitsDto(visits)).build();
    }

    @GET
    @Path("/{visitId}")
    public Response getVisitDto(@PathParam("visitId") int visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(visitMapper.toVisitDto(visit)).build();
    }

    @POST
    @Transactional
    public Response addVisit(@Valid @NotNull VisitDto visitDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse();
        if (visitDto.getId() != null) {
            bindingErrorsResponse.addBodyIdError(null, visitDto.getId());
        }
        if (bindingErrorsResponse.hasErrors() || visitDto.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Visit visit = visitMapper.toVisit(visitDto);
        this.clinicService.saveVisit(visit);
        visitDto.setId(visit.getId());
        return Response.created(UriBuilder.fromUri("/visits/" + visit.getId()).build()).entity(visitDto).build();
    }

    @PUT
    @Path("/{visitId}")
    @Transactional
    public Response updateVisit(@PathParam("visitId") int visitId, @Valid @NotNull VisitDto visitDto) {
        BindingErrorsResponse bindingErrorsResponse = new BindingErrorsResponse(visitId, visitDto.getId());
        boolean bodyIdMatchesPathId = visitDto.getId() == null || visitId == visitDto.getId();
        if (bindingErrorsResponse.hasErrors() || !bodyIdMatchesPathId) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(bindingErrorsResponse)
                .build();
        }
        Visit currentVisit = this.clinicService.findVisitById(visitId);
        if (currentVisit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        currentVisit.setDate(visitDto.getDate());
        currentVisit.setDescription(visitDto.getDescription());
        this.clinicService.saveVisit(currentVisit);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{visitId}")
    @Transactional
    public Response deleteVisit(@PathParam("visitId") int visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteVisit(visit);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
