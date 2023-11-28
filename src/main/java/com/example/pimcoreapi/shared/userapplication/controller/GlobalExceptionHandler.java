package com.example.pimcoreapi.shared.userapplication.controller;

import com.example.pimcoreapi.shared.exception.base.BusinessException;
import com.example.pimcoreapi.shared.exception.base.InfrastructureException;
import com.example.pimcoreapi.shared.exception.data.ErrorResponse;
import com.example.pimcoreapi.shared.exception.domain.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getDescription(false));
        apiError.addValidationError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<Object> handleInfrastructureException(InfrastructureException ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getDescription(false));
        apiError.addValidationError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "We are sorry, an internal error has occurred.", request.getDescription(false));
        apiError.addValidationError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundExceptionException(NotFoundException ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND, "Validation failed", request.getDescription(false));
        apiError.addValidationError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
