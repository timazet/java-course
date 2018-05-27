package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * There is a problem with final methods.
 * Due to the fact that we don't have here any aspectj weaving and proxy-target-class=true attribute is set
 * for <tx:annotation/> element, we implicitly use CGLIB to create proxies with transactional logic.
 * But as we know there is no place for final methods, because they can't be overridden during extending.
 * And we face with a problem when CGLIB forced to create an instance of DogService using constructor but can't wrap methods
 * marked with transactional annotation due to final methods.
 */
public class DogService {
    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Transactional
    public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Transactional
    public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Transactional
    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional
    public boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }

    private final DogDao dogDao;
}
