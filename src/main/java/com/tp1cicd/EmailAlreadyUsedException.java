package com.tp1cicd;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(final String email) {
        super("L'email est deja utilise: " + email);
    }
}
