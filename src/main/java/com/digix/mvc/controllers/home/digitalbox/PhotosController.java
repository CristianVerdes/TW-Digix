package com.digix.mvc.controllers.home.digitalbox;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/digitalbox")
public class PhotosController {

    @Autowired
    private ContentService contentService;

    private final static int PAGE_SIZE = 5;
    private final static int TYPE_ID = 1;

    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public ModelAndView displayPhotos(HttpServletRequest request,
                                      @RequestParam(value = "page") String pageNumber) {

        ModelAndView model = new ModelAndView("digitalbox/photos/photos");

        /* get userID from session scope */
        String userID = (String) request.getSession().getAttribute("userID");

        /* get photos from specific page number */
        List<Content> photos = contentService.getPage(userID, TYPE_ID, Integer.parseInt(pageNumber), PAGE_SIZE);
        model.addObject("photos", photos);

        /* get total number of pages */
        int numberOfPages = contentService.countPages(userID, TYPE_ID, PAGE_SIZE);
        model.addObject("counter", numberOfPages);

        /* store in session scope currentPage */
        model.addObject("currentPage", pageNumber);
        request.getSession().setAttribute("currentPage", pageNumber);

        return model;
    }

    @RequestMapping(value = "/photoDetails", method = RequestMethod.GET)
    public ModelAndView displayPhotoDetails(@RequestParam(value = "contentID") String contentID) {
        ModelAndView model = new ModelAndView("digitalbox/photos/viewphoto");

        /* get contents details such as locations and tags */
        Content photo = contentService.getContent(contentID);
        model.addObject("photo", photo);

        return model;
    }

    @RequestMapping(value = "/postphoto", method = RequestMethod.POST)
    @ResponseBody
    public String postPhoto(@RequestParam(value = "contentID") String contentID,
                            HttpServletRequest request) {

        contentService.postContent(contentID);
        return null;
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.GET)
    public ModelAndView displayEditPhoto(@RequestParam(value = "contentID") String contentID) {

        ModelAndView model = new ModelAndView("digitalbox/photos/editphoto");

        Content content = contentService.getContent(contentID);

        model.addObject("content", content);

        return model;
    }

    @RequestMapping(value = "/editPhoto", method = RequestMethod.POST)
    public ModelAndView saveEditPhoto(@ModelAttribute Content content) {

        ModelAndView model = new ModelAndView("digitalbox/photos/editphoto");

        contentService.updateContent(content);

        model.addObject("content", content);

        return model;
    }

    @RequestMapping(value = "/deletephoto", method = RequestMethod.GET)
    @ResponseBody
    public RedirectView deletePhoto(@RequestParam(value = "contentID") String contentID,
                                    HttpServletRequest request) {
        String currentPage = (String) request.getSession().getAttribute("currentPage");

        contentService.deleteContent(contentID, TYPE_ID);

        return new RedirectView("/digitalbox/photos?page=" + currentPage);
    }
}
