package com.digix.mvc.controllers.home.settings;

import com.digix.mvc.model.entities.dao.UserProfile;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/settings")
public class EditProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/editprofile", method = RequestMethod.GET)
    public ModelAndView displayEditProfile(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("settings/editprofile");

        String userID = (String) request.getSession().getAttribute("userID");

        UserProfile user = profileService.getUserProfile(userID);

        model.addObject("user", user);
        model.addObject("userProfile", new UserProfile());

        return model;
    }

    @RequestMapping(value = "/editprofile", method = RequestMethod.POST)
    public RedirectView executeEditProfile(@ModelAttribute UserProfile userProfile) {

        profileService.editUserProfile(userProfile);

        return new RedirectView("/settings/editprofile");
    }

}
