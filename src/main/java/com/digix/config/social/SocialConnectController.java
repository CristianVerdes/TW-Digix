package com.digix.config.social;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.ui.Model;
import org.springframework.web.context.request.NativeWebRequest;

public class SocialConnectController extends ConnectController {

    public SocialConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }

    @Override
    public String connectionStatus(String providerId, NativeWebRequest request, Model model) {
        return "redirect:/settings/socialsync";
    }
}
