package com.digix.mvc.model.services.api;

import com.digix.mvc.model.entities.api.jsons.ContentJson;
import com.digix.mvc.model.entities.api.responses.ContentResponse;
import com.digix.mvc.model.entities.api.responses.ContentListResponse;
import com.digix.mvc.model.entities.api.exceptions.*;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.util.List;

public class ApiContentService {

    private DataSource dataSource;
    private ContentService contentService;
    private ApiService apiService;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
     * Returns a content of a specific user
     *
     * @param accessToken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @return a content (photo, video or document)
     * @throws ContentNotFoundException
     * @throws UnauthorizedTokenException
     * @throws InvalidTokenException
     */
    public Content getContent(String accessToken, String contentID) throws ContentNotFoundException, UnauthorizedTokenException, InvalidTokenException {
        if (apiService.isValidAccessToken(accessToken)) {
            Content content = contentService.getContent(contentID);
            if (content != null)
                if (apiService.isValidContentRequest(contentID, accessToken)) return content;
                else throw new UnauthorizedTokenException();
            else throw new ContentNotFoundException();
        } else throw new InvalidTokenException();
    }

    /**
     * Returns ResponseEntity(JSON) for a list of contents of a specific user and type of content
     *
     * @param accessToken represents the token used by an user for API access
     * @param typeID      reference for a specific Type Content
     * @return a content list(photos, videos or documents)
     */
    public ResponseEntity<ContentListResponse> getContents(String accessToken, int typeID) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

        if (apiService.isValidAccessToken(accessToken)) {
            String userID = apiService.getUserID(accessToken);
            List<Content> contents = contentService.getContents(userID, typeID);
            return new ResponseEntity<>(new ContentListResponse("Success", contents), responseHeaders,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ContentListResponse("Invalid access token", null), responseHeaders, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Returns a content of a specific user and type of content
     *
     * @param accessToken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @param typeID      reference for a specific Type Content
     * @return a content (photo, video or document)
     * @throws ContentNotFoundException
     * @throws UnauthorizedTokenException
     * @throws InvalidTokenException
     */

    private Content getContent(String accessToken, String contentID, int typeID)
            throws ContentNotFoundException, UnauthorizedTokenException, InvalidTokenException {

        if (apiService.isValidAccessToken(accessToken)) {
            String userID = apiService.getUserID(accessToken);
            Content content = contentService.getContent(userID, contentID, typeID);
            if (content != null) {
                if (apiService.isValidContentRequest(contentID, accessToken)) return content;
                else throw new UnauthorizedTokenException();
            } else throw new ContentNotFoundException();
        } else throw new InvalidTokenException();
    }

    /**
     * * Returns ResponseEntity(JSON) for a content of a specific user and type of content
     *
     * @param accessToken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @param typeID      reference for a specific Type Content
     * @return @return a content (photo, video or document)
     */
    public ResponseEntity<ContentResponse> getResponseContent(String accessToken, String contentID, int typeID) {
        try {
            Content content = this.getContent(accessToken, contentID, typeID);
            return new ResponseEntity<>(new ContentResponse("Success", new ContentJson(content)), HttpStatus.OK);
        } catch (ContentNotFoundException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a content for a specific user
     *
     * @param accesstoken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @param typeID     reference for a specific Type Content
     * @return the deleted content
     * @throws InvalidContentTypeException
     * @throws UnauthorizedTokenException
     * @throws InvalidContentIdException
     * @throws InvalidTokenException
     */
    public Content deleteContent(String accesstoken, String contentID, int typeID) throws InvalidContentTypeException, UnauthorizedTokenException, ContentNotFoundException, InvalidTokenException {
        if (apiService.isValidAccessToken(accesstoken)) {
            Content content = contentService.getContent(contentID);
            if (content != null) {
                if (apiService.isValidContentRequest(contentID, accesstoken)) {
                    boolean isDeleted = contentService.deleteContent(contentID, typeID);
                    if (isDeleted) return content;
                    else throw new InvalidContentTypeException();
                } else throw new UnauthorizedTokenException();
            } else throw new ContentNotFoundException();
        } else throw new InvalidTokenException();
    }

    /**
     * Post a specific content
     *
     * @param accessToken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @param typeID     reference for a specific Type Content
     * @return the content object that was posted
     * @throws UnauthorizedTokenException
     * @throws PhotoAlreadyPostedException
     * @throws InvalidContentTypeException
     * @throws ContentNotFoundException
     * @throws InvalidTokenException
     */
    private Content getPostedContent(String accessToken, String contentID, String typeID)
            throws UnauthorizedTokenException, PhotoAlreadyPostedException, InvalidContentTypeException, ContentNotFoundException, InvalidTokenException {
        if (apiService.isValidAccessToken(accessToken)) {
            Content content = contentService.getContent(contentID);
            if (content != null) {
                if (content.getTypeID().equals(typeID)) {
                    if (content.getPostedDate() == null) {
                        if (apiService.isValidContentRequest(contentID, accessToken)) {
                            contentService.postContent(contentID);
                            return contentService.getContent(contentID);
                        } else throw new UnauthorizedTokenException();
                    } else throw new PhotoAlreadyPostedException();
                } else throw new InvalidContentTypeException();
            } else throw new ContentNotFoundException();
        } else throw new InvalidTokenException();

    }

    /**
     *Post a specific content
     *
     * @param accessToken represents the token used by an user for API access
     * @param contentID   reference for a specific Content
     * @param typeID     reference for a specific Type Content
     * @return Response Object for posted content
     */
    public ResponseEntity<ContentResponse> postContent(String accessToken, String contentID, String typeID) {
        try {
            Content postedPhoto = this.getPostedContent(accessToken, contentID, typeID);
            String type = "";
            switch (typeID) {
                case "1" : type = "Photo"; break;
                case "2" : type = "Video"; break;
                case "3" : type = "Document"; break;
            }
            return new ResponseEntity<>(new ContentResponse(type + " was successfully posted", new ContentJson(postedPhoto)), HttpStatus.OK);
        } catch (UnauthorizedTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (PhotoAlreadyPostedException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.CONFLICT);
        } catch (InvalidContentTypeException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (ContentNotFoundException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

}
