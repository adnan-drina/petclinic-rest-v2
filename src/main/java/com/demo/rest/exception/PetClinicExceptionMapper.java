package com.demo.rest.exception;

import com.demo.util.ObjectRetrievalFailureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Narrow JAX-RS ExceptionMapper for service-layer failures (V6 R6: never
 * ExceptionMapper&lt;Exception&gt;). Staging counterpart: ExceptionControllerAdvice.
 */
@Provider
public class PetClinicExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        if (throwable instanceof EntityNotFoundException
                || throwable instanceof ObjectRetrievalFailureException) {
            return json(Response.Status.NOT_FOUND, throwable);
        }
        if (throwable instanceof ValidationException) {
            return json(Response.Status.BAD_REQUEST, throwable);
        }
        if (throwable instanceof PersistenceException) {
            return json(Response.Status.SERVICE_UNAVAILABLE, throwable);
        }
        return json(Response.Status.INTERNAL_SERVER_ERROR, throwable);
    }

    private static Response json(Response.Status status, Throwable ex) {
        return Response.status(status)
                .entity(new ErrorInfo(ex))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /** Staging-faithful error body: className + exMessage. */
    public static class ErrorInfo {
        public final String className;
        public final String exMessage;

        public ErrorInfo(Throwable ex) {
            this.className = ex.getClass().getName();
            this.exMessage = ex.getLocalizedMessage();
        }
    }
}
