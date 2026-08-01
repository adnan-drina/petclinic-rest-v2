package com.demo.service;

import java.util.Collection;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.model.Visit;

/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
public interface ClinicService {

    Pet findPetById(int id);
    Collection<Pet> findAllPets();
    void savePet(Pet pet);
    void deletePet(Pet pet);

    Collection<Visit> findVisitsByPetId(int petId);
    Visit findVisitById(int visitId);
    Collection<Visit> findAllVisits();
    void saveVisit(Visit visit);
    void deleteVisit(Visit visit);

    Vet findVetById(int id);
    Collection<Vet> findVets();
    Collection<Vet> findAllVets();
    void saveVet(Vet vet);
    void deleteVet(Vet vet);

    Owner findOwnerById(int id);
    Collection<Owner> findAllOwners();
    void saveOwner(Owner owner);
    void deleteOwner(Owner owner);
    Collection<Owner> findOwnerByLastName(String lastName);

    PetType findPetTypeById(int petTypeId);
    Collection<PetType> findAllPetTypes();
    Collection<PetType> findPetTypes();
    void savePetType(PetType petType);
    void deletePetType(PetType petType);

    Specialty findSpecialtyById(int specialtyId);
    Collection<Specialty> findAllSpecialties();
    void saveSpecialty(Specialty specialty);
    void deleteSpecialty(Specialty specialty);

}
