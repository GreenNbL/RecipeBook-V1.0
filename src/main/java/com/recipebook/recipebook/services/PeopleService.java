package com.recipebook.recipebook.services;


import com.recipebook.recipebook.models.Person;
import com.recipebook.recipebook.repositories.PeopleRepository;
import com.recipebook.recipebook.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Value("${avatar.upload.path}")
    private String uploadAvatarDir;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll()
    {
        return peopleRepository.findAll();
    }

    public Person findOne(int id)
    {
        Optional<Person> foundPerson=peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    public Person searchByLoginAndPassword(String login, String password)
    {
        Optional<Person>foundPerson= peopleRepository.findByLoginAndPassword(login,password);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void updateAvatar(int id,MultipartFile file) throws IOException {
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if(!optionalPerson.isPresent())
            throw new PersonNotFoundException();
        String filename = file.getOriginalFilename();
        Path path = Paths.get(uploadAvatarDir + filename);
        Files.write(path, file.getBytes());
        Person person = optionalPerson.get();
        person.setAvatarAddress(String.valueOf(path));
        peopleRepository.save(person);
    }
    @Transactional
    public void deleteAvatar(int id) throws IOException {
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if(!optionalPerson.isPresent())
            throw new PersonNotFoundException();
        Person person = optionalPerson.get();
        String avatarAddress = person.getAvatarAddress();

        if (avatarAddress != null && !avatarAddress.isEmpty()) {
            Path path = Paths.get(avatarAddress);
            Files.deleteIfExists(path);
        }
        person.setAvatarAddress(null);
        peopleRepository.save(person);
    }

    @Transactional
    public void save(Person person)
    {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson)
    {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

}
