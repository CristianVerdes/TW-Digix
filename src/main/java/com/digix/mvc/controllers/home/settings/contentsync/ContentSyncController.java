package com.digix.mvc.controllers.home.settings.contentsync;

import com.digix.mvc.model.services.home.social.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/settings")
public class ContentSyncController {

    @Autowired
    private ProviderService providerService;

    @RequestMapping(value = "/contentsync", method = RequestMethod.GET)
    public ModelAndView contentSync(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("settings/contentsync/contentsync");

        String email = request.getUserPrincipal().getName();

        int socialConnections = providerService.countConnection(email);
        model.addObject("socialConnections", socialConnections);

        return model;
    }
}
