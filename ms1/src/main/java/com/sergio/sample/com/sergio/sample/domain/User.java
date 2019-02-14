package com.sergio.sample.com.sergio.sample.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
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

    public UUID getId() {
        return id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(transaction, user.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transaction);
    }

    @Override
    public String toString() {
        return "{\"User\":{"
                + "\"id\":" + id
                + ", \"transaction\":" + transaction
                + "}}";
    }
}
