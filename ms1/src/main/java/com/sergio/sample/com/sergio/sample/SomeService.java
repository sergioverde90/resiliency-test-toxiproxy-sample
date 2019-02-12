package com.sergio.sample.com.sergio.sample;

import com.sergio.sample.com.sergio.sample.domain.User;
import io.micronaut.spring.tx.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SomeService {

    private static final Logger LOG = LoggerFactory.getLogger(SomeService.class);

    @Inject
    TransactionClient transactionsClient;

    @Inject
    UserRepository userRepository;

    public List<Transaction> createTransaction(UUID someUserId) {
        LOG.info("getting new transactions...");
        final List<Transaction> transactions = transactionsClient.getNewTransactions(someUserId); // 1º point of failure
        LOG.info("transactions = {}", transactions);

        final var newTx = new Transaction("new tx concept", LocalDateTime.now());
        final var createdTransactionId = transactionsClient.createTransaction(someUserId, newTx); // 2º point of failure
        if (createdTransactionId == null) return transactions;

        final var updatedTransactions = addNewTransaction(transactions, newTx);
        final var user = new User(someUserId, "Some name", updatedTransactions);
        LOG.info("updating user transactions...");
        compensatingTransaction(
                () -> userRepository.update(user), // 3º point of failure with compensating transaction
                () -> transactionsClient.removeTransaction(someUserId, createdTransactionId)
        );
        return transactions;
    }

    private static void compensatingTransaction(Runnable action, Runnable compensating) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warn("Error occurred. Compensating transactions...");
            try { compensating.run(); } catch (Exception ignore) { }
        }
    }

    private static boolean hasNewTransactions(List<Transaction> transactions) {
        return !transactions.isEmpty();
    }

    private static List<Transaction> addNewTransaction(List<Transaction> transactions, Transaction newTx) {
        List<Transaction> copy = new ArrayList<>(transactions);
        copy.add(newTx);
        return copy;
    }

}
