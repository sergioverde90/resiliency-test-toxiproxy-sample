package com.sergio.sample.com.sergio.sample;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
public class Transaction {

    @Column(name = "tx_date")
    private LocalDateTime date;

    @Column(name = "tx_concept")
    private String concept;

    private Transaction() {}

    public Transaction(String concept, LocalDateTime date) {
        this.date = date;
        this.concept = concept;
    }

    @Override
    public String toString() {
        return "{"
                + "\"date\":" + date
                + ", \"concept\":\"" + concept + "\""
                + "}";
    }
}
