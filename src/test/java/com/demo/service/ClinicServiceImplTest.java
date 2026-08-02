package com.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.repository.OwnerRepository;
import com.demo.repository.PetRepository;
import com.demo.repository.PetTypeRepository;
import com.demo.repository.SpecialtyRepository;
import com.demo.repository.VetRepository;
import com.demo.repository.VisitRepository;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private PetTypeRepository petTypeRepository;

    private ClinicServiceImpl clinicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clinicService = new ClinicServiceImpl(
                petRepository, vetRepository, ownerRepository,
                visitRepository, specialtyRepository, petTypeRepository);
    }

    /* ---- find-by-id returns null on PersistenceException ---- */

    @Test
    void findPetByIdReturnsNullOnPersistenceException() {
        when(petRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findPetById(99));
        verify(petRepository).findById(99);
    }

    @Test
    void findPetByIdDelegatesToRepository() {
        Pet pet = new Pet();
        when(petRepository.findById(1)).thenReturn(pet);
        assertSame(pet, clinicService.findPetById(1));
        verify(petRepository).findById(1);
    }

    @Test
    void findVetByIdReturnsNullOnPersistenceException() {
        when(vetRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findVetById(99));
        verify(vetRepository).findById(99);
    }

    @Test
    void findVetByIdDelegatesToRepository() {
        Vet vet = new Vet();
        when(vetRepository.findById(1)).thenReturn(vet);
        assertSame(vet, clinicService.findVetById(1));
        verify(vetRepository).findById(1);
    }

    @Test
    void findOwnerByIdReturnsNullOnPersistenceException() {
        when(ownerRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findOwnerById(99));
        verify(ownerRepository).findById(99);
    }

    @Test
    void findOwnerByIdDelegatesToRepository() {
        Owner owner = new Owner();
        when(ownerRepository.findById(1)).thenReturn(owner);
        assertSame(owner, clinicService.findOwnerById(1));
        verify(ownerRepository).findById(1);
    }

    @Test
    void findVisitByIdReturnsNullOnPersistenceException() {
        when(visitRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findVisitById(99));
        verify(visitRepository).findById(99);
    }

    @Test
    void findVisitByIdDelegatesToRepository() {
        Visit visit = new Visit();
        when(visitRepository.findById(1)).thenReturn(visit);
        assertSame(visit, clinicService.findVisitById(1));
        verify(visitRepository).findById(1);
    }

    @Test
    void findPetTypeByIdReturnsNullOnPersistenceException() {
        when(petTypeRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findPetTypeById(99));
        verify(petTypeRepository).findById(99);
    }

    @Test
    void findPetTypeByIdDelegatesToRepository() {
        PetType petType = new PetType();
        when(petTypeRepository.findById(1)).thenReturn(petType);
        assertSame(petType, clinicService.findPetTypeById(1));
        verify(petTypeRepository).findById(1);
    }

    @Test
    void findSpecialtyByIdReturnsNullOnPersistenceException() {
        when(specialtyRepository.findById(99)).thenThrow(PersistenceException.class);
        assertNull(clinicService.findSpecialtyById(99));
        verify(specialtyRepository).findById(99);
    }

    @Test
    void findSpecialtyByIdDelegatesToRepository() {
        Specialty specialty = new Specialty();
        when(specialtyRepository.findById(1)).thenReturn(specialty);
        assertSame(specialty, clinicService.findSpecialtyById(1));
        verify(specialtyRepository).findById(1);
    }

    /* ---- findAll delegation ---- */

    @Test
    void findAllPetsDelegatesToRepository() {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet());
        when(petRepository.findAll()).thenReturn(pets);
        Collection<Pet> result = clinicService.findAllPets();
        assertSame(pets, result);
        verify(petRepository).findAll();
    }

    @Test
    void findAllVetsDelegatesToRepository() {
        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet());
        when(vetRepository.findAll()).thenReturn(vets);
        Collection<Vet> result = clinicService.findAllVets();
        assertSame(vets, result);
        verify(vetRepository).findAll();
    }

    @Test
    void findAllOwnersDelegatesToRepository() {
        List<Owner> owners = new ArrayList<>();
        when(ownerRepository.findAll()).thenReturn(owners);
        Collection<Owner> result = clinicService.findAllOwners();
        assertSame(owners, result);
        verify(ownerRepository).findAll();
    }

    @Test
    void findAllVisitsDelegatesToRepository() {
        List<Visit> visits = new ArrayList<>();
        when(visitRepository.findAll()).thenReturn(visits);
        Collection<Visit> result = clinicService.findAllVisits();
        assertSame(visits, result);
        verify(visitRepository).findAll();
    }

    @Test
    void findAllPetTypesDelegatesToRepository() {
        List<PetType> types = new ArrayList<>();
        when(petTypeRepository.findAll()).thenReturn(types);
        Collection<PetType> result = clinicService.findAllPetTypes();
        assertSame(types, result);
        verify(petTypeRepository).findAll();
    }

    @Test
    void findAllSpecialtiesDelegatesToRepository() {
        List<Specialty> specs = new ArrayList<>();
        when(specialtyRepository.findAll()).thenReturn(specs);
        Collection<Specialty> result = clinicService.findAllSpecialties();
        assertSame(specs, result);
        verify(specialtyRepository).findAll();
    }

    /* ---- save delegation ---- */

    @Test
    void savePetDelegatesToRepository() {
        Pet pet = new Pet();
        clinicService.savePet(pet);
        verify(petRepository).save(pet);
    }

    @Test
    void saveVetDelegatesToRepository() {
        Vet vet = new Vet();
        clinicService.saveVet(vet);
        verify(vetRepository).save(vet);
    }

    @Test
    void saveOwnerDelegatesToRepository() {
        Owner owner = new Owner();
        clinicService.saveOwner(owner);
        verify(ownerRepository).save(owner);
    }

    @Test
    void saveVisitDelegatesToRepository() {
        Visit visit = new Visit();
        clinicService.saveVisit(visit);
        verify(visitRepository).save(visit);
    }

    @Test
    void savePetTypeDelegatesToRepository() {
        PetType petType = new PetType();
        clinicService.savePetType(petType);
        verify(petTypeRepository).save(petType);
    }

    @Test
    void saveSpecialtyDelegatesToRepository() {
        Specialty specialty = new Specialty();
        clinicService.saveSpecialty(specialty);
        verify(specialtyRepository).save(specialty);
    }

    /* ---- delete delegation ---- */

    @Test
    void deletePetDelegatesToRepository() {
        Pet pet = new Pet();
        clinicService.deletePet(pet);
        verify(petRepository).delete(pet);
    }

    @Test
    void deleteVetDelegatesToRepository() {
        Vet vet = new Vet();
        clinicService.deleteVet(vet);
        verify(vetRepository).delete(vet);
    }

    @Test
    void deleteOwnerDelegatesToRepository() {
        Owner owner = new Owner();
        clinicService.deleteOwner(owner);
        verify(ownerRepository).delete(owner);
    }

    @Test
    void deleteVisitDelegatesToRepository() {
        Visit visit = new Visit();
        clinicService.deleteVisit(visit);
        verify(visitRepository).delete(visit);
    }

    @Test
    void deletePetTypeDelegatesToRepository() {
        PetType petType = new PetType();
        clinicService.deletePetType(petType);
        verify(petTypeRepository).delete(petType);
    }

    @Test
    void deleteSpecialtyDelegatesToRepository() {
        Specialty specialty = new Specialty();
        clinicService.deleteSpecialty(specialty);
        verify(specialtyRepository).delete(specialty);
    }

    /* ---- findVets caching with ConcurrentHashMap ---- */

    @Test
    void findVetsPopulatesCache() {
        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet());
        when(vetRepository.findAll()).thenReturn(vets);

        Collection<Vet> result = clinicService.findVets();
        assertEquals(1, result.size());
        verify(vetRepository).findAll();
    }

    @Test
    void findVetsReturnsCachedResultWithoutCallingRepository() {
        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet());
        when(vetRepository.findAll()).thenReturn(vets);

        clinicService.findVets();
        Collection<Vet> cached = clinicService.findVets();

        assertEquals(1, cached.size());
        verify(vetRepository, times(1)).findAll();
    }

    @Test
    void findVetsRefreshesAfterInterval() throws Exception {
        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet());
        when(vetRepository.findAll()).thenReturn(vets);

        clinicService.findVets();

        // O-SONARLINEFIX S2925: backdate AtomicLong last*Refresh instead of Thread.sleep
        Field lastRefresh = null;
        for (Field f : ClinicServiceImpl.class.getDeclaredFields()) {
            if (AtomicLong.class.isAssignableFrom(f.getType()) && f.getName().toLowerCase().contains("refresh")) {
                lastRefresh = f; break;
            }
        }
        org.junit.jupiter.api.Assertions.assertNotNull(lastRefresh);
        lastRefresh.setAccessible(true);
        ((AtomicLong) lastRefresh.get(clinicService)).set(System.currentTimeMillis() - 61000);

        clinicService.findVets();
        verify(vetRepository, times(2)).findAll();
    }

    /* ---- query delegation ---- */

    @Test
    void findOwnerByLastNameDelegatesToRepository() {
        List<Owner> owners = new ArrayList<>();
        when(ownerRepository.findByLastName("Davis")).thenReturn(owners);
        Collection<Owner> result = clinicService.findOwnerByLastName("Davis");
        assertSame(owners, result);
        verify(ownerRepository).findByLastName("Davis");
    }

    @Test
    void findVisitsByPetIdDelegatesToRepository() {
        List<Visit> visits = new ArrayList<>();
        when(visitRepository.findByPetId(1)).thenReturn(visits);
        Collection<Visit> result = clinicService.findVisitsByPetId(1);
        assertSame(visits, result);
        verify(visitRepository).findByPetId(1);
    }

    @Test
    void findPetTypesDelegatesToRepository() {
        List<PetType> types = new ArrayList<>();
        when(petRepository.findPetTypes()).thenReturn(types);
        Collection<PetType> result = clinicService.findPetTypes();
        assertSame(types, result);
        verify(petRepository).findPetTypes();
    }
}
