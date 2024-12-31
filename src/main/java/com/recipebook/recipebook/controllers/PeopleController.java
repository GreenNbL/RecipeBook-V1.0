package com.recipebook.recipebook.controllers;

import com.recipebook.recipebook.dao.PersonDAO;
import com.recipebook.recipebook.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PeopleController {

    private final PeopleService personService;

    @Autowired
    public PeopleController( PeopleService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public String show() {
        System.out.println("Showing people");
        //model.addAttribute("people", personDAO.findAll());
        return "show";
    }
}
