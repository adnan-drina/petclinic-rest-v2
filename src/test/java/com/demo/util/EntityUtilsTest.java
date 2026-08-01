package com.demo.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.demo.model.BaseEntity;


class EntityUtilsTest {

    static class TestEntity extends BaseEntity {
        private String name;

        public TestEntity(int id, String name) {
            this.name = name;
            setId(id);
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void getByIdReturnsEntityWhenFound() {
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(1, "Entity1"),
            new TestEntity(2, "Entity2"),
            new TestEntity(3, "Entity3")
        );

        TestEntity result = EntityUtils.getById(entities, TestEntity.class, 2);

        assertEquals(2, result.getId());
        assertEquals("Entity2", result.getName());
    }

    @Test
    void getByIdThrowsExceptionWhenNotFound() {
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(1, "Entity1"),
            new TestEntity(2, "Entity2")
        );

        ObjectRetrievalFailureException exception = assertThrows(
            ObjectRetrievalFailureException.class,
            () -> EntityUtils.getById(entities, TestEntity.class, 99)
        );

        assertEquals(TestEntity.class, exception.getEntityClass());
        assertEquals(99, exception.getEntityId());
    }

    @Test
    void getByIdFindsFirstMatchingEntity() {
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(1, "Entity1"),
            new TestEntity(2, "Entity2"),
            new TestEntity(1, "Duplicate1")  // Same ID, different entity
        );

        TestEntity result = EntityUtils.getById(entities, TestEntity.class, 1);

        // Should return the first one found
        assertEquals(1, result.getId());
        assertEquals("Entity1", result.getName());
    }

    @Test
    void getByIdHandlesNullCollection() {
        assertThrows(
            NullPointerException.class,
            () -> EntityUtils.getById(null, TestEntity.class, 1)
        );
    }

    @Test
    void utilityClassCannotBeInstantiated() {
        // private ctor on abstract util — reflection without setAccessible → IllegalAccessException
        assertThrows(
            IllegalAccessException.class,
            () -> EntityUtils.class.getDeclaredConstructor().newInstance()
        );
    }

    @Test
    void getByIdWithEmptyCollection() {
        List<TestEntity> entities = Arrays.asList();

        ObjectRetrievalFailureException exception = assertThrows(
            ObjectRetrievalFailureException.class,
            () -> EntityUtils.getById(entities, TestEntity.class, 1)
        );

        assertEquals(TestEntity.class, exception.getEntityClass());
        assertEquals(1, exception.getEntityId());
    }

    @Test
    void getByIdWithZeroId() {
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(0, "ZeroEntity")
        );

        TestEntity result = EntityUtils.getById(entities, TestEntity.class, 0);

        assertEquals(0, result.getId());
        assertEquals("ZeroEntity", result.getName());
    }

    @Test
    void getByIdWithNegativeId() {
        List<TestEntity> entities = Arrays.asList(
            new TestEntity(-1, "NegativeEntity")
        );

        TestEntity result = EntityUtils.getById(entities, TestEntity.class, -1);

        assertEquals(-1, result.getId());
        assertEquals("NegativeEntity", result.getName());
    }
}