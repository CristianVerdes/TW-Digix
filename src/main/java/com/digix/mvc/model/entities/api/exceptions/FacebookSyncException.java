package com.digix.mvc.model.entities.api.exceptions;


public class FacebookSyncException extends Exception{

    @Override
    public String getMessage() {
        return "Account is not synchronized with facebook.";
    }
}
