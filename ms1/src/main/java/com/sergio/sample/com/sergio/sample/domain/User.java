package com.sergio.sample.com.sergio.sample.domain;

import com.sergio.sample.com.sergio.sample.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "transactions")
    private String transactions;

    private User() {}

    public User(UUID id, String name, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.transactions = transactions.toString();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
