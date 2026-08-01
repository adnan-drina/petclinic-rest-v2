package com.demo.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * OwnerAllOfDto
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-01T12:13:14.412618866Z[GMT]")
public class OwnerAllOfDto   {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("pets")
  @Valid
  private List<PetDto> pets = new ArrayList<>();

  public OwnerAllOfDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet owner.
   * minimum: 0
   * @return id
  */

@Min(0)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public OwnerAllOfDto pets(List<PetDto> pets) {
    this.pets = pets;
    return this;
  }

  public OwnerAllOfDto addPetsItem(PetDto petsItem) {
    if (this.pets == null) {
      this.pets = new ArrayList<>();
    }
    this.pets.add(petsItem);
    return this;
  }

  /**
   * The pets owned by this individual including any booked vet visits.
   * @return pets
  */
  @NotNull

  @Valid

  public List<PetDto> getPets() {
    return pets;
  }

  public void setPets(List<PetDto> pets) {
    this.pets = pets;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OwnerAllOfDto ownerAllOf = (OwnerAllOfDto) o;
    return Objects.equals(this.id, ownerAllOf.id) &&
        Objects.equals(this.pets, ownerAllOf.pets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, pets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OwnerAllOfDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

