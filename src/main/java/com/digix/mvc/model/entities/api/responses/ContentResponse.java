package com.digix.mvc.model.entities.api.responses;

import com.digix.mvc.model.entities.api.jsons.ContentJson;

public class ContentResponse {

    private String message;
    private ContentJson content;

    public ContentResponse(String message, ContentJson content) {
        this.message = message;
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentJson getContent() {
        return content;
    }

    public void setContent(ContentJson content) {
        this.content = content;
    }
}
