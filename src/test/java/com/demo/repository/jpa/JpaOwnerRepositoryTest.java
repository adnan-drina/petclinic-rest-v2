/*
 * Copyright 2002-2017 the original author or authors.
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
package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

/**
 * JPA-based characterization tests for {@link JpaOwnerRepositoryImpl}.
 * Verifies transactional behavior and JPA operations as required by
 * transaction-to-quarkus-00003 finding.
 * 
 * Tests focus on verifying @Transactional annotations are present on mutating methods
 * and that the repository follows Quarkus transaction patterns.
 */
class JpaOwnerRepositoryTest {

    @Test
    void classHasApplicationScopedAnnotation() {
        assertTrue(JpaOwnerRepositoryImpl.class.isAnnotationPresent(
            jakarta.enterprise.context.ApplicationScoped.class),
            "JpaOwnerRepositoryImpl should be @ApplicationScoped");
    }

    @Test
    void saveMethodHasTransactionalAnnotation() throws NoSuchMethodException {
        Method saveMethod = JpaOwnerRepositoryImpl.class.getMethod("save", 
            com.demo.model.Owner.class);
        assertTrue(saveMethod.isAnnotationPresent(Transactional.class),
            "save() method should be annotated with @Transactional");
    }

    @Test
    void deleteMethodHasTransactionalAnnotation() throws NoSuchMethodException {
        Method deleteMethod = JpaOwnerRepositoryImpl.class.getMethod("delete", 
            com.demo.model.Owner.class);
        assertTrue(deleteMethod.isAnnotationPresent(Transactional.class),
            "delete() method should be annotated with @Transactional");
    }

    @Test
    void saveMethodExistsAndIsPublic() throws NoSuchMethodException {
        Method saveMethod = JpaOwnerRepositoryImpl.class.getMethod("save", 
            com.demo.model.Owner.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(saveMethod.getModifiers()), 
            "save() method should be public");
    }

    @Test
    void deleteMethodExistsAndIsPublic() throws NoSuchMethodException {
        Method deleteMethod = JpaOwnerRepositoryImpl.class.getMethod("delete", 
            com.demo.model.Owner.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(deleteMethod.getModifiers()), 
            "delete() method should be public");
    }

    @Test
    void findByIdMethodExists() throws NoSuchMethodException {
        Method findByIdMethod = JpaOwnerRepositoryImpl.class.getMethod("findById", int.class);
        assertNotNull(findByIdMethod, "findById() method should exist");
    }

    @Test
    void findByLastNameMethodExists() throws NoSuchMethodException {
        Method findByLastNameMethod = JpaOwnerRepositoryImpl.class.getMethod("findByLastName", String.class);
        assertNotNull(findByLastNameMethod, "findByLastName() method should exist");
    }

    @Test
    void repositoryImplementsOwnerRepositoryInterface() {
        assertTrue(Arrays.asList(JpaOwnerRepositoryImpl.class.getInterfaces()).contains(
            com.demo.repository.OwnerRepository.class),
            "JpaOwnerRepositoryImpl should implement OwnerRepository");
    }

    @Test
    void noSpringRepositoryAnnotation() {
        boolean hasSpringRepo = Arrays.stream(JpaOwnerRepositoryImpl.class.getAnnotations())
            .anyMatch(a -> a.annotationType().getName().equals(
                "org.springframework.stereotype.Repository"));
        assertFalse(hasSpringRepo,
            "Should not have Spring @Repository annotation in migrated code");
    }

    @Test
    void entityManagerIsConstructorInjected() {
        Constructor<?>[] constructors = JpaOwnerRepositoryImpl.class.getConstructors();
        assertTrue(constructors.length > 0, "Should have at least one constructor");
        
        // Verify constructor with EntityManager parameter exists
        Constructor<?> entityManagerConstructor = Arrays.stream(constructors)
            .filter(c -> Arrays.asList(c.getParameterTypes()).contains(jakarta.persistence.EntityManager.class))
            .findFirst()
            .orElse(null);
            
        assertNotNull(entityManagerConstructor, 
            "Should have constructor accepting EntityManager parameter");
    }

    @Test
    void classIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(JpaOwnerRepositoryImpl.class.getModifiers()),
            "JpaOwnerRepositoryImpl should be public");
    }
}