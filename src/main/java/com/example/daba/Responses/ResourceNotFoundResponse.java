package com.example.daba.Responses;

public class ResourceNotFoundResponse {

    private String error;

    public ResourceNotFoundResponse() {
    }

    public ResourceNotFoundResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
