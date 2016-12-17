package com.digix.mvc.model.entities.api.exceptions;

public class PhotoAlreadyPostedException extends Exception {

    @Override
    public String getMessage() {
        return "Photo is already posted";
    }
}
