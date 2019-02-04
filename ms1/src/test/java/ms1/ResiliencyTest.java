package ms1;

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

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() throws IOException {
        var toxyproxyclient = new ToxiproxyClient("localhost", 8474);
        Proxy p = toxyproxyclient.createProxy("resiliencyTestProxy", "0.0.0.0:20000", "ms1:9090");
        p.toxics().latency("testLatency", ToxicDirection.DOWNSTREAM, 1);
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testHello() {
        HttpRequest request = HttpRequest.GET("/hello");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        System.out.println("body = " + body);
    }

    @Test
    public void test() throws IOException {
        HttpRequest request = HttpRequest.GET("/hello");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        System.out.println("body = " + body);
    }

}
