package com.small.eCommerce.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import java.util.HashSet;
import java.util.Set;

public class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    public GlobalExceptionHandlerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(mock(Path.class));
        when(violation.getMessage()).thenReturn("Invalid value");
        when(violation.getPropertyPath().toString()).thenReturn("order.id");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        when(webRequest.getDescription(false)).thenReturn("Description");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assert errorResponse != null;
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("{id=Invalid value}", errorResponse.getMessage());
        assertEquals("Description", errorResponse.getPath());
    }

    @Test
    public void testHandleFoundException() {
        // Arrange
        FoundException ex = new FoundException("Product not found");
        when(webRequest.getDescription(false)).thenReturn("Description");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleProductNotFoundException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assert errorResponse != null;
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Product not found", errorResponse.getMessage());
        assertEquals("Description", errorResponse.getPath());
    }
}
