package com.digix.mvc.controllers.home.settings.contentsync.facebook;

import com.digix.mvc.model.entities.dao.contents.Friend;
import com.digix.mvc.model.services.home.ContentService;
import com.digix.mvc.model.services.home.social.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/settings/contentsync/facebook/family")
public class FamilyController {

    @Autowired
    private FacebookService facebookService;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public RedirectView syncFamilyMembers(HttpServletRequest request) {

        String userID = (String) request.getSession().getAttribute("userID");

        List<Friend> friendList = facebookService.getFamily();
        for (Friend friend : friendList) {
            friend.setUserID(userID);
            contentService.addFriend(friend);
        }

        return new RedirectView("/social/family");
    }
}
