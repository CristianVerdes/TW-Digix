package com.digix.mvc.model.entities.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UserProfile {

    private String userID;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private Timestamp timestamp;
    private String joinDate;
    private String avatarPath;
    private String birthday;
    private String gender;
    private String location;
    private String facebookURL;
    private String googleURL;
    private String linkedinURL;
    private String twitterURL;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        joinDate = new SimpleDateFormat("dd MMMM yyyy \'at\' hh:mm").format(timestamp);
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public String getGoogleURL() {
        return googleURL;
    }

    public void setGoogleURL(String googleURL) {
        this.googleURL = googleURL;
    }

    public String getLinkedinURL() {
        return linkedinURL;
    }

    public void setLinkedinURL(String linkedinURL) {
        this.linkedinURL = linkedinURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", joinDate='" + joinDate + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", facebookURL='" + facebookURL + '\'' +
                ", googleURL='" + googleURL + '\'' +
                ", linkedinURL='" + linkedinURL + '\'' +
                ", twitterURL='" + twitterURL + '\'' +
                '}';
    }
}
