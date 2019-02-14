package com.sergio.sample.com.sergio.sample.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
public class Transaction {

    public enum TransactionStatus {
        CREATED, NOT_CREATED
    }

    @Column(name = "tx_id")
    private UUID id;

    @Column(name = "tx_date")
    private LocalDateTime date;

    @Column(name = "tx_concept")
    private String concept;

    private TransactionStatus transactionStatus = TransactionStatus.CREATED;

    private Transaction() {}

    public static Transaction errorTransaction() {
        Transaction tx = new Transaction();
        tx.transactionStatus = TransactionStatus.NOT_CREATED;
        return tx;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    @Override
    public String toString() {
        return "{\"Transaction\":{"
                + "\"id\":" + id
                + ", \"date\":" + date
                + ", \"concept\":\"" + concept + "\""
                + ", \"transactionStatus\":\"" + transactionStatus + "\""
                + "}}";
    }
}
