package com.sstohnij.stacktraceqabackendv0.exception;

import com.sstohnij.stacktraceqabackendv0.common.ResponseObject;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidConformationTokenException;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidRequestParameter;
import com.sstohnij.stacktraceqabackendv0.exception.custom.PermissionException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.transform.Result;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<ResponseObject<?>> handleJwtException(JwtException e) {
        log.error(e.getMessage(), e);
        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResponseObject<?>> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("You are not authorized to perform this action")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = PermissionException.class)
    public ResponseEntity<ResponseObject<?>> handleAccessDeniedException(PermissionException e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("You don't have permission to perform this action: " + e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ResponseObject<?>> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("Invalid username or password")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = NotValidConformationTokenException.class)
    public ResponseEntity<ResponseObject<?>> handleNotValidConfirmationToken(NotValidConformationTokenException e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    @ExceptionHandler(value = AuthorizationServiceException.class)
    public ResponseEntity<ResponseObject<?>> handleAuthorizationServiceException(AuthorizationServiceException e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("Authorization exception: " + e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseObject<?>> handleAuthenticationException(InternalAuthenticationServiceException e) {
        log.error(e.getMessage(), e);

        String message = "";
        if(e.getCause() instanceof DisabledException) {
            message = "Verify your email";
        }

        ResponseObject<?> response = ResponseObject
                .builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(message)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ResponseObject<?>> handleEntityNotFoundException(EntityNotFoundException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<ResponseObject<?>> handleEntityExistsException(EntityExistsException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<?>> handleConstraintViolationException(ConstraintViolationException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject<?>> handleInvalidMethodArgumentException(MethodArgumentNotValidException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = NotValidRequestParameter.class)
    public ResponseEntity<ResponseObject<?>> handleNotValidRequestParameterException(NotValidRequestParameter e) {
        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseObject<?>> handleGenericException(Exception e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response = ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("An error occurred while processing your request. Please try again")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
