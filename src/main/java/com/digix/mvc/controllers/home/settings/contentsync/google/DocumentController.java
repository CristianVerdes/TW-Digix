package com.digix.mvc.controllers.home.settings.contentsync.google;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/settings/contentsync/google/documents")
public class DocumentController {

    private final static String TYPE_ID = "3";
    private final static String PROVIDER = "google";

    @Autowired
    private GoogleService googleService;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayDocuments() {
        ModelAndView model = new ModelAndView("settings/contentsync/google/documents");

        List<Content> documents = googleService.getFiles();
        model.addObject("documents", documents);

        return model;
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public RedirectView storeDocument(@RequestParam(value = "providerID") String providerID,
                                      HttpServletRequest request) {

        String userID = (String) request.getSession().getAttribute("userID");

        Content content = googleService.getFile(providerID);

        /* prepare content object to be added */
        content.setUserID(userID);
        content.setTypeID(TYPE_ID);
        content.setProvider(PROVIDER);

        contentService.addContent(content);

        return new RedirectView("/settings/contentsync/google/documents/");
    }

}
