package com.digix.mvc.model.entities.api.jsons;

import org.springframework.social.facebook.api.Photo;

/**
 * Created by Andrei on 6/1/2016.
 */
public class PhotoJson {

    private String id;
    private String source;

    public PhotoJson(Photo photo) {
        this.id = photo.getId();
        this.source = photo.getSource();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
