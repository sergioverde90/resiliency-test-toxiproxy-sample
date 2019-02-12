package ms2.com.sergio.sample;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private LocalDateTime date;
    private String concept;

    private Transaction() {}

    public Transaction(String concept, LocalDateTime date) {
        this.date = date;
        this.concept = concept;
    }

    public Transaction(UUID txId, Transaction other) {
        this.id = txId;
        this.date = other.date;
        this.concept = other.concept;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    public UUID getId() {
        return id;
    }
}
