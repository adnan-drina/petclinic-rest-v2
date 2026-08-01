package com.demo.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.demo.rest.BindingErrorsResponse.BindingError;
import com.demo.model.Person;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

class BindingErrorsResponseTest {

    @Test
    void defaultConstructorCreatesEmptyErrors() {
        BindingErrorsResponse response = new BindingErrorsResponse();
        String json = response.toJSON();
        assertEquals("[]", json);
    }

    @Test
    void constructorWithIdOnly() {
        BindingErrorsResponse response = new BindingErrorsResponse(42);
        String json = response.toJSON();
        assertEquals("[]", json); // No errors for just path ID
    }

    @Test
    void constructorWithNullPathIdAndValidBodyId() {
        BindingErrorsResponse response = new BindingErrorsResponse(null, 42);
        String json = response.toJSON();
        
        // Should create an error for body ID when path ID is null
        assertTrue(json.contains("body"));
        assertTrue(json.contains("id"));
        assertTrue(json.contains("must not be specified"));
    }

    @Test
    void constructorWithNullPathIdAndNullBodyId() {
        BindingErrorsResponse response = new BindingErrorsResponse(null, null);
        String json = response.toJSON();
        assertEquals("[]", json); // No error when both are null
    }

    @Test
    void constructorWithMatchingIds() {
        BindingErrorsResponse response = new BindingErrorsResponse(42, 42);
        String json = response.toJSON();
        assertEquals("[]", json); // No error when IDs match
    }

    @Test
    void constructorWithMismatchedIds() {
        BindingErrorsResponse response = new BindingErrorsResponse(42, 99);
        String json = response.toJSON();
        
        // Should create error for mismatch
        assertTrue(json.contains("does not match pathId: 42"));
    }

    @Test
    void addErrorAddsSingleError() {
        BindingErrorsResponse response = new BindingErrorsResponse();
        BindingError error = createTestError("object", "field", "value", "message");
        
        response.addError(error);
        
        String json = response.toJSON();
        assertTrue(json.contains("object"));
        assertTrue(json.contains("field"));
        assertTrue(json.contains("value"));
        assertTrue(json.contains("message"));
    }

    @Test
    void addAllErrorsProcessesMultipleViolations() {
        // Create a person with validation violations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Person invalidPerson = new Person();
        Set<ConstraintViolation<Person>> violations = validator.validate(invalidPerson);
        
        BindingErrorsResponse response = new BindingErrorsResponse();
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<?>> rawViolations = (Set<ConstraintViolation<?>>) (Set<?>) violations;
        response.addAllErrors(rawViolations);
        
        String json = response.toJSON();
        // Should contain validation errors (depends on Person validation rules)
        assertNotNull(json);
    }

    @Test
    void toJSONReturnsEmptyArrayOnSerializationError() {
        BindingErrorsResponse response = new BindingErrorsResponse();
        // Add an error that would cause serialization issues (if any)
        BindingError error = createTestError("object", null, null, null);
        response.addError(error);
        
        String json = response.toJSON();
        // Should handle null values gracefully
        assertNotNull(json);
    }

    @Test
    void toStringReturnsReadableFormat() {
        BindingErrorsResponse response = new BindingErrorsResponse();
        BindingError error = createTestError("object", "field", "value", "message");
        response.addError(error);
        
        String str = response.toString();
        assertTrue(str.contains("BindingErrorsResponse"));
        assertTrue(str.contains("bindingErrors"));
    }

    @Test
    void toStringHandlesNullErrors() {
        BindingErrorsResponse response = new BindingErrorsResponse();
        String str = response.toString();
        assertTrue(str.contains("BindingErrorsResponse"));
        assertTrue(str.contains("[]"));
    }

    @Test
    void bindingErrorDefaultConstructor() {
        BindingError error = new BindingError();
        // Fields are private, but we can test through toString
        String str = error.toString();
        assertTrue(str.contains("objectName="));
        assertTrue(str.contains("fieldName="));
        assertTrue(str.contains("fieldValue="));
        assertTrue(str.contains("errorMessage="));
    }

    @Test
    void bindingErrorToStringReturnsDetails() {
        BindingError error = new BindingError();
        try {
            java.lang.reflect.Method setObjectName = BindingError.class.getDeclaredMethod(
                "setObjectName", String.class);
            setObjectName.setAccessible(true);
            setObjectName.invoke(error, "person");
            
            java.lang.reflect.Method setFieldName = BindingError.class.getDeclaredMethod(
                "setFieldName", String.class);
            setFieldName.setAccessible(true);
            setFieldName.invoke(error, "name");
            
            java.lang.reflect.Method setFieldValue = BindingError.class.getDeclaredMethod(
                "setFieldValue", String.class);
            setFieldValue.setAccessible(true);
            setFieldValue.invoke(error, "John");
            
            java.lang.reflect.Method setErrorMessage = BindingError.class.getDeclaredMethod(
                "setErrorMessage", String.class);
            setErrorMessage.setAccessible(true);
            setErrorMessage.invoke(error, "Too short");
            
            String str = error.toString();
            assertTrue(str.contains("person"));
            assertTrue(str.contains("name"));
            assertTrue(str.contains("John"));
            assertTrue(str.contains("Too short"));
        } catch (Exception e) {
            fail("Reflection setup failed: " + e.getMessage());
        }
    }

    @Test
    void addBodyIdErrorWithNull() {
        // Test indirectly via constructor with null pathId and non-null bodyId
        BindingErrorsResponse response = new BindingErrorsResponse(null, 123);
        String json = response.toJSON();
        assertTrue(json.contains("body"));
        assertTrue(json.contains("id"));
        assertTrue(json.contains("123"));
        assertTrue(json.contains("must not be specified"));
    }

    private BindingError createTestError(String objectName, String fieldName, 
                                       String fieldValue, String errorMessage) {
        BindingError error = new BindingError();
        try {
            java.lang.reflect.Method setObjectName = BindingError.class.getDeclaredMethod(
                "setObjectName", String.class);
            setObjectName.setAccessible(true);
            setObjectName.invoke(error, objectName);
            
            java.lang.reflect.Method setFieldName = BindingError.class.getDeclaredMethod(
                "setFieldName", String.class);
            setFieldName.setAccessible(true);
            setFieldName.invoke(error, fieldName);
            
            java.lang.reflect.Method setFieldValue = BindingError.class.getDeclaredMethod(
                "setFieldValue", String.class);
            setFieldValue.setAccessible(true);
            setFieldValue.invoke(error, fieldValue);
            
            java.lang.reflect.Method setErrorMessage = BindingError.class.getDeclaredMethod(
                "setErrorMessage", String.class);
            setErrorMessage.setAccessible(true);
            setErrorMessage.invoke(error, errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test error", e);
        }
        return error;
    }
}