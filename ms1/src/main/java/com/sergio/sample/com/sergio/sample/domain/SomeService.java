package com.sergio.sample.com.sergio.sample.domain;

import com.sergio.sample.com.sergio.sample.ConceptRequest;
import com.sergio.sample.com.sergio.sample.TransactionClient;
import com.sergio.sample.com.sergio.sample.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class SomeService {

    private static final Logger LOG = LoggerFactory.getLogger(SomeService.class);

    @Inject
    TransactionClient transactionsClient;

    @Inject
    UserRepository userRepository;

    public List<Transaction> getTransactions(UUID someUserId) {
        LOG.info("getting new transactions...");
        return transactionsClient.getNewTransactions(someUserId); // 1º point of failure
    }

    public void createUserTransaction(UUID someUserId, String concept) {
        final Transaction newTx = transactionsClient.createTransaction(someUserId, new ConceptRequest(concept)); // 2º point of failure
        LOG.info("transaction created = {}, updating user transactions...", newTx);
        final var user = new User(someUserId, newTx);
        compensatingTransaction(
                () -> userRepository.save(user), // 3º point of failure with compensating transaction
                () -> transactionsClient.removeTransaction(someUserId, newTx.getId())
        );

    }

    private static void compensatingTransaction(Runnable action, Runnable compensating) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warn("Error occurred. Compensating transaction...");
            try { compensating.run(); } catch (Exception ignore) { }
        }
    }

}
