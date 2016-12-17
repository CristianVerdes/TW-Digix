package com.digix.mvc.controllers.home;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.NewsFeedService;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private NewsFeedService newsFeedService;

    private final static int PAGE_SIZE = 5;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayHome(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("home");

        String userID = profileService.getUserID(request.getUserPrincipal().getName());
        request.getSession().setAttribute("userID", userID);

        List<Content> contentList = newsFeedService.getNewsFeed(1, PAGE_SIZE);
        model.addObject(contentList);

        return model;
    }

}
