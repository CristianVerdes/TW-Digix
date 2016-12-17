package com.digix.mvc.model.entities.dao.contents;

import org.springframework.social.facebook.api.Reference;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tag {

    private String contentID;
    private String tagName;
    private String providerID;
    private String relationship;


    public Tag(ResultSet resultSet) throws SQLException {
        this.contentID = resultSet.getString("CONTENT_ID");
        this.tagName = resultSet.getString("NAME");
        this.relationship = resultSet.getString("RELATIONSHIP");
    }

    public Tag(org.springframework.social.facebook.api.Tag tag) {
        this.tagName = tag.getName();
        this.providerID = tag.getId();
    }

    public Tag(Reference reference) {
        this.tagName = reference.getName();
        this.providerID = reference.getId();
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "contentID='" + contentID + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
