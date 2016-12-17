package com.digix.mvc.controllers.home.social;

import com.digix.mvc.model.entities.dao.contents.Friend;
import com.digix.mvc.model.services.SocialService;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Verdes on 6/4/2016.
 */
@Controller(value = "socialFamilyController")
@RequestMapping(value = "/social/family")
public class FamilyController {

    @Autowired
    private SocialService socialService;

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ModelAndView displayFamily(HttpServletRequest request){
        ModelAndView model = new ModelAndView("/social/family");
        String userID = profileService.getUserID(request.getUserPrincipal().getName());

        List<Friend> familyList = socialService.getFamily(userID);

        model.addObject("familyList",familyList);

        return model;
    }
}
