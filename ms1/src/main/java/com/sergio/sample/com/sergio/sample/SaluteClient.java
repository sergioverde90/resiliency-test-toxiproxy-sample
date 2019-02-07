package com.sergio.sample.com.sergio.sample;

import io.micronaut.configuration.hystrix.annotation.HystrixCommand;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@HystrixCommand
@Client("http://localhost:9090")
public interface SaluteClient {

    @Get("/salute")
    String salute();

}
