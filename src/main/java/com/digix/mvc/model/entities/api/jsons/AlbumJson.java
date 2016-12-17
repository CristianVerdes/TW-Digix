package com.digix.mvc.model.entities.api.jsons;


import org.springframework.social.facebook.api.Album;

public class AlbumJson {

    private String id;
    private String name;

    public AlbumJson(Album album) {
        this.id = album.getId();
        this.name = album.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
