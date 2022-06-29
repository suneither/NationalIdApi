package com.example.daba.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "national_id")
public class NationalId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String personalCode;
    private Date birthDate;
    private String gender;

    @OneToMany(mappedBy = "nationalId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ValidationError> validationErrors;

    public NationalId(){
        validationErrors = new ArrayList<>();
    }

    public NationalId(int id, String personalCode, Date birthDate, String gender, List<ValidationError> validationErrors) {
        this.id = id;
        this.personalCode = personalCode;
        this.birthDate = birthDate;
        this.gender = gender;
        this.validationErrors = validationErrors;
    }

    public int getId() {
        return id;
    }

    public String getPersonal_code() {
        return personalCode;
    }

    public void setPersonal_code(String personalCode) {
        this.personalCode = personalCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
