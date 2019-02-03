package ms2.com.sergio.sample;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/salute")
public class SaluteController {

    @Get
    public String get() {
        return "Helloooo from micronaut controller";
    }
}
