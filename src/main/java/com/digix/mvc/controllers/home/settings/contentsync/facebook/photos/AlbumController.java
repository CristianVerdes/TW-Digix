package com.digix.mvc.controllers.home.settings.contentsync.facebook.photos;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/settings/contentsync/facebook/photos")
public class AlbumController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private FacebookService facebookService;

    private String WEB_PATH;

    @Autowired
    public AlbumController(ServletContext servletContext) {
        WEB_PATH = servletContext.getRealPath("");
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public ModelAndView displayMenu() {
        return new ModelAndView("settings/contentsync/facebook/photos/menu");
    }

    @RequestMapping(value = "/albums", method = RequestMethod.GET)
    public ModelAndView displayAlbums() {
        ModelAndView model = new ModelAndView("settings/contentsync/facebook/photos/albums");

        PagedList<Album> albums = facebookService.getAlbums();
        model.addObject("albums", albums);

        return model;
    }

    @RequestMapping(value = "/albums/album", method = RequestMethod.GET)
    public ModelAndView displayAlbum(@RequestParam(value = "albumID") String albumID) {
        ModelAndView model = new ModelAndView("settings/contentsync/facebook/photos/album");

        Album album = facebookService.getAlbum(albumID);
        model.addObject("album", album);

        PagedList<Photo> photos = facebookService.getPhotos(albumID);
        model.addObject("photos", photos);

        return model;
    }

    @RequestMapping(value = "/albums/store", method = RequestMethod.POST)
    public RedirectView storePhoto(HttpServletRequest request,
                                   @RequestParam(value = "albumID") String albumID,
                                   @RequestParam(value = "photoID") String photoID) {

        String userID = (String) request.getSession().getAttribute("userID");

        Content albumPhoto = facebookService.getPhoto(albumID, photoID);

        contentService.savePhotoFile(WEB_PATH, userID, albumPhoto, "facebook");

        return new RedirectView("/settings/contentsync/facebook/photos/albums/album?albumID=" + albumID);
    }
}
