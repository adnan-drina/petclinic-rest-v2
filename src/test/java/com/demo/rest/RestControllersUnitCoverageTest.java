package com.demo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.dto.OwnerDto;
import com.demo.dto.PetDto;
import com.demo.dto.PetTypeDto;
import com.demo.dto.SpecialtyDto;
import com.demo.dto.UserDto;
import com.demo.dto.VetDto;
import com.demo.dto.VisitDto;
import com.demo.mapper.OwnerMapper;
import com.demo.mapper.PetMapper;
import com.demo.mapper.PetTypeMapper;
import com.demo.mapper.SpecialtyMapper;
import com.demo.mapper.UserMapper;
import com.demo.mapper.VetMapper;
import com.demo.mapper.VisitMapper;
import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.User;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.service.ClinicService;
import com.demo.service.UserService;

import jakarta.ws.rs.core.Response;

/**
 * Direct controller unit coverage for Sonar new_coverage (mutate branches
 * RestAssured often misses when HTTP validation short-circuits).
 */
class RestControllersUnitCoverageTest {

    ClinicService clinicService;
    VetMapper vetMapper;
    SpecialtyMapper specialtyMapper;
    OwnerMapper ownerMapper;
    PetMapper petMapper;
    VisitMapper visitMapper;
    PetTypeMapper petTypeMapper;
    UserService userService;
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        clinicService = mock(ClinicService.class);
        vetMapper = mock(VetMapper.class);
        specialtyMapper = mock(SpecialtyMapper.class);
        ownerMapper = mock(OwnerMapper.class);
        petMapper = mock(PetMapper.class);
        visitMapper = mock(VisitMapper.class);
        petTypeMapper = mock(PetTypeMapper.class);
        userService = mock(UserService.class);
        userMapper = mock(UserMapper.class);
    }

    @Test
    void vetControllerBranches() {
        VetRestController c = new VetRestController(clinicService, vetMapper, specialtyMapper);
        when(clinicService.findAllVets()).thenReturn(new ArrayList<>());
        assertEquals(404, c.getAllVets().getStatus());
        Vet vet = new Vet();
        vet.setId(1);
        when(clinicService.findAllVets()).thenReturn(List.of(vet));
        when(vetMapper.toVetDtos(anyCollection())).thenReturn(List.of(new VetDto()));
        assertEquals(200, c.getAllVets().getStatus());

        when(clinicService.findVetById(1)).thenReturn(vet);
        when(vetMapper.toVetDto(vet)).thenReturn(new VetDto());
        assertEquals(200, c.getVet(1).getStatus());
        when(clinicService.findVetById(9)).thenReturn(null);
        assertEquals(404, c.getVet(9).getStatus());

        VetDto dto = new VetDto();
        dto.setFirstName("A");
        dto.setLastName("B");
        when(vetMapper.toVet(dto)).thenReturn(vet);
        doNothing().when(clinicService).saveVet(any());
        assertEquals(201, c.addVet(dto).getStatus());
        VetDto withId = new VetDto();
        withId.setId(3);
        assertEquals(400, c.addVet(withId).getStatus());

        VetDto upd = new VetDto();
        upd.setId(1);
        upd.setFirstName("U");
        upd.setLastName("V");
        upd.setSpecialties(new ArrayList<>());
        when(clinicService.findVetById(1)).thenReturn(vet);
        when(specialtyMapper.toSpecialtys(any())).thenReturn(new ArrayList<>());
        assertEquals(204, c.updateVet(1, upd).getStatus());
        when(clinicService.findVetById(2)).thenReturn(null);
        VetDto upd2 = new VetDto();
        upd2.setId(2);
        assertEquals(404, c.updateVet(2, upd2).getStatus());
        VetDto mismatch = new VetDto();
        mismatch.setId(9);
        assertEquals(400, c.updateVet(1, mismatch).getStatus());

        when(clinicService.findVetById(1)).thenReturn(vet);
        assertEquals(204, c.deleteVet(1).getStatus());
        when(clinicService.findVetById(9)).thenReturn(null);
        assertEquals(404, c.deleteVet(9).getStatus());
    }

    @Test
    void specialtyAndPetTypeBranches() {
        SpecialtyRestController sc = new SpecialtyRestController(clinicService, specialtyMapper);
        when(clinicService.findAllSpecialties()).thenReturn(new ArrayList<>());
        when(specialtyMapper.toSpecialtyDtos(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, sc.getAllSpecialtys().getStatus());
        when(specialtyMapper.toSpecialtyDtos(anyCollection())).thenReturn(List.of(new SpecialtyDto()));
        when(clinicService.findAllSpecialties()).thenReturn(List.of(new Specialty()));
        assertEquals(200, sc.getAllSpecialtys().getStatus());
        Specialty specialty = new Specialty();
        specialty.setId(1);
        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDto(specialty)).thenReturn(new SpecialtyDto());
        assertEquals(200, sc.getSpecialty(1).getStatus());
        when(clinicService.findSpecialtyById(9)).thenReturn(null);
        assertEquals(404, sc.getSpecialty(9).getStatus());
        SpecialtyDto sd = new SpecialtyDto();
        sd.setName("s");
        when(specialtyMapper.toSpecialty(sd)).thenReturn(specialty);
        doNothing().when(clinicService).saveSpecialty(any());
        assertEquals(201, sc.addSpecialty(sd).getStatus());
        SpecialtyDto sid = new SpecialtyDto();
        sid.setId(1);
        sid.setName("s");
        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        assertEquals(204, sc.updateSpecialty(1, sid).getStatus());
        when(clinicService.findSpecialtyById(2)).thenReturn(null);
        SpecialtyDto sid2 = new SpecialtyDto();
        sid2.setId(2);
        assertEquals(404, sc.updateSpecialty(2, sid2).getStatus());
        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        assertEquals(204, sc.deleteSpecialty(1).getStatus());
        when(clinicService.findSpecialtyById(9)).thenReturn(null);
        assertEquals(404, sc.deleteSpecialty(9).getStatus());

        PetTypeRestController pc = new PetTypeRestController(clinicService, petTypeMapper);
        when(clinicService.findAllPetTypes()).thenReturn(new ArrayList<>());
        when(petTypeMapper.toPetTypeDtos(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, pc.getAllPetTypes().getStatus());
        when(petTypeMapper.toPetTypeDtos(anyCollection())).thenReturn(List.of(new PetTypeDto()));
        when(clinicService.findAllPetTypes()).thenReturn(List.of(new PetType()));
        assertEquals(200, pc.getAllPetTypes().getStatus());
        PetType pt = new PetType();
        pt.setId(1);
        when(clinicService.findPetTypeById(1)).thenReturn(pt);
        when(petTypeMapper.toPetTypeDto(pt)).thenReturn(new PetTypeDto());
        assertEquals(200, pc.getPetType(1).getStatus());
        when(clinicService.findPetTypeById(9)).thenReturn(null);
        assertEquals(404, pc.getPetType(9).getStatus());
        PetTypeDto ptd = new PetTypeDto();
        ptd.setName("t");
        when(petTypeMapper.toPetType(ptd)).thenReturn(pt);
        doNothing().when(clinicService).savePetType(any());
        assertEquals(201, pc.addPetType(ptd).getStatus());
        PetTypeDto ptd1 = new PetTypeDto();
        ptd1.setId(1);
        ptd1.setName("t");
        when(clinicService.findPetTypeById(1)).thenReturn(pt);
        assertEquals(204, pc.updatePetType(1, ptd1).getStatus());
        when(clinicService.findPetTypeById(2)).thenReturn(null);
        PetTypeDto ptd2 = new PetTypeDto();
        ptd2.setId(2);
        assertEquals(404, pc.updatePetType(2, ptd2).getStatus());
        when(clinicService.findPetTypeById(1)).thenReturn(pt);
        assertEquals(204, pc.deletePetType(1).getStatus());
        when(clinicService.findPetTypeById(9)).thenReturn(null);
        assertEquals(404, pc.deletePetType(9).getStatus());
    }

    @Test
    void ownerPetVisitUserRootBranches() throws Exception {
        OwnerRestController oc = new OwnerRestController(clinicService, ownerMapper);
        when(clinicService.findOwnerByLastName("X")).thenReturn(new ArrayList<>());
        when(ownerMapper.toOwnerDtoCollection(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, oc.getOwnersList("X").getStatus());
        when(clinicService.findOwnerByLastName("Y")).thenReturn(List.of(new Owner()));
        when(ownerMapper.toOwnerDtoCollection(anyCollection())).thenReturn(List.of(new OwnerDto()));
        assertEquals(200, oc.getOwnersList("Y").getStatus());
        when(clinicService.findAllOwners()).thenReturn(new ArrayList<>());
        when(ownerMapper.toOwnerDtoCollection(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, oc.getOwners().getStatus());
        when(clinicService.findAllOwners()).thenReturn(List.of(new Owner()));
        when(ownerMapper.toOwnerDtoCollection(anyCollection())).thenReturn(List.of(new OwnerDto()));
        assertEquals(200, oc.getOwners().getStatus());
        Owner owner = new Owner();
        owner.setId(1);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(new OwnerDto());
        assertEquals(200, oc.getOwner(1).getStatus());
        when(clinicService.findOwnerById(9)).thenReturn(null);
        assertEquals(404, oc.getOwner(9).getStatus());
        OwnerDto od = new OwnerDto();
        od.setFirstName("a");
        od.setLastName("b");
        when(ownerMapper.toOwner(od)).thenReturn(owner);
        doNothing().when(clinicService).saveOwner(any());
        assertEquals(201, oc.addOwner(od).getStatus());
        OwnerDto od1 = new OwnerDto();
        od1.setId(1);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        assertEquals(204, oc.updateOwner(1, od1).getStatus());
        when(clinicService.findOwnerById(2)).thenReturn(null);
        OwnerDto od2 = new OwnerDto();
        od2.setId(2);
        assertEquals(404, oc.updateOwner(2, od2).getStatus());
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        assertEquals(204, oc.deleteOwner(1).getStatus());
        when(clinicService.findOwnerById(9)).thenReturn(null);
        assertEquals(404, oc.deleteOwner(9).getStatus());

        PetRestController pc = new PetRestController(clinicService, petMapper);
        Pet pet = new Pet();
        pet.setId(1);
        when(clinicService.findPetById(1)).thenReturn(pet);
        when(petMapper.toPetDto(pet)).thenReturn(new PetDto());
        assertEquals(200, pc.getPet(1).getStatus());
        when(clinicService.findPetById(9)).thenReturn(null);
        assertEquals(404, pc.getPet(9).getStatus());
        when(clinicService.findAllPets()).thenReturn(new ArrayList<>());
        when(petMapper.toPetsDto(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, pc.getPets().getStatus());
        when(clinicService.findAllPets()).thenReturn(List.of(pet));
        when(petMapper.toPetsDto(anyCollection())).thenReturn(List.of(new PetDto()));
        assertEquals(200, pc.getPets().getStatus());
        when(clinicService.findPetTypes()).thenReturn(List.of(new PetType()));
        when(petMapper.toPetTypeDtos(anyCollection())).thenReturn(List.of(new PetTypeDto()));
        assertEquals(200, pc.getPetTypes().getStatus());
        PetDto pd = new PetDto();
        pd.setName("p");
        when(petMapper.toPet(pd)).thenReturn(pet);
        doNothing().when(clinicService).savePet(any());
        assertEquals(201, pc.addPet(pd).getStatus());
        PetDto pd1 = new PetDto();
        pd1.setId(1);
        pd1.setType(new PetTypeDto());
        when(clinicService.findPetById(1)).thenReturn(pet);
        when(petMapper.toPetType(any())).thenReturn(new PetType());
        assertEquals(204, pc.updatePet(1, pd1).getStatus());
        when(clinicService.findPetById(2)).thenReturn(null);
        PetDto pd2 = new PetDto();
        pd2.setId(2);
        assertEquals(404, pc.updatePet(2, pd2).getStatus());
        when(clinicService.findPetById(1)).thenReturn(pet);
        assertEquals(204, pc.deletePet(1).getStatus());
        when(clinicService.findPetById(9)).thenReturn(null);
        assertEquals(404, pc.deletePet(9).getStatus());

        VisitRestController vc = new VisitRestController(clinicService, visitMapper);
        when(clinicService.findAllVisits()).thenReturn(new ArrayList<>());
        when(visitMapper.toVisitsDto(anyCollection())).thenReturn(new ArrayList<>());
        assertEquals(404, vc.getAllVisitDtos().getStatus());
        when(clinicService.findAllVisits()).thenReturn(List.of(new Visit()));
        when(visitMapper.toVisitsDto(anyCollection())).thenReturn(List.of(new VisitDto()));
        assertEquals(200, vc.getAllVisitDtos().getStatus());
        Visit visit = new Visit();
        visit.setId(1);
        when(clinicService.findVisitById(1)).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(new VisitDto());
        assertEquals(200, vc.getVisitDto(1).getStatus());
        when(clinicService.findVisitById(9)).thenReturn(null);
        assertEquals(404, vc.getVisitDto(9).getStatus());
        VisitDto vd = new VisitDto();
        when(visitMapper.toVisit(vd)).thenReturn(visit);
        doNothing().when(clinicService).saveVisit(any());
        assertEquals(201, vc.addVisit(vd).getStatus());
        VisitDto vd1 = new VisitDto();
        vd1.setId(1);
        when(clinicService.findVisitById(1)).thenReturn(visit);
        assertEquals(204, vc.updateVisit(1, vd1).getStatus());
        when(clinicService.findVisitById(2)).thenReturn(null);
        VisitDto vd2 = new VisitDto();
        vd2.setId(2);
        assertEquals(404, vc.updateVisit(2, vd2).getStatus());
        when(clinicService.findVisitById(1)).thenReturn(visit);
        assertEquals(204, vc.deleteVisit(1).getStatus());
        when(clinicService.findVisitById(9)).thenReturn(null);
        assertEquals(404, vc.deleteVisit(9).getStatus());

        UserRestController uc = new UserRestController(userService, userMapper);
        UserDto ud = new UserDto();
        ud.setUsername("u");
        when(userMapper.toUser(ud)).thenReturn(new User());
        doNothing().when(userService).saveUser(any());
        assertEquals(201, uc.addOwner(ud).getStatus());
        verify(userService).saveUser(any());

        assertEquals(307, new RootRestController().redirectToSwagger().getStatus());
    }
}
