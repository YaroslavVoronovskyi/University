package ua.com.foxminded.university.controller.rest.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.InternalServerErrorException;

import org.modelmapper.MappingException;
import org.springframework.core.Ordered; 
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class UniversityRestControllerExceptionHandler extends ResponseEntityExceptionHandler { 
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
          List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getField() + " " + x.getDefaultMessage())
                  .collect(Collectors.toList());
        ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).timestamp(LocalDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()).message(errors.toString()).build();
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handlerRequestException(EmptyResultDataAccessException exception){
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.name()).message(exception.getMessage()).build();
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(value = {MappingException.class})
    public ResponseEntity<Object> handlerRequestException(MappingException exception){
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.name()).message(exception.getMessage()).build();
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handlerRequestException(EntityNotFoundException exception){
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.name()).message(exception.getMessage()).build();
        return buildResponseEntity(apiError);
    }
  
    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
    public ResponseEntity<Object> handlerRequestException(InvalidDataAccessApiUsageException exception){
        ApiError apiError = ApiError.builder().status(HttpStatus.CONFLICT).timestamp(LocalDateTime.now())
                .error(HttpStatus.CONFLICT.name()).message(exception.getMessage()).build();
        return buildResponseEntity(apiError); 
    }
    
    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handlerRequestException(InternalServerErrorException exception){
        ApiError apiError = ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).timestamp(LocalDateTime.now())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name()).message(exception.getMessage()).build();
        return buildResponseEntity(apiError); 
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError){
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
