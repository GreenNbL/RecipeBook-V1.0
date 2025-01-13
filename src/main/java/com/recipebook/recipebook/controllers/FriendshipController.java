package com.recipebook.recipebook.controllers;

import com.recipebook.recipebook.models.Friendship;
import com.recipebook.recipebook.models.Person;
import com.recipebook.recipebook.repositories.FriendshipRepository;
import com.recipebook.recipebook.services.FriendshipsService;
import com.recipebook.recipebook.util.FriendshipErrorResponse;
import com.recipebook.recipebook.util.FriendshipNotFoundException;
import com.recipebook.recipebook.util.PersonErrorResponse;
import com.recipebook.recipebook.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipsService friendshipsService;

    @Autowired
    public FriendshipController(FriendshipsService friendshipsService) {
        this.friendshipsService = friendshipsService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createFriendship(@RequestParam("personId") int personId,@RequestParam("friendId") int friendId ) {
        friendshipsService.saveFriendship(personId,friendId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> approvedFriendship(@RequestParam("personId") int personId,@RequestParam("friendId") int friendId ) {
        friendshipsService.approvedFriendship(personId,friendId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteFriendship(@RequestParam("personId") int personId,@RequestParam("friendId") int friendId ) {
        friendshipsService.deletedFriendship(personId,friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<FriendshipErrorResponse> handleException(FriendshipNotFoundException e) {
        FriendshipErrorResponse response=new FriendshipErrorResponse(
                "Friendship wasn't found",new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response=new PersonErrorResponse(
                "Person wasn't found",new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
