package com.digix.mvc.controllers.home;

import com.digix.mvc.model.entities.dao.UserProfile;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/userdetails", method = RequestMethod.GET)
    public ModelAndView displayProfile(@RequestParam(value = "userID") String userID,
                                       HttpServletRequest request) {
        ModelAndView model = new ModelAndView("profile");

        String email = request.getUserPrincipal().getName();
        UserProfile userProfile = profileService.getUserProfile(userID);

        userProfile.setFacebookURL(profileService.getProfileURL(email, "facebook"));
        userProfile.setGoogleURL(profileService.getProfileURL(email, "google"));
        userProfile.setLinkedinURL(profileService.getProfileURL(email, "linkedin"));
        userProfile.setTwitterURL(profileService.getProfileURL(email, "twitter"));

        model.addObject("userProfile", userProfile);

        return model;
    }
}
