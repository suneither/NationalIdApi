package com.example.daba.Controllers;

import com.example.daba.Exceptions.ResourceNotFoundException;
import com.example.daba.Responses.ResourceNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NationalIdsControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceNotFoundResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResourceNotFoundResponse response = new ResourceNotFoundResponse(ex.getMessage());
        return response;
    }
}