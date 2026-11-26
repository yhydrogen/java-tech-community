package com.example.ssmstudy.exception;


import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail globalExceptionHandler(MethodArgumentNotValidException ex){
        //return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getBindingResult().getFieldError().getDefaultMessage());
        BindingResult result = ex.getBindingResult();
        FieldError fieldError = result.getFieldError();

        String message = null;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }
        if (message == null || message.isBlank()) {
            message = "请求参数不合法";
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }
}
