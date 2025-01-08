package com.recipebook.recipebook.dto;


import jakarta.validation.constraints.NotEmpty;

public class PersonLoginDTO {
    @NotEmpty(message="Login should not be empty")
    private String login;

    @NotEmpty(message="Password should not be empty")
    private String password;
}
