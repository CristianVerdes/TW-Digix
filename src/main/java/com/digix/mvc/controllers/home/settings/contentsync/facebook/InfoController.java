package com.digix.mvc.controllers.home.settings.contentsync.facebook;

import com.digix.mvc.model.entities.dao.UserProfile;
import com.digix.mvc.model.services.home.ProfileService;
import com.digix.mvc.model.services.home.social.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/settings/contentsync/facebook/info")
public class InfoController {

    @Autowired
    private FacebookService facebookService;
    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public RedirectView syncInfo(HttpServletRequest request) {

        String userID = (String) request.getSession().getAttribute("userID");
        UserProfile userProfile = facebookService.getUserProfile();

        profileService.saveFacebookProfile(userID, userProfile);

        return new RedirectView("/settings/contentsync");
    }
}
