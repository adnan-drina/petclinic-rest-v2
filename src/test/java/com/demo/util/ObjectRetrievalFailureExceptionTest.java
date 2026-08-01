package com.demo.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjectRetrievalFailureExceptionTest {

    @Test
    void constructorWithClassAndId() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(String.class, 42);

        assertEquals("Could not find String with identifier 42", exception.getMessage());
    }

    @Test
    void constructorStoresClassAndId() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Integer.class, 123);

        assertEquals(Integer.class, exception.getEntityClass());
        assertEquals(123, exception.getEntityId());
    }

    @Test
    void constructorWithStringId() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Object.class, "test-id");

        assertEquals(Object.class, exception.getEntityClass());
        assertEquals("test-id", exception.getEntityId());
    }

    @Test
    void getEntityClassReturnsClass() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Long.class, 1L);

        assertEquals(Long.class, exception.getEntityClass());
    }

    @Test
    void getEntityIdReturnsId() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Integer.class, 999);

        assertEquals(999, exception.getEntityId());
    }

    @Test
    void exceptionIsRuntimeException() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Object.class, 1);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void exceptionIsSerializable() {
        ObjectRetrievalFailureException exception = 
            new ObjectRetrievalFailureException(Object.class, 1);

        assertTrue(exception instanceof java.io.Serializable);
    }
}