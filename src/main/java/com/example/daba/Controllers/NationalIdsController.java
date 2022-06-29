package com.example.daba.Controllers;

import com.example.daba.Exceptions.ResourceNotFoundException;
import com.example.daba.Models.NationalId;
import com.example.daba.Responses.ValidationResponse;
import com.example.daba.Services.Validator;
import com.example.daba.Repository.NationalIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/validate")
public class NationalIdsController {

    @Autowired
    NationalIdRepository nationalIdRepository;

    Validator validator;

    public NationalIdsController(){
        validator = new Validator();
        initScheduledDeletion();
    }

    @GetMapping("/")
    public ResponseEntity<List<NationalId>> getAll(){
        List<NationalId> nationalIds = nationalIdRepository.findAll();
        if(nationalIds.size() == 0){
            throw new ResourceNotFoundException("There are no data");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(nationalIds);
        }
    }

    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ValidationResponse> create(@PathVariable String id){
        ValidationResponse validationResponse = validator.validateSingleId(id);

        save(validationResponse);

        validationResponse.setHttpStatus(HttpStatus.OK);
        validationResponse.setMessage();

        return ResponseEntity.status(HttpStatus.OK).body(validationResponse);

    }

    @RequestMapping(value = "/ids", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ValidationResponse> create(@RequestBody List<String> ids){

        ValidationResponse validationResponse = validator.validateMultipleIds(ids);

        save(validationResponse);

        validationResponse.setHttpStatus(HttpStatus.OK);
        validationResponse.setMessage();

        return ResponseEntity.status(HttpStatus.OK).body(validationResponse);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<ValidationResponse> create(@RequestParam("file") MultipartFile file){

        ValidationResponse validationResponse = null;
        try {
            validationResponse = validator.validateMultipleIds(file);
        } catch (IOException e) {
            throw new ResourceNotFoundException("File does not exist");
        }

        save(validationResponse);

        validationResponse.setHttpStatus(HttpStatus.OK);
        validationResponse.setMessage();

        return ResponseEntity.status(HttpStatus.OK).body(validationResponse);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<NationalId> get(@PathVariable String id){

        NationalId nationalId = nationalIdRepository.findByPersonalCode(id);

        if(nationalId == null){
            throw new ResourceNotFoundException("Id " + id + " does not exist");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(nationalId);
        }

    }

    @RequestMapping(value = "/byDateRange", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<NationalId>> searchByBirthDateRange(@RequestBody Map<String, String> body){

        String from = body.get("from");
        String to = body.get("to");

        Date dateFrom = null;
        Date dateTo = null;

        if(from == null || to == null){
            throw new ResourceNotFoundException("Dates are not specified");
        }

        try {
            dateFrom = new SimpleDateFormat("yyyy-mm-dd").parse(from);
            dateTo = new SimpleDateFormat("yyyy-mm-dd").parse(to);
        } catch (ParseException e) {
            throw new ResourceNotFoundException("Date is invalid it has to be this format: yyyy-mm-dd");
        }

        List<NationalId> nationalIds = nationalIdRepository.findAllByBirthDateBetween(dateFrom, dateTo);

        if(nationalIds.size() == 0){
            throw new ResourceNotFoundException("There are no entries at { " + from + "  " + to + " } range");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(nationalIds);
        }
    }

    private void save(ValidationResponse validationResponse){
        if(validationResponse.getValidIds().size() != 0)
        validationResponse.getValidIds().forEach(nationalId -> {
            nationalIdRepository.save(nationalId);
        });

        if(validationResponse.getInvalidIds().size() != 0)
        validationResponse.getInvalidIds().forEach(nationalId -> {
            nationalIdRepository.save(nationalId);
        });

    }

    private void initScheduledDeletion(){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

        Runnable task1 = () -> {
            nationalIdRepository.deleteNationalId();
        };

        ses.scheduleAtFixedRate(task1, 5, 120, TimeUnit.SECONDS);
    }
}
