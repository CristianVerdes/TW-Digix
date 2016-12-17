package com.digix.mvc.controllers.account;

import com.digix.mvc.model.entities.dao.User;
import com.digix.mvc.model.services.account.LogOutService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogOutController {

    @Autowired
    private LogOutService logOutService;

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RedirectView displayLogOut(RedirectAttributes redirectAttrs,
                                      HttpServletRequest request) {

        /* get email from logged in user */
        User user = new User();
        user.setEmail(request.getSession().getAttribute("email").toString());

        logOutService.execute(user);

        redirectAttrs.addFlashAttribute("msg", "You've been logged out successfully.");
        return new RedirectView("login");
    }
}
