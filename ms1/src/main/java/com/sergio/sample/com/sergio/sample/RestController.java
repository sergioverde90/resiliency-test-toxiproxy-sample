package com.sergio.sample.com.sergio.sample;

import com.sergio.sample.com.sergio.sample.domain.User;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

@Controller("/hello")
public class RestController {

    @Inject
    SaluteClient saluteClient;

    @Inject
    DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Get
    @Transactional
    public String getSalute() throws SQLException {
        // simple operative:
        // 1º obtain resource from a third party system
        // 2º insert data into database
        // 3º publish to an event bus
        //dataSource.getConnection();
        User user = new User(UUID.randomUUID(), "Sergio");
        entityManager.persist(user);
        return saluteClient.salute();
    }

}
