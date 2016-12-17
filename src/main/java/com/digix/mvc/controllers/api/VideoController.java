package com.digix.mvc.controllers.api;

import com.digix.mvc.model.entities.api.exceptions.ContentNotFoundException;
import com.digix.mvc.model.entities.api.exceptions.InvalidContentTypeException;
import com.digix.mvc.model.entities.api.exceptions.InvalidTokenException;
import com.digix.mvc.model.entities.api.exceptions.UnauthorizedTokenException;
import com.digix.mvc.model.entities.api.jsons.ContentJson;
import com.digix.mvc.model.entities.api.responses.ContentResponse;
import com.digix.mvc.model.entities.api.responses.ContentListResponse;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.api.ApiContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "videoApiController")
@RequestMapping(value = "/api/videos")
public class VideoController {

    private static final int TYPE_ID = 2;

    @Autowired
    private ApiContentService apiContentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getVideos(@RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getContents(accesstoken, TYPE_ID);
    }

    @RequestMapping(value = "/{videoID}", method = RequestMethod.GET)
    public ResponseEntity<ContentResponse> getVideo(@PathVariable String videoID,
                                                    @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getResponseContent(accesstoken, videoID, TYPE_ID);
    }

    @RequestMapping(value = "/{videoID}", method = RequestMethod.POST)
    public ResponseEntity<ContentResponse> postVideo(@PathVariable String videoID,
                                                     @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.postContent(accesstoken, videoID, String.valueOf(TYPE_ID));
    }

    @RequestMapping(value = "/{videoID}", method = RequestMethod.DELETE)
    public ResponseEntity<ContentResponse> deleteVideo(@PathVariable String videoID,
                                                       @RequestParam(value = "accesstoken") String accesstoken) {
        try {
            Content deletedContent = apiContentService.deleteContent(accesstoken, videoID, TYPE_ID);
            return new ResponseEntity<>(new ContentResponse("Video was successfully deleted", new ContentJson(deletedContent)), HttpStatus.OK);
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

}