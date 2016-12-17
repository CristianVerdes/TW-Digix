package com.digix.mvc.controllers.home.settings.contentsync.facebook.photos;

import com.digix.mvc.model.entities.dao.PostPage;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.FacebookService;
import com.digix.mvc.model.services.home.social.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/settings/contentsync/facebook/photos")
public class PostsController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private FacebookService facebookService;

    private final String WEB_PATH;

    @Autowired
    public PostsController(ServletContext servletContext) {
        WEB_PATH = servletContext.getRealPath("");
    }


    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ModelAndView displayPosts(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("settings/contentsync/facebook/photos/posts");

        /* get accessToken to retrieve full photo from post via graph.facebook */
        String email = request.getUserPrincipal().getName();
        String accessToken = providerService.getAccessToken(email, "facebook");

        /* retrieve allPhotos that was retrieved from facebook */
        List<Content> photos = (List<Content>) request.getSession().getAttribute("allPhotos");
        if (photos == null) {
            /* if any facebook photos wasn't retrieved then get first page of photos */
            PostPage currentPostPage = facebookService.getNextPage(accessToken, Post.PostType.PHOTO, null);
            List<Content> allPhotos = new ArrayList<>(currentPostPage.getContentList());

            /* save in session context */
            request.getSession().setAttribute("postPage", currentPostPage);
            request.getSession().setAttribute("allPhotos", allPhotos);

            /* add current photos to view */
            model.addObject("photos", currentPostPage.getContentList());

        } else {
            /* add photos from session context to view (will load faster) */
            model.addObject("photos", photos);
        }

        return model;
    }

    @RequestMapping(value = "/nextpage", method = RequestMethod.GET)
    @ResponseBody
    public List<Content> getNextPage(HttpServletRequest request) {

        /* get accessToken to retrieve full photo from post via graph.facebook */
        String email = request.getUserPrincipal().getName();
        String accessToken = providerService.getAccessToken(email, "facebook");

        PostPage postPage = (PostPage) request.getSession().getAttribute("postPage");

        if (postPage != null && postPage.getCurrentPage().getNextPage() == null) {
            /* last page was reached */
            return null;
        } else {

            /* get nextPage */
            PostPage currentPostPage = facebookService.getNextPage(accessToken, Post.PostType.PHOTO, postPage);

            /* get all photos that were stored in session */
            List<Content> allPhotos = (List<Content>) request.getSession().getAttribute("allPhotos");

            /* filter duplicate photos */
            List<Content> currentPagePhotos = currentPostPage.getContentList();
            for (Iterator<Content> iterator = currentPagePhotos.iterator(); iterator.hasNext(); ) {
                Content photoPost = iterator.next();
                /* remove photo from next page photos if is already showed in view
                    or add it to all photos from session scope otherwise
                 */
                if (allPhotos.contains(photoPost)) iterator.remove();
                else allPhotos.add(photoPost);
            }

            /* save nextPage and allPhotos in sessions scope */
            request.getSession().setAttribute("postPage", currentPostPage);
            request.getSession().setAttribute("allPhotos", allPhotos);

            return currentPagePhotos;
        }
    }

    @RequestMapping(value = "/posts/store", method = RequestMethod.POST)
    public RedirectView storePhoto(HttpServletRequest request,
                                   @RequestParam(value = "photoURL") String photoURL) {

        /* get all photos from session */
        List<Content> photos = (List<Content>) request.getSession().getAttribute("allPhotos");
        String userID = (String) request.getSession().getAttribute("userID");

        /* get photo than is going to be stored from photosList*/
        for (Iterator<Content> iterator = photos.iterator(); iterator.hasNext(); ) {
            Content photoPost = iterator.next();
            if (photoPost.getPath().equals(photoURL)) {

                /* save photo file */
                contentService.savePhotoFile(WEB_PATH, userID, photoPost, "facebook");
                /* remove file from preview after storing */
                iterator.remove();
            }
        }

        request.getSession().setAttribute("postPage", null);

        return new RedirectView("/settings/contentsync/facebook/photos/posts");
    }
}
