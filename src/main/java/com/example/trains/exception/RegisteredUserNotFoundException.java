package com.example.trains.exception;

import org.springframework.security.core.AuthenticationException;

public class RegisteredUserNotFoundException extends AuthenticationException {
    public RegisteredUserNotFoundException(String message) {
        super(message);
    }
}
