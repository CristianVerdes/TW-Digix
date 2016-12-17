package com.digix.mvc.controllers.home.settings.contentsync.facebook.videos;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.FacebookService;
import com.digix.mvc.model.services.home.social.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/settings/contentsync/facebook/")
public class VideoController {

    @Autowired
    private FacebookService facebookService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public ModelAndView displayVideos(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("settings/contentsync/facebook/videos/videos");

        List<Content> videoPosts = (List<Content>) request.getSession().getAttribute("videoPosts");

        String email = request.getUserPrincipal().getName();
        String accessToken = providerService.getAccessToken(email, "facebook");

        if (videoPosts == null) {
            videoPosts = facebookService.getContentPosts(accessToken, Post.PostType.VIDEO);

            request.getSession().setAttribute("videoPosts", videoPosts);
        }
        model.addObject("videoPosts", videoPosts);

        return model;
    }

    @RequestMapping(value = "/videos/store", method = RequestMethod.POST)
    public RedirectView storeVideo(HttpServletRequest request, @RequestParam(value = "videoID") String videoID) {

        List<Content> videoPosts = (List<Content>) request.getSession().getAttribute("videoPosts");

        String userID = (String) request.getSession().getAttribute("userID");

        for (Iterator<Content> iterator = videoPosts.iterator(); iterator.hasNext(); ) {
            Content videoPost = iterator.next();
            if (videoPost.getProviderID().equals(videoID)) {

                 /* prepare Content Object */
                Content content = new Content(userID, "2", "facebook", videoPost.getPath(), videoPost.getDescription(), videoPost.getProviderID());
                content.setUploadedDate(videoPost.getUploadedDate());
                content.setTagList(videoPost.getTagList());
                content.setLocation(videoPost.getLocation());

                contentService.storeContent(content);
                iterator.remove();
            }

        }

        return new RedirectView("/settings/contentsync/facebook/videos");
    }
}
