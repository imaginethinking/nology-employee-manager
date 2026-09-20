package com.nology.employeemanager.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.nology.employeemanager.common.dtos.ApiErrorResponse;
import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.common.exceptions.ServiceValidationException;
import com.nology.employeemanager.common.exceptions.UnauthorizedException;
import com.nology.employeemanager.common.exceptions.UnprocessableContentException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                req.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableContentException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessableContent(UnprocessableContentException ex,
            HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage(),
                req.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceValidationexception(ServiceValidationException ex,
            HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage(),
                req.getRequestURI(),
                ex.getErrors());

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<ApiErrorResponse>
    // handleMethodArgumentTypeMismatchException(
    // MethodArgumentNotValidException ex, HttpServletRequest req) {
    // ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST,
    // ex.getMessage(), req.getRequestURI());

    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorisedException(
            UnauthorizedException ex, HttpServletRequest req) {
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
