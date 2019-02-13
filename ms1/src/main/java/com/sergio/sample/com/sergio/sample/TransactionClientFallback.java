package com.sergio.sample.com.sergio.sample;

import io.micronaut.retry.annotation.Fallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Fallback
public class TransactionClientFallback implements TransactionClient {

    private static final Logger LOG = LoggerFactory.getLogger(SomeService.class);

    @Override
    public List<Transaction> getNewTransactions(UUID userId) {
        LOG.warn("executing fallback method when getting new transactions");
        return Collections.emptyList();
    }

    @Override
    public UUID bindTransactionToUser(UUID userId, Transaction newTx) {
        LOG.warn("executing fallback method when create transaction");
        return null;
    }

    @Override
    public void removeTransaction(UUID userId, UUID txId) {
        throw new IllegalStateException("compensation transaction has not been performed. Error occurred.");
    }

}
