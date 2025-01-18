package com.recipebook.recipebook.dto;


import jakarta.validation.constraints.NotEmpty;


public class PersonLikeFriendDTO {

    private int id;

    private String firstName;

    private String secondName;

    public PersonLikeFriendDTO() {
    }

    public PersonLikeFriendDTO(int id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
