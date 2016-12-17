package com.digix.mvc.model.entities.api.jsons;


import com.digix.mvc.model.entities.dao.contents.Content;

import java.util.Date;

public class ContentJson {

    private String contentID;
    private String typeID;
    private String type;
    private String provider;
    private String path;
    private String description;
    private Date postedDate;
    private Date uploadedDate;

    public ContentJson(Content content) {
        contentID = content.getContentID();
        typeID = content.getTypeID();
        type = content.getType();
        provider = content.getProvider();
        path = content.getPath();
        description = content.getDescription();
        postedDate = content.getPostedDate();
        uploadedDate = content.getUploadedDate();
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
