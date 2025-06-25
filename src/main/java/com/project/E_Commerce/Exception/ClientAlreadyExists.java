package com.project.E_Commerce.Exception;


public class ClientAlreadyExists extends RuntimeException {
    public ClientAlreadyExists(String message) {
        super(message);
    }
}
