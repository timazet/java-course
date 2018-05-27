package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JdbcDogService implements DogService {
    public JdbcDogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Transactional @Override public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Transactional @Override public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Transactional @Override public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional @Override public boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }

    // doesn't have to be @Transactional since doSaveDogs() is transactional anyway
    // That's not true.
    // The problem is that we use runtime proxying and according to that we redirect each call to proxy to the proxied target.
    // But here we call proxied target method inside another proxied target method, therefore there is no
    // transactional rollback logic for runtime exceptions.
    @Transactional
    @Override public void createNewDogsAndIgnoreAlreadySaved(List<Dog> dogs) {
        List<Dog> toSave = new ArrayList<>();
        for (Dog dog : dogs) if(dog.getId() == null) toSave.add(dog);
        doSaveDogs(toSave);
    }
    @Transactional
    private void doSaveDogs(List<Dog> dogs) {
        for(Dog dog: dogs) createDog(dog);
    }

    private final DogDao dogDao;
}
