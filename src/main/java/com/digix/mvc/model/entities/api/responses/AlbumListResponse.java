package com.digix.mvc.model.entities.api.responses;


import com.digix.mvc.model.entities.api.jsons.AlbumJson;
import org.springframework.social.facebook.api.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListResponse {

    private String message;
    private List<AlbumJson> albums = new ArrayList<>();

    public AlbumListResponse(String message, List<Album> facebookAlbums) {
        this.message = message;
        this.setAlbums(facebookAlbums);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AlbumJson> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        if(albums != null) {
            for (Album album : albums) {
                this.albums.add(new AlbumJson(album));
            }
        }
    }
}
