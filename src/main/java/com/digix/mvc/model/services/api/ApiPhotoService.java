package com.digix.mvc.model.services.api;

import com.digix.mvc.model.entities.api.exceptions.*;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.FacebookService;
import org.springframework.social.facebook.api.Album;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Photo;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import java.util.ArrayList;
import java.util.List;

public class ApiPhotoService {

    private ContentService contentService;
    private ApiService apiService;

    public ContentService getContentService() {
        return contentService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }


    /**
     * Upload a photo file from a specific URL
     *
     * @param accessToken represents the token used by an user for API access
     * @param photoURL    photo URL
     * @param WEB_PATH    path corresponding to the given virtual path.
     * @return Content object that was uploaded
     * @throws FileAlreadyUploadedException
     * @throws UploadFailedException
     * @throws InvalidTokenException
     */
    public Content uploadPhoto(String accessToken, String photoURL, String WEB_PATH)
            throws FileAlreadyUploadedException, UploadFailedException, InvalidTokenException {

        String userID = apiService.getUserID(accessToken);
        if (apiService.isValidAccessToken(accessToken)) {
            String providerID = contentService.storePhotoFile(WEB_PATH, userID, photoURL);
            if (providerID == null) throw new FileAlreadyUploadedException();
            else {
                Content content = contentService.getContentFromProviderID(providerID);
                if (content != null) return content;
                else throw new UploadFailedException();
            }
        } else throw new InvalidTokenException();
    }

    /**
     * Store a facebook photo with a specific photo ID from an album.
     *
     * @param accessToken represents the token used by an user for API access
     * @param albumID     an album ID
     * @param photoID     a photo ID
     * @param WEB_PATH    path corresponding to the given virtual path.
     * @return Content object that was stored
     * @throws UploadFailedException
     * @throws FileAlreadyUploadedException
     * @throws InvalidContentIdException
     * @throws FacebookSyncException
     * @throws InvalidTokenException
     */
    public Content storeFacebookPhoto(String accessToken, String albumID, String photoID, String WEB_PATH)
            throws UploadFailedException, FileAlreadyUploadedException, InvalidContentIdException, FacebookSyncException, InvalidTokenException {
        if (apiService.isValidAccessToken(accessToken)) {
            String userID = apiService.getUserID(accessToken);

            String facebookToken = apiService.getAccessToken(userID, "facebook");

            if (facebookToken != null) {
                Facebook facebook = new FacebookTemplate(facebookToken);
                FacebookService facebookService = new FacebookService(facebook, contentService);

                Content albumPhoto = facebookService.getPhoto(albumID, photoID);
                if (albumPhoto != null) {
                    if (contentService.getContentFromProviderID(photoID) == null) {
                        Content storedContent = contentService.savePhotoFile(WEB_PATH, userID, albumPhoto, "facebook");
                        if (storedContent != null) return storedContent;
                        else throw new UploadFailedException();
                    } else throw new FileAlreadyUploadedException();
                } else throw new InvalidContentIdException();
            } else throw new FacebookSyncException();
        } else throw new InvalidTokenException();
    }

    /**
     * Retrieves a list of albums belonging to the authenticated user. Requires "user_photos" permission.
     *
     * @param accessToken represents the token used by an user for API access
     * @return a list Albums for the user, or an empty list if not available.
     * @throws InvalidTokenException
     * @throws FacebookSyncException
     */
    public List<Album> getFacebookAlbums(String accessToken)
            throws InvalidTokenException, FacebookSyncException {

        List<Album> albumList = new ArrayList<>();

        if (apiService.isValidAccessToken(accessToken)) {
            String userID = apiService.getUserID(accessToken);
            String facebookToken = apiService.getAccessToken(userID, "facebook");

            if (facebookToken != null) {
                FacebookService facebookService = new FacebookService(new FacebookTemplate(facebookToken), contentService);

                PagedList<Album> albumPagedList = facebookService.getAlbums();
                for (Album album : albumPagedList) albumList.add(album);

            } else throw new FacebookSyncException();

        } else throw new InvalidTokenException();

        return albumList;
    }

    /**
     * Retrieves data for a specific album.
     *
     * @param accessToken represents the token used by an user for API access
     * @param albumID     the album ID
     * @return the requested Album object.
     * @throws FacebookSyncException
     * @throws InvalidTokenException
     */
    public List<Photo> getFacebookPhotos(String accessToken, String albumID)
            throws FacebookSyncException, InvalidTokenException {

        List<Photo> photoList = new ArrayList<>();

        if (apiService.isValidAccessToken(accessToken)) {
            String userID = apiService.getUserID(accessToken);
            String facebookToken = apiService.getAccessToken(userID, "facebook");

            if (facebookToken != null) {
                FacebookService facebookService = new FacebookService(new FacebookTemplate(facebookToken), contentService);
                PagedList<Photo> photoPagedList = facebookService.getPhotos(albumID);
                for (Photo photo : photoPagedList) photoList.add(photo);
            } else throw new FacebookSyncException();

        } else throw new InvalidTokenException();

        return photoList;

    }

}
