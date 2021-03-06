package com.sergio.sample.com.sergio.sample;

import com.sergio.sample.com.sergio.sample.domain.Transaction;
import io.micronaut.configuration.hystrix.annotation.HystrixCommand;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.UUID;

@HystrixCommand
@Client("http://localhost:9090/transactions")
public interface TransactionClient {

    @Get("/{userId}")
    List<Transaction> getNewTransactions(@PathVariable UUID userId);

    @Post("/{userId}")
    Transaction createTransaction(@PathVariable UUID userId, @Body ConceptRequest conceptRequest);

    @Delete("/{userId}/{txId}")
    void removeTransaction(@PathVariable UUID userId, @PathVariable UUID txId);

}
