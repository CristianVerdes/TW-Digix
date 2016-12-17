package com.digix.mvc.controllers.home.social;

import com.digix.mvc.model.entities.dao.contents.Friend;
import com.digix.mvc.model.services.SocialService;
import com.digix.mvc.model.services.home.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Cristian Verdes on 6/4/2016.
 */
@Controller
@RequestMapping(value = "/social/friends")
public class FriendsController {
    @Autowired
    private SocialService socialService;

    @Autowired
    private ProfileService profileService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayFriends(HttpServletRequest request){
        ModelAndView model = new ModelAndView("/social/friends");
        String userID = profileService.getUserID(request.getUserPrincipal().getName());

        List<Friend> friendsList = socialService.getFriends(userID);

        model.addObject("friendsList", friendsList);

        return model;
    }

}
