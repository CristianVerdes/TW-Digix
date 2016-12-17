package com.digix.mvc.controllers.api;

import com.digix.mvc.model.entities.api.responses.ContentResponse;
import com.digix.mvc.model.entities.api.jsons.ContentJson;
import com.digix.mvc.model.entities.api.exceptions.ContentNotFoundException;
import com.digix.mvc.model.entities.api.exceptions.InvalidTokenException;
import com.digix.mvc.model.entities.api.exceptions.UnauthorizedTokenException;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.api.ApiContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "contentApiController")
@RequestMapping(value = "/api/contents")
public class ContentController {

    @Autowired
    private ApiContentService apiContentService;

    @RequestMapping(value = "/{contentID}", method = RequestMethod.GET)
    public ResponseEntity<ContentResponse> getContent(@PathVariable String contentID,
                                                      @RequestParam(value = "accesstoken") String accesstoken) {

        try {
            Content content = apiContentService.getContent(accesstoken, contentID);
            return new ResponseEntity<>(new ContentResponse("Success", new ContentJson(content)), HttpStatus.FOUND);
        } catch (ContentNotFoundException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ContentResponse(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
