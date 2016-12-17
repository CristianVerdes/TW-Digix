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

@RestController(value = "documentApiController")
@RequestMapping(value = "/api/documents")
public class DocumentController {

    private static final int TYPE_ID = 3;

    @Autowired
    private ApiContentService apiContentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ContentListResponse> getDocuments(@RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getContents(accesstoken, TYPE_ID);
    }

    @RequestMapping(value = "/{documentID}", method = RequestMethod.GET)
    public ResponseEntity<ContentResponse> getDocument(@PathVariable String documentID,
                                                       @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.getResponseContent(accesstoken, documentID, TYPE_ID);
    }

    @RequestMapping(value = "/{documentID}", method = RequestMethod.POST)
    public ResponseEntity<ContentResponse> postDocument(@PathVariable String documentID,
                                                        @RequestParam(value = "accesstoken") String accesstoken) {
        return apiContentService.postContent(accesstoken, documentID, String.valueOf(TYPE_ID));
    }

    @RequestMapping(value = "/{documentID}", method = RequestMethod.DELETE)
    public ResponseEntity<ContentResponse> deleteDocument(@PathVariable String documentID,
                                                          @RequestParam(value = "accesstoken") String accesstoken) {
        try {
            Content deletedContent = apiContentService.deleteContent(accesstoken, documentID, TYPE_ID);
            return new ResponseEntity<>(new ContentResponse("Document was successfully deleted", new ContentJson(deletedContent)), HttpStatus.OK);
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
