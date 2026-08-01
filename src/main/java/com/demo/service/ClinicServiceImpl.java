package com.demo.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

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

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and caching annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
public class ClinicServiceImpl implements ClinicService {

    private static final String VETS_CACHE_KEY = "vets";
    private static final long CACHE_REFRESH_INTERVAL_MS = 60_000L;

    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PetTypeRepository petTypeRepository;

    private final ConcurrentHashMap<String, Collection<Vet>> vetsCache;
    private final AtomicLong lastVetsRefresh;

    @Inject
    public ClinicServiceImpl(
            PetRepository petRepository,
            VetRepository vetRepository,
            OwnerRepository ownerRepository,
            VisitRepository visitRepository,
            SpecialtyRepository specialtyRepository,
            PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository;
        this.petTypeRepository = petTypeRepository;
        this.vetsCache = new ConcurrentHashMap<>();
        this.lastVetsRefresh = new AtomicLong(0);
    }

    @Override
    @Transactional
    public Collection<Pet> findAllPets() throws PersistenceException {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public void deletePet(Pet pet) throws PersistenceException {
        petRepository.delete(pet);
    }

    @Override
    @Transactional
    public Visit findVisitById(int visitId) {
        try {
            return visitRepository.findById(visitId);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Collection<Visit> findAllVisits() throws PersistenceException {
        return visitRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteVisit(Visit visit) throws PersistenceException {
        visitRepository.delete(visit);
    }

    @Override
    @Transactional
    public Vet findVetById(int id) {
        try {
            return vetRepository.findById(id);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Collection<Vet> findAllVets() throws PersistenceException {
        return vetRepository.findAll();
    }

    @Override
    @Transactional
    public void saveVet(Vet vet) throws PersistenceException {
        vetRepository.save(vet);
    }

    @Override
    @Transactional
    public void deleteVet(Vet vet) throws PersistenceException {
        vetRepository.delete(vet);
    }

    @Override
    @Transactional
    public Collection<Owner> findAllOwners() throws PersistenceException {
        return ownerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOwner(Owner owner) throws PersistenceException {
        ownerRepository.delete(owner);
    }

    @Override
    @Transactional
    public PetType findPetTypeById(int petTypeId) {
        try {
            return petTypeRepository.findById(petTypeId);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Collection<PetType> findAllPetTypes() throws PersistenceException {
        return petTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void savePetType(PetType petType) throws PersistenceException {
        petTypeRepository.save(petType);
    }

    @Override
    @Transactional
    public void deletePetType(PetType petType) throws PersistenceException {
        petTypeRepository.delete(petType);
    }

    @Override
    @Transactional
    public Specialty findSpecialtyById(int specialtyId) {
        try {
            return specialtyRepository.findById(specialtyId);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Collection<Specialty> findAllSpecialties() throws PersistenceException {
        return specialtyRepository.findAll();
    }

    @Override
    @Transactional
    public void saveSpecialty(Specialty specialty) throws PersistenceException {
        specialtyRepository.save(specialty);
    }

    @Override
    @Transactional
    public void deleteSpecialty(Specialty specialty) throws PersistenceException {
        specialtyRepository.delete(specialty);
    }

    @Override
    @Transactional
    public Collection<PetType> findPetTypes() throws PersistenceException {
        return petRepository.findPetTypes();
    }

    @Override
    @Transactional
    public Owner findOwnerById(int id) {
        try {
            return ownerRepository.findById(id);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Pet findPetById(int id) {
        try {
            return petRepository.findById(id);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void savePet(Pet pet) throws PersistenceException {
        petRepository.save(pet);
    }

    @Override
    @Transactional
    public void saveVisit(Visit visit) throws PersistenceException {
        visitRepository.save(visit);
    }

    @Override
    @Transactional
    public Collection<Vet> findVets() throws PersistenceException {
        return vetsCache.compute(VETS_CACHE_KEY, (key, cached) -> {
            if (cached != null) {
                long now = System.currentTimeMillis();
                if (now - lastVetsRefresh.get() < CACHE_REFRESH_INTERVAL_MS) {
                    return cached;
                }
            }
            Collection<Vet> vets = vetRepository.findAll();
            lastVetsRefresh.set(System.currentTimeMillis());
            return vets;
        });
    }

    @Override
    @Transactional
    public void saveOwner(Owner owner) throws PersistenceException {
        ownerRepository.save(owner);
    }

    @Override
    @Transactional
    public Collection<Owner> findOwnerByLastName(String lastName) throws PersistenceException {
        return ownerRepository.findByLastName(lastName);
    }

    @Override
    @Transactional
    public Collection<Visit> findVisitsByPetId(int petId) {
        return visitRepository.findByPetId(petId);
    }

}
