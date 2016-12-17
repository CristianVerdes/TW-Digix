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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/settings/contentsync/google/photos")
public class GooglePhotosController {

    private final static String TYPE_ID = "1";
    private final static String PROVIDER = "google";

    @Autowired
    private GoogleService googleService;
    @Autowired
    private ContentService contentService;

    private final String WEB_PATH;

    @Autowired
    public GooglePhotosController(ServletContext servletContext) {
        WEB_PATH = servletContext.getRealPath("");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayPhotos(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("settings/contentsync/google/photos");

        List<Content> photos = googleService.getPhotos();
        model.addObject("photos", photos);

        request.getSession().setAttribute("photosList",photos);

        return model;
    }

    @RequestMapping(value="/store", method = RequestMethod.POST)
    public RedirectView storePhoto(@RequestParam(value="providerID") String providerID,
                                   @RequestParam(value = "url") String url,
                                    HttpServletRequest request){
        String userID = (String) request.getSession().getAttribute("userID");

        Content photo = googleService.getPhoto(providerID, url);


        /* Prepare content to be added  */
        photo.setUserID(userID);
        photo.setTypeID(TYPE_ID);
        photo.setProvider(PROVIDER);

        contentService.savePhotoFile(WEB_PATH, userID, photo, PROVIDER);

        return new RedirectView("/settings/contentsync/google/photos/");

    }

}
