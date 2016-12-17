package com.digix.mvc.controllers.account;

import com.digix.mvc.model.entities.dao.User;
import com.digix.mvc.model.services.account.RegisterService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RedirectView executeSignUp(@ModelAttribute User user,
                                      RedirectAttributes redirectAttributes) {

        if (registerService.passwordMatch(user)) {
            boolean userIsInserted = registerService.execute(user);
            if (userIsInserted) redirectAttributes.addFlashAttribute("msg", "You have successfully registered.");
            else redirectAttributes.addFlashAttribute("msg", "E-mail is already in use.");

        } else redirectAttributes.addFlashAttribute("msg", "Passwords do not match.");

        return new RedirectView("login");
    }

}
