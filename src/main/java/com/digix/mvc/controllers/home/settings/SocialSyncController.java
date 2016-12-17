package com.digix.mvc.controllers.home.settings;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/settings")
public class SocialSyncController {

    @RequestMapping(value = "/socialsync", method = RequestMethod.GET)
    public ModelAndView displaySocialSync() {
        return new ModelAndView("settings/socialsync");
    }
}
