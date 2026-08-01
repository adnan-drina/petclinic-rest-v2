/**
 * Domain model entities migrated from Spring PetClinic.
 *
 * <p>JPA entities using jakarta.persistence annotations, validated
 * with jakarta.validation constraints. Entities are mapped to DTOs
 * via MapStruct mappers in {@link com.demo.mapper}.</p>
 *
 * <p>Entity hierarchy:
 * <ul>
 *   <li>{@link BaseEntity} — base class with @Id</li>
 *   <li>{@link NamedEntity} — extends BaseEntity, adds name field</li>
 *   <li>{@link Person} — extends BaseEntity, contact information</li>
 * </ul>
 * </p>
 *
 * <p>Business entities: {@link Owner}, {@link Pet}, {@link PetType},
 * {@link Vet}, {@link Visit}, {@link Specialty}.</p>
 *
 * <p>Security entities: {@link User}, {@link Role}.</p>
 */
package com.demo.model;
