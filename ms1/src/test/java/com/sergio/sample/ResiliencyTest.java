package com.sergio.sample;

import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ResiliencyTest {

    private static ToxiproxyClient toxiproxyClient;
    private static Proxy thirdPartyProxy;
    private static Proxy postgresProxy;

    static {
        initProxies();
    }

    private static void initProxies() {
        try {
            toxiproxyClient = new ToxiproxyClient("localhost", 8474);
            postgresProxy = toxiproxyClient.createProxy("postgres-proxy", "toxiproxy:5432", "db:5432");
            thirdPartyProxy = toxiproxyClient.createProxy("ms2Proxy", "toxiproxy:9090", "ms2:9090");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final EmbeddedServer server = ApplicationContext.build().run(EmbeddedServer.class);;
    private static final HttpClient client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());

    @BeforeClass
    public static void init() throws IOException {

        thirdPartyProxy.toxics().latency("testLatency", ToxicDirection.DOWNSTREAM, 100);
    }

    @Test
    public void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/hello");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        System.out.println("body = " + body);
    }

    @AfterClass
    public static void teardown() throws IOException {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }

        for (Proxy proxy : toxiproxyClient.getProxies()) {
            proxy.delete();
        }

    }

}
