package com.sergio.sample.com.sergio.sample;

import io.micronaut.retry.annotation.Fallback;

@Fallback
public class FallbackSaluteClient implements SaluteClient {
    @Override
    public String salute() {
        return "from fallback :(";
    }
}
