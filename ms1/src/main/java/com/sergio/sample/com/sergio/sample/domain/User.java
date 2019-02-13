package com.sergio.sample.com.sergio.sample.domain;

import com.sergio.sample.com.sergio.sample.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@EqualsAndHashCode
public class User {

    @Id
    private UUID id;

    @Embedded
    private Transaction transaction;

    private User() {}

    public User(UUID id, Transaction newTx) {
        this.id = id;
        this.transaction = newTx;
    }

}
