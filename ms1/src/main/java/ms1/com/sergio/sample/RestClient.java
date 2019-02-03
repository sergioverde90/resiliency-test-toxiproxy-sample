package ms1.com.sergio.sample;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://localhost:9090")
public interface RestClient {
    @Get("/salute")
    String salute();
}
