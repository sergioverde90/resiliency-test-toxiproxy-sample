package ms2.com.sergio.sample;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private LocalDateTime date;
    private String concept;

    private Transaction() {}

    public Transaction(String concept) {
        this.id = UUID.randomUUID();
        this.date = LocalDateTime.now();
        this.concept = concept;
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
