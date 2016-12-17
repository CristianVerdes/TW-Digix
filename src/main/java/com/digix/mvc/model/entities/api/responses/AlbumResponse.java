package com.digix.mvc.model.entities.api.responses;

import com.digix.mvc.model.entities.api.jsons.PhotoJson;
import org.springframework.social.facebook.api.Photo;

import java.util.ArrayList;
import java.util.List;

public class AlbumResponse {

    private String message;
    private List<PhotoJson> photos = new ArrayList<>();

    public AlbumResponse(String message, List<Photo> facebookPhotos) {
        this.message = message;
        setPhotos(facebookPhotos);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PhotoJson> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> facebookPhotos) {
        if (facebookPhotos != null) {
            for (Photo photo : facebookPhotos) {
                this.photos.add(new PhotoJson(photo));
            }
        }
    }
}

