package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void extendsPerson() {
        Owner owner = new Owner();
        assertTrue(owner instanceof Person);
    }

    @Test
    void extendsBaseEntity() {
        Owner owner = new Owner();
        assertTrue(owner instanceof BaseEntity);
    }

    @Test
    void addressIsNullInitially() {
        Owner owner = new Owner();
        assertNull(owner.getAddress());
    }

    @Test
    void setAddressUpdatesAddress() {
        Owner owner = new Owner();
        owner.setAddress("123 Main St");
        assertEquals("123 Main St", owner.getAddress());
    }

    @Test
    void cityIsNullInitially() {
        Owner owner = new Owner();
        assertNull(owner.getCity());
    }

    @Test
    void setCityUpdatesCity() {
        Owner owner = new Owner();
        owner.setCity("Springfield");
        assertEquals("Springfield", owner.getCity());
    }

    @Test
    void telephoneIsNullInitially() {
        Owner owner = new Owner();
        assertNull(owner.getTelephone());
    }

    @Test
    void setTelephoneUpdatesTelephone() {
        Owner owner = new Owner();
        owner.setTelephone("555-1234");
        assertEquals("555-1234", owner.getTelephone());
    }

    @Test
    void petsIsEmptyInitially() {
        Owner owner = new Owner();
        List<Pet> pets = owner.getPets();
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void getPetsReturnsUnmodifiableList() {
        Owner owner = new Owner();
        List<Pet> pets = owner.getPets();
        Pet pet = new Pet();
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> pets.add(pet));
        assertNotNull(exception);
    }

    @Test
    void getPetsReturnsSortedByName() {
        Owner owner = new Owner();
        
        Pet pet1 = new Pet();
        pet1.setName("Zebra");
        owner.addPet(pet1);
        
        Pet pet2 = new Pet();
        pet2.setName("Apple");
        owner.addPet(pet2);
        
        List<Pet> pets = owner.getPets();
        assertEquals(2, pets.size());
        assertEquals("Apple", pets.get(0).getName());
        assertEquals("Zebra", pets.get(1).getName());
    }

    @Test
    void addPetSetsBackReference() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        owner.addPet(pet);
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void setPetsReplacesAllPets() {
        Owner owner = new Owner();
        Pet pet1 = new Pet();
        pet1.setName("Buddy");
        owner.addPet(pet1);
        
        assertEquals(1, owner.getPets().size());

        Pet pet2 = new Pet();
        pet2.setName("Max");
        owner.setPets(List.of(pet2));
        
        assertEquals(1, owner.getPets().size());
        assertEquals("Max", owner.getPets().get(0).getName());
    }

    @Test
    void getPetReturnsNullWhenNotFound() {
        Owner owner = new Owner();
        Pet pet = owner.getPet("Nonexistent");
        assertNull(pet);
    }

    @Test
    void getPetFindsPetByName() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);
        
        Pet found = owner.getPet("Fluffy");
        assertEquals(pet, found);
    }

    @Test
    void getPetIsCaseInsensitive() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Fluffy");
        owner.addPet(pet);
        
        Pet found = owner.getPet("fluffy");
        assertEquals(pet, found);
    }

    @Test
    void getPetIgnoresNewPetsWhenFlagSet() {
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setId(1);
        existingPet.setName("OldPet");
        owner.addPet(existingPet);
        
        Pet newPet = new Pet();
        newPet.setName("NewPet");
        owner.addPet(newPet);
        
        Pet found = owner.getPet("NewPet", true);
        assertNull(found);
        
        Pet foundIncludingNew = owner.getPet("NewPet", false);
        assertEquals(newPet, foundIncludingNew);
    }

    @Test
    void toStringIncludesAllFields() {
        Owner owner = new Owner();
        owner.setId(5);
        owner.setFirstName("Bob");
        owner.setLastName("Johnson");
        owner.setAddress("456 Oak Ave");
        owner.setCity("Portland");
        owner.setTelephone("555-9876");
        
        String result = owner.toString();
        assertTrue(result.contains("id=5"));
        assertTrue(result.contains("lastName=Johnson"));
        assertTrue(result.contains("firstName=Bob"));
        assertTrue(result.contains("address=456 Oak Ave"));
        assertTrue(result.contains("city=Portland"));
        assertTrue(result.contains("telephone=555-9876"));
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Owner owner = new Owner();
        assertTrue(owner.isNew());
        owner.setId(10);
        assertEquals(10, owner.getId());
        assertFalse(owner.isNew());
    }

    @Test
    void inheritsFirstNameAndLastNameFromPerson() {
        Owner owner = new Owner();
        owner.setFirstName("Jane");
        owner.setLastName("Smith");
        assertEquals("Jane", owner.getFirstName());
        assertEquals("Smith", owner.getLastName());
    }
}