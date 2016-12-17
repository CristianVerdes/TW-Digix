package com.digix.mvc.controllers.home.digitalbox;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/digitalbox")
public class DocumentsController {

    @Autowired
    private ContentService contentService;

    private final static int PAGE_SIZE = 3;
    private final static int TYPE_ID = 3;

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public ModelAndView displayDocuments(@RequestParam(value = "page") String page,
                                         HttpServletRequest request) {
        ModelAndView model = new ModelAndView("digitalbox/documents/documents");

        String userID = (String) request.getSession().getAttribute("userID");

        /* get videos from a specific page number */
        List<Content> documents = contentService.getPage(userID, TYPE_ID, Integer.parseInt(page), PAGE_SIZE);
        model.addObject("documents", documents);

        /* get total number of pages */
        int numberOfPages = contentService.countPages(userID, TYPE_ID, PAGE_SIZE);
        model.addObject("counter", numberOfPages);

        /* store in session scope currentPage */
        model.addObject("currentPage", page);
        request.getSession().setAttribute("currentPage", page);

        return model;
    }

    @RequestMapping(value = "/postdocument", method = RequestMethod.POST)
    public RedirectView postDocument(@RequestParam(value = "contentID") String contentID,
                                     HttpServletRequest request) {

        contentService.postContent(contentID);

        String currentPage = (String) request.getSession().getAttribute("currentPage");

        return new RedirectView("/digitalbox/documents?page=" + currentPage);
    }

    @RequestMapping(value = "/deletedocument", method = RequestMethod.GET)
    public RedirectView deleteDocument(@RequestParam(value = "contentID") String contentID,
                                    HttpServletRequest request) {
        String currentPage = (String) request.getSession().getAttribute("currentPage");

        contentService.deleteContent(contentID, TYPE_ID);

        return new RedirectView("/digitalbox/documents?page=" + currentPage);
    }

    @RequestMapping(value = "/editdocument", method = RequestMethod.GET)
    public ModelAndView displayEditVideo(@RequestParam(value = "contentID") String contentID) {

        ModelAndView model = new ModelAndView("digitalbox/documents/editdocument");

        Content content = contentService.getContent(contentID);

        model.addObject("content", content);

        return model;
    }

    @RequestMapping(value = "/editdocument", method = RequestMethod.POST)
    public ModelAndView saveEditVideo(@ModelAttribute Content content) {

        ModelAndView model = new ModelAndView("digitalbox/documents/editdocument");

        contentService.updateContent(content);

        model.addObject("content", content);

        return model;
    }
}
