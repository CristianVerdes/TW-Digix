package com.digix.mvc.model.entities.dao.contents;

import com.digix.mvc.model.entities.dao.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.social.facebook.api.Photo;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.google.api.drive.DriveFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Content {

    private String contentID;
    private String userID;
    private String typeID;
    private String type;
    private String provider;
    private String path;
    private String description;
    private Date postedDate;
    private String postedDateString;
    private Date uploadedDate;
    private String providerID;
    private User user = new User();
    private Location location = new Location();
    private List<Tag> tagList = new ArrayList<>();
    private String thumbnailURL;


    public Content() {
    }


    public Content(ResultSet resultSet) throws SQLException {
        setContentID(resultSet.getString("CONTENT_ID"));
        setUserID(resultSet.getString("USER_ID"));
        setTypeID(resultSet.getString("TYPE_ID"));
        try {
            setType(resultSet.getString("TYPE"));
        } catch (SQLException e) {
            e.getErrorCode();
        }
        setProvider(resultSet.getString("PROVIDER"));
        setPath(resultSet.getString("PATH"));
        setDescription(resultSet.getString("DESCRIPTION"));
        setPostedDate(resultSet.getDate("POSTED_DATE"));
        setPostedDateString(resultSet.getTimestamp("POSTED_DATE"));
        setUploadedDate(resultSet.getDate("UPLOADED_DATE"));
        setProviderID(resultSet.getString("PROVIDER_ID"));
        setThumbnailURL(resultSet.getString("THUMBNAIL_LINK"));
        try {
            user.setFirstName(resultSet.getString("FIRST_NAME"));
            user.setLastName(resultSet.getString("LAST_NAME"));
            user.setUserID(resultSet.getInt("USER_ID"));
            user.setEmail(resultSet.getString("EMAIL"));
            user.setStatus(resultSet.getString("STATUS"));
        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    public Content(String userID, String typeID, String provider, String path, String description, String providerID) {
        this.userID = userID;
        this.typeID = typeID;
        this.provider = provider;
        this.path = path;
        this.description = description;
        this.providerID = providerID;
        this.tagList = new ArrayList<>();
    }

    public Content(Photo photo) {
        this.providerID = photo.getId();
        this.path = photo.getSource();
        this.description = photo.getName();
        setUploadedDate(photo.getCreatedTime());
        setLocation(photo.getPlace());
        setTagsFromPhoto(photo.getTags());
    }

    public Content(DriveFile driveFile) {
        setProviderID(driveFile.getId());
        setDescription(driveFile.getTitle());
        setPath(driveFile.getEmbedLink());
        setThumbnailURL(driveFile.getThumbnailLink());
        setUploadedDate(driveFile.getCreatedDate());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
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

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(Timestamp timestampPostedDate) {
        if (timestampPostedDate != null) {
            postedDateString = new SimpleDateFormat("dd MMMM \'at\' hh:mm").format(timestampPostedDate);
        }
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(org.springframework.social.facebook.api.Location location) {
        if (location != null) {
            this.location = new Location(location);
        }
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void setTags(List<Reference> referenceList) {
        for (Reference reference : referenceList) {
            tagList.add(new Tag(reference));
        }
    }

    public void setTagsFromPhoto(List<org.springframework.social.facebook.api.Tag> tagList) {
        if (tagList != null) {
            for (org.springframework.social.facebook.api.Tag tag : tagList) {
                this.tagList.add(new Tag(tag));
            }
        }
    }

    public void addTag(Tag tag) {
        this.tagList.add(tag);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if(this.path.equals(content.getPath())) return true;
        else return false;

    }

    @Override
    public int hashCode() {
        int result = contentID.hashCode();
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (typeID != null ? typeID.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override

    public String toString() {
        return "Content{" +
                "contentID='" + contentID + '\'' +
                ", userID='" + userID + '\'' +
                ", typeID='" + typeID + '\'' +
                ", provider='" + provider + '\'' +
                ", path='" + path + '\'' +
                ", description='" + description + '\'' +
                ", postedDate=" + postedDate +
                ", postedDateString='" + postedDateString + '\'' +
                ", uploadedDate=" + uploadedDate +
                ", providerID='" + providerID + '\'' +
                ", user=" + user +
                ", location=" + location +
                ", tagList=" + tagList +
                '}';
    }
}
