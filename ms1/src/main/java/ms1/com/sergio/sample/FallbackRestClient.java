package ms1.com.sergio.sample;

import io.micronaut.retry.annotation.Fallback;

@Fallback
public class FallbackRestClient implements RestClient {
    @Override
    public String salute() {
        return "from fallback :(";
    }
}
