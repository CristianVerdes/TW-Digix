package com.digix.mvc.controllers.account;

import com.digix.mvc.model.entities.dao.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LogInController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView displayLogin(@RequestParam(value = "error", required = false) String error) {

        ModelAndView model = new ModelAndView("login");

        if (error != null) model.addObject("error", "Invalid username and password!");
        model.addObject("user", new User());

        return model;
    }

    @RequestMapping("favicon.ico")
    public RedirectView favicon() {
        return new RedirectView("/");
    }
}
