package com.sergio.sample.com.sergio.sample.repository;

import com.sergio.sample.com.sergio.sample.domain.User;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }
}
