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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable int id,@RequestParam("file") MultipartFile file) {
        try {
             peopleService.updateAvatar(id, file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating avatar");
        }
    }
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable int id) {
        try {
            peopleService.deleteAvatar(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating avatar");
        }
    }
    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable int id) throws FileNotFoundException, MalformedURLException {
        // Найдите пользователя по ID
        Person optionalPerson = peopleService.findOne(id);
        if (optionalPerson == null) {
            throw new PersonNotFoundException();
        }

        // Получите адрес аватара
        String avatarAddress = optionalPerson.getAvatarAddress();
        Path filePath = (Path) Paths.get(avatarAddress);

        // Проверьте, существует ли файл
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Avatar not found");
        }

        // Create a UrlResource from the file path
        UrlResource resource = new UrlResource(filePath.toUri());

        /// Check if the resource exists
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
