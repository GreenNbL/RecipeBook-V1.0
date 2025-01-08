package com.recipebook.recipebook.repositories;

import com.recipebook.recipebook.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByLoginAndPassword(String login, String password);
}
