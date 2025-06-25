package com.project.E_Commerce.Exception;

//specific exception to say that the acess is denied

public class NoUserFoundException extends RuntimeException{

    public NoUserFoundException(String message)
    {
        super(message);
    }
}
