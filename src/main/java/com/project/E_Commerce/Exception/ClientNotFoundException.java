package com.project.E_Commerce.Exception;

//if the user wants to log in and he is not present then at that time this exception is thrown
public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message)
    {
        super(message);
    }


}
