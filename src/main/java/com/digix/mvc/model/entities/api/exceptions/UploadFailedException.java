package com.digix.mvc.model.entities.api.exceptions;


public class UploadFailedException extends Exception {

    @Override
    public String getMessage() {
        return "Upload failed";
    }

}
