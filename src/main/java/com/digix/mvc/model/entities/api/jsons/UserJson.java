package com.digix.mvc.model.entities.api.jsons;


import com.digix.mvc.model.entities.dao.UserProfile;

public class UserJson {

    private String userID;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String joinDate;
    private String avatarPath;

    public UserJson(UserProfile userProfile) {
        this.userID = userProfile.getUserID();
        this.email = userProfile.getEmail();
        this.firstName = userProfile.getFirstName();
        this.lastName = userProfile.getLastName();
        this.status = userProfile.getStatus();
        this.joinDate = userProfile.getJoinDate();
        this.avatarPath = userProfile.getAvatarPath();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}
