package com.digix.mvc.model.entities.api.exceptions;

public class InvalidContentTypeException extends Exception {

    @Override
    public String getMessage() {
        return "Invalid typeID";
    }
}
