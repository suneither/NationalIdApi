package com.example.daba.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "validation_error")
public class ValidationError {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String errorMessage;
    private String errorCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "national_id", nullable = false)
    @JsonBackReference
    private NationalId nationalId;

    public ValidationError() {
    }

    public ValidationError(int id, String errorMessage, String errorCode, NationalId nationalId) {
        this.id = id;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.nationalId = nationalId;
    }

    public int getId() {
        return id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public NationalId getNationalId() {
        return nationalId;
    }

    public void setNationalId(NationalId nationalId) {
        this.nationalId = nationalId;
    }
}
