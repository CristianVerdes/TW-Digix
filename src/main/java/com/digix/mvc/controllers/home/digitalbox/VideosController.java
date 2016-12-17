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

/**
 * Created by Cristian Verdes on 5/18/2016.
 */
@Controller
@RequestMapping("/digitalbox")
public class VideosController {

    @Autowired
    private ContentService contentService;

    private final static int PAGE_SIZE = 3;
    private final static int TYPE_ID = 2;

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public ModelAndView displayVideos(@RequestParam(value = "page") String page,
                                      HttpServletRequest request) {
        ModelAndView model = new ModelAndView("digitalbox/videos/videos");

        String userID = (String) request.getSession().getAttribute("userID");

        /* get videos from a specific page number */
        List<Content> videos = contentService.getPage(userID, TYPE_ID, Integer.parseInt(page), PAGE_SIZE);
        model.addObject("videos", videos);

        /* get total number of pages */
        int numberOfPages = contentService.countPages(userID, TYPE_ID, PAGE_SIZE);
        model.addObject("counter", numberOfPages);

        /* store in session scope currentPage */
        model.addObject("currentPage", page);
        request.getSession().setAttribute("currentPage", page);

        return model;
    }

    @RequestMapping(value = "/postvideo", method = RequestMethod.POST)
    public RedirectView postvideo(@RequestParam(value = "contentID") String contentID,
                                  HttpServletRequest request) {

        contentService.postContent(contentID);

        String currentPage = (String) request.getSession().getAttribute("currentPage");

        return new RedirectView("/digitalbox/videos?page=" + currentPage);
    }

    @RequestMapping(value = "/deletevideo", method = RequestMethod.GET)
    public RedirectView deleteVideo(@RequestParam(value = "contentID") String contentID,
                                    HttpServletRequest request) {
        String currentPage = (String) request.getSession().getAttribute("currentPage");

        contentService.deleteContent(contentID, TYPE_ID);

        return new RedirectView("/digitalbox/videos?page=" + currentPage);
    }

    @RequestMapping(value = "/editvideo", method = RequestMethod.GET)
    public ModelAndView displayEditVideo(@RequestParam(value = "contentID") String contentID) {

        ModelAndView model = new ModelAndView("digitalbox/videos/editvideo");

        Content content = contentService.getContent(contentID);

        model.addObject("content", content);

        return model;
    }

    @RequestMapping(value = "/editvideo", method = RequestMethod.POST)
    public ModelAndView saveEditVideo(@ModelAttribute Content content) {

        ModelAndView model = new ModelAndView("digitalbox/videos/editvideo");

        contentService.updateContent(content);

        model.addObject("content", content);

        return model;
    }

}
