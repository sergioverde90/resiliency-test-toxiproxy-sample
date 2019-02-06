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

    private static final EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);;
    private static final HttpClient client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());

    @BeforeClass
    public static void init() throws IOException {
        var toxiproxyClient = new ToxiproxyClient("localhost", 8474);
        Proxy p = toxiproxyClient.createProxy("ms2Proxy", "toxyproxy:9090", "ms2:9090");
        Proxy p2 = toxiproxyClient.createProxy("postgresProxy", "toxyproxy:5432", "db:5432");
        p.toxics().latency("testLatency", ToxicDirection.DOWNSTREAM, 1000);
    }

    @Test
    public void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/hello");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        System.out.println("body = " + body);
    }

    @AfterClass
    public static void teardown() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

}
