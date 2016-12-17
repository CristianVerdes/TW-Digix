package com.digix.mvc.controllers.api;

import com.digix.mvc.model.entities.api.responses.ContentListResponse;
import com.digix.mvc.model.entities.api.responses.UserResponse;
import com.digix.mvc.model.entities.dao.UserProfile;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.api.ApiService;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "userApiController")
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "{userID}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUser(@RequestParam(value = "accesstoken") String accesstoken,
                                                @PathVariable String userID) {

        if (apiService.isValidAccessToken(accesstoken)) {
            UserProfile userProfile = profileService.getUserProfile(userID);
            if (userProfile != null)
                return new ResponseEntity<>(new UserResponse("Success", userProfile), HttpStatus.OK);
            else return new ResponseEntity<>(new UserResponse("User not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserResponse("Invalid access token", null), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{userID}/photos", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getPhotos(@RequestParam(value = "accesstoken") String accesstoken,
                                                         @PathVariable String userID) {

        if (apiService.isValidAccessToken(accesstoken)) {
            if (profileService.getUserProfile(userID) != null) {
                List<Content> contentList = contentService.getPostedContent(userID, "1");
                return new ResponseEntity<>(new ContentListResponse("Success", contentList), HttpStatus.OK);
            } else return new ResponseEntity<>(new ContentListResponse("User not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ContentListResponse("Invalid access token", null), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{userID}/videos", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getVideos(@RequestParam(value = "accesstoken") String accesstoken,
                                                            @PathVariable String userID) {

        if (apiService.isValidAccessToken(accesstoken)) {
            if (profileService.getUserProfile(userID) != null) {
                List<Content> contentList = contentService.getPostedContent(userID, "2");
                return new ResponseEntity<>(new ContentListResponse("Success", contentList), HttpStatus.OK);
            } else return new ResponseEntity<>(new ContentListResponse("User not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ContentListResponse("Invalid access token", null), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{userID}/documents", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getDocuments(@RequestParam(value = "accesstoken") String accesstoken,
                                                         @PathVariable String userID) {

        if (apiService.isValidAccessToken(accesstoken)) {
            if (profileService.getUserProfile(userID) != null) {
                List<Content> contentList = contentService.getPostedContent(userID, "3");
                return new ResponseEntity<>(new ContentListResponse("Success", contentList), HttpStatus.OK);
            } else return new ResponseEntity<>(new ContentListResponse("User not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ContentListResponse("Invalid access token", null), HttpStatus.BAD_REQUEST);
    }
}