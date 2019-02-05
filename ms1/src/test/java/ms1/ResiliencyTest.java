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
        var toxiproxyClient = new ToxiproxyClient("localhost", 8474);
        // This configuration is confusing. When you run this code the toxiproxyClient will communicate
        // with its server CREATING A NEW PROXY INSIDE DOCKER. Having this in mind the first rare parameter, 'listen',
        // means where is your proxy listening (inside docker, on 0.0.0.0, in the dockerhost)
        // and which port its using. We can use the name of the service, in this case, 'toxyproxy'.
        //
        // You will need use the proxy port in order to communicate with the third party system. So, if your third party
        // system reached in 9090, you should use another port and use the 9090 for the proxy.
        //
        // The second rare parameter, upstream, is the other side where of the proxy. So, again, because its inside docker
        // you cannot use 'localhost'. You will need to use the name of the service, in this case, 'ms2'.
        //
        // If you are more curious, when you up the toxyProxyServer inside docker and you create a new proxy you will
        // see something like this in the log:
        //
        // time="2019-02-04T19:16:33Z" level="info" msg="API HTTP server starting" host="0.0.0.0" port="8474" version="2.1.4"
        // time="2019-02-04T19:18:12Z" level="info" msg="Started proxy" name="resiliencyTestProxy" proxy="[::]:20000" upstream="ms2:9090"
        Proxy p = toxiproxyClient.createProxy("resiliencyTestProxy", "toxyproxy:9090", "ms2:9090");
        p.toxics().latency("testLatency", ToxicDirection.DOWNSTREAM, 10000);
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @Test
    public void testHello() {
        HttpRequest request = HttpRequest.GET("/hello");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        System.out.println("body = " + body);
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

}
