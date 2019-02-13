package com.sergio.sample;

import com.sergio.sample.com.sergio.sample.SomeService;
import com.sergio.sample.com.sergio.sample.Transaction;
import com.sergio.sample.com.sergio.sample.TransactionClient;
import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import io.micronaut.context.ApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
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
    public void shouldCompensateTransactionWhenDatabaseNetworkFail() throws IOException {
        // GIVEN
        transactionsClient.bindTransactionToUser(userId, new Transaction("sc", LocalDateTime.now()));
        // WHEN
        postgresProxy.delete();
        someService.createUserTransaction(userId, "new tx concept");
        List<Transaction> transactions = transactionsClient.getNewTransactions(userId);
        assertEquals(1, transactions.size());
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
