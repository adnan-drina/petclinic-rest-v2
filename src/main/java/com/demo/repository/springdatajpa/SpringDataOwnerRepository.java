/*
 * Copyright 2002-2013 the original author or authors.
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
package com.demo.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.repository.Repository;
import com.demo.model.Owner;
import com.demo.repository.OwnerRepository;

/**
 * Spring Data-shaped specialization of {@link OwnerRepository}. Fetch-join
 * queries live in {@code JpaOwnerRepositoryImpl}; Quarkus Spring Data JPA
 * extension is not used (avoids dual Arc implementations).
 */
public interface SpringDataOwnerRepository extends OwnerRepository, Repository<Owner, Integer> {

    @Override
    Collection<Owner> findByLastName(String lastName);

    @Override
    Owner findById(int id);
}
