package com.digix.mvc.model.entities.api.exceptions;


public class InvalidContentIdException extends Exception {

    @Override
    public String getMessage() {
        return "Invalid ID";
    }

}
