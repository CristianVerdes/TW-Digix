package com.digix.mvc.model.services.home.social;

import com.digix.mvc.model.entities.dao.PostPage;
import com.digix.mvc.model.entities.dao.UserProfile;
import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.entities.dao.contents.Friend;
import com.digix.mvc.model.entities.utility.PhotoSourceReader;
import com.digix.mvc.model.services.home.ContentService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service(value = "facebookService")
public class FacebookService {

    private final Facebook facebook;

    private ContentService contentService;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    public FacebookService(Facebook facebook, ContentService contentService) {
        this.facebook = facebook;
        this.contentService = contentService;
    }

    /**
     * Retrieves a list of albums belonging to the authenticated user. Requires "user_photos" permission.
     *
     * @return a list Albums for the user, or an empty list if not available.
     */
    public PagedList<Album> getAlbums() {
        return facebook.mediaOperations().getAlbums();
    }

    /**
     * Retrieves data for a specific album. Requires "user_photos" permission if the album is not public.
     *
     * @param albumID the album ID
     * @return the requested Album object.
     */
    public Album getAlbum(String albumID) {
        return facebook.mediaOperations().getAlbum(albumID);
    }

    /**
     * Retrieves data for up to 25 photos from a specific album or that a user is tagged in.
     * If the objectId parameter is the ID of an album, the photos returned are the photos from that album.
     * If the objectId parameter is the ID of a user, the photos returned are the photos that the user is tagged in.
     * Requires "user_photos" permission if the album is not public.
     *
     * @param albumID an album ID
     * @return a list of Photos in the specified album.
     */
    public PagedList<Photo> getPhotos(String albumID) {
        PagedList<Photo> facebookPhotos = facebook.mediaOperations().getPhotos(albumID);

        PagedList<Photo> photos = facebook.mediaOperations().getPhotos(albumID);
        photos.clear(); /* just to instantiate a PageList<Photo> Object */

//        if (contentService != null) {
        /* add only not stored photos to view */
        for (Photo photo : facebookPhotos) {
            Content content = contentService.getContentFromProviderID(photo.getId());
            if (content == null) photos.add(photo);
        }
//        } else return facebookPhotos;
        return photos;
    }

    /**
     * Get a certain Photo Object with from an album, having album ID and photo ID.
     *
     * @param albumID an album ID
     * @param photoID a photo ID
     * @return a photo in the specified album, with a specified photo id
     */
    public Content getPhoto(String albumID, String photoID) {
        Album album = facebook.mediaOperations().getAlbum(albumID);
        PagedList<Photo> photos = facebook.mediaOperations().getPhotos(album.getId());
        for (Photo photo : photos) {
            if (photo.getId().equals(photoID)) {
                return new Content(photo);
            }
        }
        return null;
    }

    /**
     * Returns a list of contents from posts that are not already stored in database
     *
     * @param accessToken security credentials for a user given by a social provider
     * @return a list of contents from posts
     */
    public List<Content> getContentPosts(String accessToken, Post.PostType type) {

        List<Content> photos = new ArrayList<>();

        PagedList<Post> posts = facebook.feedOperations().getFeed();

        photos.addAll(filter(accessToken, posts, type));
        while (posts.getNextPage() != null) {
            PagingParameters pagingParameters = posts.getNextPage();
            posts = facebook.feedOperations().getFeed(pagingParameters);
            photos.addAll(filter(accessToken, posts, type));
        }
        return photos;
    }

    /**
     * Filter photos that are not already stored in DB.
     *
     * @param accessToken security credentials for a user given by a social provider
     * @param posts       a list of post Posts.
     * @param type        post type such as PHOTO, VIDEO, LINK, STATUS
     * @return a list of content that is going to filtered by post type
     */
    private List<Content> filter(String accessToken, PagedList<Post> posts, Post.PostType type) {
        List<Content> photos = new ArrayList<>();
        if (type.equals(Post.PostType.PHOTO)) {
            for (Post post : posts) {
                if (post.getType().equals(Post.PostType.PHOTO)) {

                    String source = PhotoSourceReader.getSource(post.getObjectId(), accessToken);

                    Content photoPost = new Content();
                    photoPost.setProviderID(post.getObjectId());
                    photoPost.setPath(source);

                /* add photo only if it not already stored in db */
                    Content content = contentService.getContentFromProviderID(photoPost.getProviderID());
                    if (content == null) {
                        List<Reference> referenceList = post.getWithTags();
                        if (referenceList != null) photoPost.setTags(referenceList);

                        if (post.getMessage() != null) photoPost.setDescription(post.getMessage());

                        if (post.getPlace() != null && post.getPlace().getLocation() != null)
                            photoPost.setLocation(post.getPlace().getLocation());

                        photoPost.setUploadedDate(post.getCreatedTime());

                        photos.add(photoPost);
                    }
                }
            }
        } else if (type.equals(Post.PostType.VIDEO)) {
            for (Post post : posts) {
                if (post.getType().equals(Post.PostType.VIDEO)) {
                    Content videoPost = new Content();

                    Content content = contentService.getContentFromProviderID(post.getId());


                    if (content == null) {
                        /* set ID*/
                        videoPost.setProviderID(post.getId());

                        /* set description */
                        if (post.getMessage() != null) videoPost.setDescription(post.getMessage());

                        /* set link */
                        String link = post.getLink();
                        if (link != null && link.contains("facebook")) videoPost.setPath(link);

                        /* set referencesList */
                        List<Reference> referenceList = post.getWithTags();
                        if (referenceList != null) videoPost.setTags(referenceList);

                        /* set location */
                        if (post.getPlace() != null && post.getPlace().getLocation() != null) {
                            videoPost.setLocation(post.getPlace().getLocation());
                        }

                        /* set uploaded date from facebook */
                        videoPost.setUploadedDate(post.getCreatedTime());
                        if (videoPost.getPath() != null) {
                            photos.add(videoPost);
                        }
                    }
                }
            }

        }
        return photos;
    }

    /**
     * Get family members from facebook
     * Create a list of Friend type with that info
     *
     * @return a list of family members
     */
    public List<Friend> getFamily() {
        PagedList<FamilyMember> familyMembers = facebook.friendOperations().getFamily();
        List<Friend> friends = new ArrayList<>();

        for (FamilyMember familyMember : familyMembers) {
            Friend friend = new Friend(familyMember);
            friends.add(friend);
        }

        return friends;
    }

    /**
     * Get nextPage of contents from posts of a specific type
     *
     * @param accessToken     security credentials for a user given by a social provider
     * @param type            post type such as PHOTO, VIDEO, LINK, STATUS
     * @param currentPostPage reference of currentPostPage
     * @return a PostPage object
     */
    public PostPage getNextPage(String accessToken, Post.PostType type, PostPage currentPostPage) {


        List<Content> photos = new ArrayList<>();

        if (currentPostPage == null) {
            currentPostPage = new PostPage();
            currentPostPage.setCurrentPage(facebook.feedOperations().getFeed());
        } else {
            currentPostPage.setCurrentPage(facebook.feedOperations().getFeed(currentPostPage.getCurrentPage().getNextPage()));
        }
        photos.addAll(filter(accessToken, currentPostPage.getCurrentPage(), type));
        while (photos.size() == 0 && currentPostPage.getCurrentPage().getNextPage() != null) {
            PagingParameters pagingParameters = currentPostPage.getCurrentPage().getNextPage();
            currentPostPage.setCurrentPage(facebook.feedOperations().getFeed(pagingParameters));
            photos.addAll(filter(accessToken, currentPostPage.getCurrentPage(), type));
        }
        currentPostPage.setContentList(photos);

        return currentPostPage;
    }

    /**
     *
     * Get userProfile information such as avatar, birthday, gender, location
     *
     * @return UserProfile Object
     */
    public UserProfile getUserProfile() {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);

        User facebookProfile = facebook.userOperations().getUserProfile();

        UserProfile userProfile = new UserProfile();
        userProfile.setAvatarPath(connection.getImageUrl());
        userProfile.setBirthday(facebookProfile.getBirthday());
        userProfile.setGender(facebookProfile.getGender());
        userProfile.setLocation(facebookProfile.getLocation().getName());

        return userProfile;
    }


}
