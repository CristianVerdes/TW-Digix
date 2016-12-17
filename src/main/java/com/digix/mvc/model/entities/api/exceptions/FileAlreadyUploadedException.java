package com.digix.mvc.model.entities.api.exceptions;

public class FileAlreadyUploadedException extends Exception {

    @Override
    public String getMessage() {
        return "File is already uploaded";
    }

}
