package ms2.com.sergio.sample;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller("/transactions")
public class TransactionController {

    private static final Map<UUID, List<Transaction>> TX_BY_USER = new ConcurrentHashMap<>();

    @Get("/{userId}")
    public List<Transaction> all(@PathVariable UUID userId) {
        return TX_BY_USER.getOrDefault(userId, Collections.emptyList());
    }

    @Post("/{userId}")
    public HttpResponse<Transaction> addNewTx(@PathVariable UUID userId, @Body ConceptRequest request) {
        System.out.println("CREATING NEW TX");
        final var transaction = new Transaction(request.getConcept());
        TX_BY_USER.computeIfAbsent(userId, k -> new ArrayList<>());
        TX_BY_USER.get(userId).add(transaction);
        return HttpResponse.created(transaction);
    }

    @Delete("/{userId}/{txId}")
    public void deleteTx(@PathVariable UUID userId, @PathVariable UUID txId) {
        System.out.println("DELETING TX = " + txId);
        TX_BY_USER.get(userId).removeIf((tx) -> tx.getId().equals(txId));
    }

}
