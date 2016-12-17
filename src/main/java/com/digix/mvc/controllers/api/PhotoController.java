package com.digix.mvc.controllers.api;

import com.digix.mvc.model.entities.api.jsons.ContentJson;
import com.digix.mvc.model.entities.api.responses.AlbumListResponse;
import com.digix.mvc.model.entities.api.responses.AlbumResponse;
import com.digix.mvc.model.entities.api.responses.ContentResponse;
import com.digix.mvc.model.entities.api.responses.ContentListResponse;
import com.digix.mvc.model.entities.api.exceptions.*;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.api.ApiContentService;
import com.digix.mvc.model.services.api.ApiPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Album;
import org.springframework.social.facebook.api.Photo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;

@RestController(value = "photoApiController")
@RequestMapping(value = "/api/photos")
public class PhotoController {

    private static final int TYPE_ID = 1;
    private final String WEB_PATH;

    @Autowired
    private ApiContentService apiContentService;
    @Autowired
    private ApiPhotoService apiPhotoService;


    @Autowired
    public PhotoController(ServletContext servletContext) {
        WEB_PATH = servletContext.getRealPath("");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getPhotos(@RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getContents(accesstoken, TYPE_ID);
    }

    @RequestMapping(value = "/{photoID}", method = RequestMethod.GET)
    public ResponseEntity<ContentResponse> getPhoto(@PathVariable String photoID,
                                                    @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getResponseContent(accesstoken, photoID, TYPE_ID);
    }

    @RequestMapping(value = "/{photoID}", method = RequestMethod.POST)
    public ResponseEntity<ContentResponse> postPhoto(@PathVariable String photoID,
                                                     @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.postContent(accesstoken, photoID, String.valueOf(TYPE_ID));
    }

    @RequestMapping(value = "/{photoID}", method = RequestMethod.DELETE)
    public ResponseEntity<ContentResponse> deletePhoto(@PathVariable String photoID,
                                                       @RequestParam(value = "accesstoken") String accesstoken) {
        try {
            Content deletedContent = apiContentService.deleteContent(accesstoken, photoID, TYPE_ID);
            return new ResponseEntity<>(new ContentResponse("Photo was successfully deleted", new ContentJson(deletedContent)), HttpStatus.OK);
        } catch (InvalidContentTypeException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (ContentNotFoundException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/facebook/albums", method = RequestMethod.GET)
    public ResponseEntity<AlbumListResponse> getFacebookAlbums(@RequestParam(value = "accesstoken") String accesstoken) {
        try {
            List<Album> albumList = apiPhotoService.getFacebookAlbums(accesstoken);
            return new ResponseEntity<>(new AlbumListResponse("Success", albumList), HttpStatus.OK);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new AlbumListResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (FacebookSyncException e) {
            return new ResponseEntity<>(new AlbumListResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/facebook/albums/{albumID}", method = RequestMethod.GET)
    public ResponseEntity<AlbumResponse> getFacebookPhotos(@RequestParam(value = "accesstoken") String accesstoken,
                                                           @PathVariable String albumID) {
        try {
            List<Photo> photoList = apiPhotoService.getFacebookPhotos(accesstoken, albumID);
            return new ResponseEntity<>(new AlbumResponse("Success", photoList), HttpStatus.OK);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new AlbumResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (FacebookSyncException e) {
            return new ResponseEntity<>(new AlbumResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/facebook", method = RequestMethod.POST)
    public ResponseEntity<ContentResponse> storeFacebookPhoto(@RequestParam(value = "accesstoken") String accesstoken,
                                                              @RequestParam(value = "albumID") String albumID,
                                                              @RequestParam(value = "photoID") String photoID) {
        try {
            Content photoContent = apiPhotoService.storeFacebookPhoto(accesstoken, albumID, photoID, WEB_PATH);
            return new ResponseEntity<>(new ContentResponse("Photo was successfully uploaded", new ContentJson(photoContent)), HttpStatus.OK);
        } catch (UploadFailedException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.CONFLICT);
        } catch (FileAlreadyUploadedException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.CONFLICT);
        } catch (InvalidContentIdException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (FacebookSyncException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<ContentResponse> uploadPhoto(@RequestParam(value = "photoURL") String photoURL,
                                                       @RequestParam(value = "accesstoken") String accesstoken) {
        try {
            Content photoContent = apiPhotoService.uploadPhoto(accesstoken, photoURL, WEB_PATH);
            return new ResponseEntity<>(new ContentResponse("File was successfully uploaded", new ContentJson(photoContent)), HttpStatus.OK);
        } catch (FileAlreadyUploadedException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.CONFLICT);
        } catch (UploadFailedException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.CONFLICT);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }

}