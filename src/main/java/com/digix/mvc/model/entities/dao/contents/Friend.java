package com.digix.mvc.model.entities.dao.contents;

import org.springframework.social.facebook.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Daniel Moniry on 03.06.2016.
 */
public class Friend {
    private int friendID;
    private String userID;
    private String providerID;
    private String name;
    private String relationship;

    public Friend(ResultSet resultSet) throws SQLException {
        this.friendID = resultSet.getInt("FRIEND_ID");
        this.userID = resultSet.getString("USER_ID");
        this.providerID = resultSet.getString("PROVIDER_ID");
        this.name = resultSet.getString("NAME");
        this.relationship = resultSet.getString("RELATIONSHIP");
    }

    public Friend(FamilyMember familyMember) {
        this.providerID = familyMember.getId();
        this.name = familyMember.getName();
        this.relationship = familyMember.getRelationship();
    }

    public Friend(String userID, Tag tag) {
        this.userID = userID;
        this.providerID = tag.getProviderID();
        this.name = tag.getTagName();
        this.relationship = "friend";
    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendID=" + friendID +
                ", userID='" + userID + '\'' +
                ", providerID='" + providerID + '\'' +
                ", name='" + name + '\'' +
                ", relationship='" + relationship + '\'' +
                '}';
    }
}
