package com.sergio.sample.com.sergio.sample.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
//@Getter
@EqualsAndHashCode
public class Transaction {

    @Column(name = "tx_id")
    private UUID id;

    @Column(name = "tx_date")
    private LocalDateTime date;

    @Column(name = "tx_concept")
    private String concept;

    private Transaction() {}

    @Override
    public String toString() {
        return "{"
                + "\"date\":" + date
                + ", \"concept\":\"" + concept + "\""
                + "}";
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
}
