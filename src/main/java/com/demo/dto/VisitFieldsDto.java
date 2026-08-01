package com.demo.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Editable fields of a vet visit.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-01T12:13:14.412618866Z[GMT]")
public class VisitFieldsDto   {
  @JsonProperty("date")
  private LocalDate date;

  @JsonProperty("description")
  private String description;

  public VisitFieldsDto date(LocalDate date) {
    this.date = date;
    return this;
  }

  /**
   * The date of the visit.
   * @return date
  */

  @Valid

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public VisitFieldsDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * The description for the visit.
   * @return description
  */
  @NotNull

@Size(min=1,max=255) 
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisitFieldsDto visitFields = (VisitFieldsDto) o;
    return Objects.equals(this.date, visitFields.date) &&
        Objects.equals(this.description, visitFields.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VisitFieldsDto {\n");
    
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

