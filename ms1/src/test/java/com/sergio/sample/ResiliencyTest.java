package com.sergio.sample;

import com.sergio.sample.com.sergio.sample.TransactionClient;
import com.sergio.sample.com.sergio.sample.domain.SomeService;
import com.sergio.sample.com.sergio.sample.domain.Transaction;
import com.sergio.sample.com.sergio.sample.domain.User;
import com.sergio.sample.com.sergio.sample.repository.UserRepository;
import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import io.micronaut.context.ApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ResiliencyTest {

    private UUID userId;

    private ToxiproxyClient toxiproxyClient;
    private Proxy thirdPartyProxy;
    private Proxy postgresProxy;

    private ApplicationContext context;
    private SomeService someService;
    private TransactionClient transactionsClient;
    private UserRepository userRepository;

    @Before
    public void init() {

        userId = UUID.randomUUID();

        // initialize toxiproxy
        toxiproxyClient = new ToxiproxyClient("localhost", 8474);
        thirdPartyProxy = initProxy(toxiproxyClient, "ms2-proxy", "toxiproxy:9090", "ms2:9090");
        postgresProxy = initProxy(toxiproxyClient, "postgres-proxy", "toxiproxy:5432", "db:5432");

        // initialize embedded server
        context = ApplicationContext.build().start();
        someService = context.getBean(SomeService.class);
        transactionsClient = context.getBean(TransactionClient.class);
        userRepository = context.getBean(UserRepository.class);
    }

    @Test
    public void shouldOpenCircuitOpenedOnThirdPartyWhenNetworkFailure() throws IOException {
        // GIVEN
        thirdPartyProxy.delete(); // simulate network failure
        // WHEN
        List<Transaction> transactions = someService.getTransactions(userId);
        // THEN
        assertEquals(Collections.emptyList(), transactions);
    }

    @Test
    public void shouldOpenCircuitOnThirdPartyWhenNetworkHighLatency() throws IOException {
        // GIVEN
        thirdPartyProxy.toxics().latency("high-latency", ToxicDirection.DOWNSTREAM, 10_000);
        // WHEN
        List<Transaction> transactions = someService.getTransactions(userId);
        // THEN
        assertEquals(Collections.emptyList(), transactions);
    }

    @Test
    public void shouldExecuteFallbackWhenCreateTransactionError() throws IOException {
        // GIVEN
        thirdPartyProxy.delete();
        // WHEN
        someService.createUserTransaction(userId, "new tx concept");
        // THEN
        User user = userRepository.findById(userId);
        assertEquals(Transaction.TransactionStatus.NOT_CREATED, user.getTransaction().getTransactionStatus());
    }

    @Test
    public void shouldCompensateTransactionWhenDatabaseNetworkFail() throws IOException {
        // GIVEN
        postgresProxy.delete();
        // WHEN
        someService.createUserTransaction(userId, "new tx concept");
        // THEN
        List<Transaction> transactions = transactionsClient.getNewTransactions(userId);
        assertEquals(0, transactions.size());
    }

    @After
    public void onTestDown() {
        try { thirdPartyProxy.delete(); } catch (IOException ignored) {}
        try { postgresProxy.delete(); } catch (IOException ignored) {}
        if (context != null) { context.stop(); }
    }

    private static Proxy initProxy(ToxiproxyClient toxiproxyClient, String proxyName, String listen, String upstream) {
        try {
            return toxiproxyClient.createProxy(proxyName, listen, upstream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
