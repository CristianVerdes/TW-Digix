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

/**
 * Created by Cristian Verdes on 6/3/2016.
 */
@Controller
@RequestMapping("/settings/contentsync/google/videos")
public class GoogleVideosController {
    private final static String TYPE_ID = "2";
    private final static String PROVIDER = "google";

    @Autowired
    private GoogleService googleService;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayVideos(HttpServletRequest request){
        ModelAndView model = new ModelAndView("settings/contentsync/google/videos");

        List<Content> videos = googleService.getVideos();
        model.addObject("videos",videos);

        request.getSession().setAttribute("videosList", videos);

        return model;
    }

    @RequestMapping(value="/store", method = RequestMethod.POST)
    public RedirectView storeVideo(@RequestParam(value="providerID") String providerID,
                                   @RequestParam(value="url") String url,
                                   HttpServletRequest request){
        String userID = (String) request.getSession().getAttribute("userID");

        Content video = googleService.getVideo(providerID,url);

        /*Prepare content to be added*/
        video.setUserID(userID);
        video.setTypeID(TYPE_ID);
        video.setProvider(PROVIDER);

        contentService.addContent(video);

        return new RedirectView("/settings/contentsync/google/videos/");

    }


}
