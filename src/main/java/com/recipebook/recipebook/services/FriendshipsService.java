package com.recipebook.recipebook.services;

import com.recipebook.recipebook.models.Friendship;
import com.recipebook.recipebook.models.Person;
import com.recipebook.recipebook.repositories.FriendshipRepository;
import com.recipebook.recipebook.util.FriendshipNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FriendshipsService {
    private final FriendshipRepository friendshipRepository;
    private final PeopleService peopleService;

    @Autowired
    public FriendshipsService(FriendshipRepository friendshipRepository, PeopleService peopleService) {
        this.friendshipRepository = friendshipRepository;
        this.peopleService = peopleService;
    }

    public List<Person> findFriendsByPersonId(int id) {
        return friendshipRepository.findFriendsByPersonId(id);
    }

    @Transactional
    public void saveFriendship(int personId,int friendId) {
        Person person=peopleService.findOne(personId);
        Person friend=peopleService.findOne(friendId);
        Friendship friendship = new Friendship(person,friend);
        friendshipRepository.save(friendship);
    }
    @Transactional
    public void approvedFriendship(int personId, int friendshipId) {
        Optional<Friendship> friendship= friendshipRepository.findByPersonIdAndFriendId(personId,friendshipId);
        if(!friendship.isPresent()) {
            throw new FriendshipNotFoundException();
        }
        friendship.get().setApproved(true);
        friendshipRepository.save(friendship.get());
    }
    @Transactional
    public void deletedFriendship(int personId, int friendshipId) {
        Optional<Friendship> friendship= friendshipRepository.findByPersonIdAndFriendId(personId,friendshipId);
        if(!friendship.isPresent()) {
            throw new FriendshipNotFoundException();
        }
        friendshipRepository.delete(friendship.get());
    }
}
