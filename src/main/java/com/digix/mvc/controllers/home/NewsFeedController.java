package com.digix.mvc.controllers.home;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.NewsFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NewsFeedController {

    @Autowired
    private NewsFeedService newsFeedService;

    private final static int PAGE_SIZE = 5;

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    @ResponseBody
    public List<Content> getFeed(@RequestParam(value = "pageNumber") String pageNumber) {

        return newsFeedService.getNewsFeed(Integer.parseInt(pageNumber), PAGE_SIZE);
    }
}
