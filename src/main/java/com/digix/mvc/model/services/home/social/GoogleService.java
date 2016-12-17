package com.digix.mvc.model.services.home.social;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.services.home.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.social.google.api.drive.DriveFilesPage;
import org.springframework.social.google.api.plus.ActivitiesPage;
import org.springframework.social.google.api.plus.Activity;
import org.springframework.social.google.api.query.ApiPage;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service(value = "googleService")
public class GoogleService {

    private final Google google;

    @Autowired
    private ContentService contentService;

    @Inject
    public GoogleService(Google google) {
        this.google = google;
    }

    //FILES
    /**
     * Returns a list of contents that are not already stored in db
     *
     * @return a list of documents
     */
    public List<Content> getFiles() {

        List<Content> documents = new ArrayList<>();

        DriveFilesPage files = google.driveOperations().getFiles("root", null);
        List<DriveFile> driveFileList = files.getItems();

        for (DriveFile driveFile : driveFileList) {
            if (driveFile.getEmbedLink() != null && driveFile.getThumbnailLink() != null) {

                Content dbContent = contentService.getContentFromProviderID(driveFile.getId());
                if (dbContent == null) {
                    Content document = new Content();
                    document.setProviderID(driveFile.getId());
                    document.setDescription(driveFile.getTitle());
                    document.setPath(driveFile.getEmbedLink());
                    document.setThumbnailURL(driveFile.getThumbnailLink());
                    document.setUploadedDate(driveFile.getCreatedDate());

                    documents.add(document);
                }
            }
        }
        return documents;
    }

    /**
     * Returns a Content object for a specific DriveFile
     *
     * @param fileID reference for a specific DriveFile Object
     * @return Content Object
     */
    public Content getFile(String fileID) {

        DriveFile driveFile = google.driveOperations().getFile(fileID);
        return new Content(driveFile);
    }


    //PHOTOS
    /**
     * Returns a list of photos that are not already stored in db
     *
     * @return a list of photos
     */
    public List<Content> getPhotos(){
        List<Content> photos = new ArrayList<>();

        String nextPageToken = null;

        do {
            ActivitiesPage activitiesPage = google.plusOperations().getActivities("me",nextPageToken);
            if(activitiesPage != null) {
                for (Activity activity : activitiesPage.getItems()) {
                    for (Activity.Attachment attachment : activity.getAttachments()) {
                        if (attachment instanceof Activity.Photo) {
                            Content dbContent = contentService.getContentFromProviderID(activity.getId());
                            if(dbContent == null) {
                                Content photo = googlePhotoToContent(activity, attachment);
                                photos.add(photo);
                            }
                        }
                    }
                }
                nextPageToken = activitiesPage.getNextPageToken();
            }
        }while (nextPageToken != null);


        return photos;
    }

    /**
     * Returns a Content object from an Attachment
     *
     * @param activity the activity from google
     * @param attachment the attachment from the activity
     * @return the Content object
     */
    private Content googlePhotoToContent(Activity activity, Activity.Attachment attachment) {
        Content content = new Content();
        content.setProviderID(activity.getId());
        content.setDescription(activity.getTitle());
        content.setPath(attachment.getPreviewImageUrl());
        content.setUploadedDate(activity.getPublished());
        return content;
    }

    /**
     * Returns one photo from Google
     * Search is done with the url of the photo
     *
     * @param providerID the ID from google
     * @param url the url of the photo
     * @return one Content object created with activity and url
     */

    public Content getPhoto(String providerID, String url){
        Activity googleActivity = google.plusOperations().getActivity(providerID);
        if (googleActivity != null) {
            for (Activity.Attachment attachment : googleActivity.getAttachments()) {
                if (attachment.getPreviewImageUrl().equals(url)) {
                    return googlePhotoToContent(googleActivity, attachment);
                }
            }
        }
        return null;
    }


    //VIDEOS
    /**
     * Returns a list of videos that are not already stored in db
     *
     * @return list of videos
     */
    public List<Content> getVideos(){
        List<Content> videos = new ArrayList<>();
        String nextPageToken = null;

        do{
            ActivitiesPage activitiesPage = google.plusOperations().getActivities("me", nextPageToken);
            if(activitiesPage != null){
                for(Activity activity:activitiesPage.getItems()){
                    for(Activity.Attachment attachment : activity.getAttachments()){
                        if(attachment instanceof Activity.Video) {
                            Content dbContent = contentService.getContentFromProviderID(activity.getId());
                            if (dbContent == null) {
                                Content video = googleVideoToContent(activity, attachment);
                                videos.add(video);
                            }
                        }
                    }
                }
            }
            nextPageToken = activitiesPage.getNextPageToken();
        }while(nextPageToken != null);

        return videos;
    }

    /**
     * Returns one video from Google
     * Search is done with the url of the video
     *
     * @param providerID the ID from google
     * @param url the url of the video
     * @return one Content object created with activity and url
     */

    public Content getVideo(String providerID, String url){
        Activity googleActivity = google.plusOperations().getActivity(providerID);
        if (googleActivity != null) {
            for (Activity.Attachment attachment : googleActivity.getAttachments()) {
                String[] splitedUrl = url.split("embed/");
                if (attachment.getUrl().equals("https://www.youtube.com/watch?v=" + splitedUrl[1])) {
                    return googleVideoToContent(googleActivity, attachment);
                }
            }
        }
        return null;
    }

    /**
     * Returns a Content object from an Attachment
     *
     * @param activity the activity from google
     * @param attachment the video from the activity
     * @return the Content object
     */

    private Content googleVideoToContent(Activity activity, Activity.Attachment attachment) {
        Content video = new Content();
        video.setProviderID(activity.getId());
        video.setDescription(activity.getTitle());
        String[] split = attachment.getUrl().split("=");
        video.setPath("https://www.youtube.com/embed/" + split[1]);
        video.setUploadedDate(activity.getPublished());
        return video;
    }

}
