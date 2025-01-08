package com.recipebook.recipebook.controllers;

import com.recipebook.recipebook.dao.PersonDAO;
import com.recipebook.recipebook.models.Person;
import com.recipebook.recipebook.services.PeopleService;
import com.recipebook.recipebook.util.PersonErrorResponse;
import com.recipebook.recipebook.util.PersonNotCreatedException;
import com.recipebook.recipebook.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/people")
public class PeopleController {


    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<Person> getPeople() {
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable int id) {
        return peopleService.findOne(id);
    }

    @GetMapping("/login")
    public Person login(@RequestParam String login, @RequestParam String password) {
        return peopleService.searchByLoginAndPassword(login, password);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> savePerson(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField()+" - "+ fieldError.getDefaultMessage()+"; ");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }
        peopleService.save(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response=new PersonErrorResponse(
                "Person wasn't found",new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response=new PersonErrorResponse(
                e.getMessage(), new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
