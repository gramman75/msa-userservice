package com.example.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Order is empty");
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}