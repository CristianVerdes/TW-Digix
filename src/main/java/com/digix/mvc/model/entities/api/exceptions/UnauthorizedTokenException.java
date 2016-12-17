package com.digix.mvc.model.entities.api.exceptions;

public class UnauthorizedTokenException extends Exception{

    @Override
    public String getMessage() {
        return "Unauthorized Request";
    }
}
