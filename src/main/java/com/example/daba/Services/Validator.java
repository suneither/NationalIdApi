package com.example.daba.Services;

import com.example.daba.Models.NationalId;
import com.example.daba.Models.ValidationError;
import com.example.daba.Responses.ValidationResponse;
import org.example.LithuanianNationalIdValidator;
import org.example.LtuNatIdModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public ValidationResponse validateSingleId(String id){
        ValidationResponse validationResponse = new ValidationResponse();

        LithuanianNationalIdValidator validator = new LithuanianNationalIdValidator();

        NationalId nationalId;
        LtuNatIdModel idModel;
        try{
            long parsedId = Long.parseLong(id);
            validator.validateID(parsedId);
            idModel = validator.getLtuNatIdModel();

            if(idModel.isValid()){
                nationalId = createValid(idModel);
                validationResponse.addValidId(nationalId);
            }else{
                nationalId = createInvalid(idModel);
                validationResponse.addInvalidId(nationalId);
            }


        }catch (NumberFormatException ex){
            idModel = validator.getLtuNatIdModel();

            nationalId = createInvalid(idModel);
            nationalId.setPersonal_code(id);
            validationResponse.addInvalidId(nationalId);
        }

        return validationResponse;
    }

    public ValidationResponse validateMultipleIds(List<String> ids){

        ValidationResponse validationResponse = new ValidationResponse();

        LithuanianNationalIdValidator validator = new LithuanianNationalIdValidator();

        for (String id : ids){
            NationalId nationalId;
            LtuNatIdModel idModel;
            try{
                long parsedId = Long.parseLong(id);
                validator.validateID(parsedId);
                idModel = validator.getLtuNatIdModel();

                if(idModel.isValid()){
                    nationalId = createValid(idModel);
                    validationResponse.addValidId(nationalId);
                }else{
                    nationalId = createInvalid(idModel);
                    validationResponse.addInvalidId(nationalId);
                }

            }catch (NumberFormatException ex){
                idModel = validator.getLtuNatIdModel();

                nationalId = createInvalid(idModel);
                nationalId.setPersonal_code(id);
                validationResponse.addInvalidId(nationalId);
            }
        }

        return validationResponse;

    }

    public ValidationResponse validateMultipleIds(MultipartFile file) throws IOException{

        ValidationResponse validationResponse = new ValidationResponse();

        LithuanianNationalIdValidator validator = new LithuanianNationalIdValidator();

        BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while( (line = bf.readLine()) != null){
            NationalId nationalId;
            LtuNatIdModel idModel;
            try{
                long parsedId = Long.parseLong(line);
                validator.validateID(parsedId);
                idModel = validator.getLtuNatIdModel();

                if(idModel.isValid()){
                    nationalId = createValid(idModel);
                    validationResponse.addValidId(nationalId);
                }else{
                    nationalId = createInvalid(idModel);
                    validationResponse.addInvalidId(nationalId);
                }

            }catch (NumberFormatException ex){
                idModel = validator.getLtuNatIdModel();

                nationalId = createInvalid(idModel);
                nationalId.setPersonal_code(line);
                validationResponse.addInvalidId(nationalId);
            }
        }

        return validationResponse;
    }

    private NationalId createInvalid(LtuNatIdModel idModel){

        NationalId nationalId = new NationalId();
        nationalId.setPersonal_code(String.valueOf(idModel.getId()));

        for (String err : idModel.getInvalidParts()){
            ValidationError validationError = new ValidationError();
            validationError.setErrorMessage(err);
            validationError.setErrorCode(HttpStatus.BAD_REQUEST.toString());
            validationError.setNationalId(nationalId);
            nationalId.getValidationErrors().add(validationError);
        }

        if(idModel.getInvalidParts().size() == 0){
            ValidationError validationError = new ValidationError();
            validationError.setErrorMessage("Provided id has non number characters!");
            validationError.setErrorCode(HttpStatus.BAD_REQUEST.toString());
            nationalId.getValidationErrors().add(validationError);
            validationError.setNationalId(nationalId);
        }

        return nationalId;
    }

    private NationalId createValid(LtuNatIdModel idModel){

        NationalId nationalId = new NationalId();

        nationalId.setPersonal_code(String.valueOf(idModel.getId()));
        nationalId.setBirthDate(idModel.getBirthDate());
        nationalId.setGender(String.valueOf(idModel.getGender()).toLowerCase());

        List<ValidationError> validationErrors = new ArrayList<>();
        for (String error: idModel.getInvalidParts()){
            ValidationError validationError = new ValidationError();
            validationError.setErrorMessage(error);
            validationError.setNationalId(nationalId);
            validationErrors.add(validationError);
        }
        nationalId.setValidationErrors(validationErrors);

        return nationalId;
    }
}
