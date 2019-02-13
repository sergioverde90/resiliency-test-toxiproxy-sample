package com.sergio.sample.com.sergio.sample;

import com.sergio.sample.com.sergio.sample.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
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

        final var newTx = new Transaction(concept, LocalDateTime.now());
        final var createdTransactionId = transactionsClient.bindTransactionToUser(someUserId, newTx); // 2º point of failure
        if (createdTransactionId == null) return;

        final var user = new User(someUserId, newTx);
        LOG.info("updating user transactions...");

        compensatingTransaction(
                () -> userRepository.update(user), // 3º point of failure with compensating transaction
                () -> transactionsClient.removeTransaction(someUserId, createdTransactionId)
        );

    }

    private static void compensatingTransaction(Runnable action, Runnable compensating) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warn("Error occurred. Compensating transactions...");
            try { compensating.run(); } catch (Exception ignore) { }
        }
    }

}
