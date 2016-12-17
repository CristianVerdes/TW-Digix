package com.digix.mvc.model.entities.api.exceptions;

public class ContentNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Content not found";
    }
}
