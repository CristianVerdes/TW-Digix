package com.digix.mvc.model.entities.api.responses;

import com.digix.mvc.model.entities.api.jsons.UserJson;
import com.digix.mvc.model.entities.dao.UserProfile;

public class UserResponse {

    private String message;
    private UserJson user;


    public UserResponse(String message, UserProfile userProfile) {
        this.message = message;
        if(userProfile != null) this.user = new UserJson(userProfile);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserJson getUser() {
        return user;
    }

    public void setUser(UserJson user) {
        this.user = user;
    }
}
