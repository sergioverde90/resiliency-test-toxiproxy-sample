package com.sergio.sample.com.sergio.sample;

import java.time.LocalDateTime;

public class Transaction {

    private LocalDateTime date;
    private String concept;

    private Transaction() {}

    public Transaction(String concept, LocalDateTime date) {
        this.date = date;
        this.concept = concept;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    @Override
    public String toString() {
        return "{"
                + "\"date\":" + date
                + ", \"concept\":\"" + concept + "\""
                + "}";
    }
}
