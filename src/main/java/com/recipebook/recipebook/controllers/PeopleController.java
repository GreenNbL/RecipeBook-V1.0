package com.recipebook.recipebook.controllers;

import com.recipebook.recipebook.dao.PersonDAO;
import com.recipebook.recipebook.models.Person;
import com.recipebook.recipebook.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/people")
public class PeopleController {


    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person> getPeople() {
        return peopleService.findAll();
    }
    @GetMapping("/{id}")
    public Person getPerson(@PathVariable int id) {
        return peopleService.findOne(id);
    }

    @PostMapping()
    public void savePerson(@RequestBody Person person) {
        peopleService.save(person);
    }
}
