package com.recipebook.recipebook.repositories;

import com.recipebook.recipebook.models.Friendship;
import com.recipebook.recipebook.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("SELECT f.friend FROM Friendship f WHERE f.person.id = :personId")
    List<Person> findFriendsByPersonId(@Param("personId") int personId);

    Optional<Friendship> findByPersonIdAndFriendId(int personId, int friendshipId);
}
