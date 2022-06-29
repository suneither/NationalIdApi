package com.example.daba.Responses;

import com.example.daba.Models.NationalId;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ValidationResponse {

    private HttpStatus httpStatus;
    private String message;

    private List<NationalId> validIds;
    private List<NationalId> invalidIds;

    public ValidationResponse() {
        validIds = new ArrayList<>();
        invalidIds = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage() {
        if(invalidIds.size() == 0){
            message = "all invalid";
        }else if(invalidIds.size() > 0 && validIds.size() > 0){
            message = "some invalid";
        }else if(invalidIds.size() > 0  && validIds.size() == 0){
            message = "all invalid";
        }
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void addInvalidId(NationalId invalidId){
        invalidIds.add(invalidId);
    }

    public void addValidId(NationalId validId){
        validIds.add(validId);
    }

    public List<NationalId> getValidIds() {
        return validIds;
    }

    public List<NationalId> getInvalidIds() {
        return invalidIds;
    }
}
