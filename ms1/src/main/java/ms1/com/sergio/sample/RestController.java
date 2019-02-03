package ms1.com.sergio.sample;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;

@Controller("/hello")
public class RestController {

    @Inject
    RestClient restClient;

    @Get
    public String getSalue() {
        return restClient.salute();
    }

}
