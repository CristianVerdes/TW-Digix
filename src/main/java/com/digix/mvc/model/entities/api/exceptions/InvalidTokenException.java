package com.digix.mvc.model.entities.api.exceptions;


public class InvalidTokenException extends Exception {

    @Override
    public String getMessage() {
        return "Invalid access token";
    }

}
