package com.sergio.sample.com.sergio.sample.repository;

import com.sergio.sample.com.sergio.sample.domain.User;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Singleton
public class UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Transactional
    public User findById(UUID userId) {
        return entityManager.find(User.class, userId);
    }
}
