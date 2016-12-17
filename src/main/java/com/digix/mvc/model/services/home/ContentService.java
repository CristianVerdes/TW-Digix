package com.digix.mvc.model.services.home;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.entities.dao.contents.Friend;
import com.digix.mvc.model.entities.dao.contents.Location;
import com.digix.mvc.model.entities.dao.contents.Tag;
import com.digix.mvc.model.entities.utility.FileSaver;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ContentService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void updateContent(Content content) {
        try (Connection connection = getDataSource().getConnection()) {

            String query = "UPDATE LOCATIONS SET COUNTRY=?, CITY=?, STREET=? WHERE CONTENT_ID=?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, content.getLocation().getCountry());
            statement.setString(2, content.getLocation().getCity());
            statement.setString(3, content.getLocation().getStreet());
            statement.setString(4, content.getContentID());

            statement.execute();

            String description = "UPDATE CONTENTS SET DESCRIPTION=? WHERE CONTENT_ID=?";
            PreparedStatement statement1 = connection.prepareStatement(description);

            statement1.setString(1, content.getDescription());
            statement1.setString(2, content.getContentID());

            statement1.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert Content Object to DB for a specific :
     * a. userID
     * b. typeID : 1 = Photo
     * 2 = Video
     * 3 = Documents
     *
     * @param content the Content Object which is inserted to DB
     */
    public void addContent(Content content) {
        try (Connection connection = getDataSource().getConnection()) {

            String query = "{call CONTENT_UTILITY.ADD_CONTENT(?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement statement = connection.prepareCall(query);

            statement.setString(1, content.getUserID());
            statement.setString(2, content.getTypeID());
            statement.setString(3, content.getProvider());
            statement.setString(4, content.getPath());
            statement.setString(5, content.getThumbnailURL());
            statement.setString(6, content.getDescription());
            statement.setString(7, content.getProviderID());
            statement.setDate(8, new java.sql.Date(content.getUploadedDate().getTime()));

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert Location Object to DB for a specific Content referenced by a contentID
     *
     * @param location  Location Object
     * @param contentID reference for a specific Content
     */
    public void addLocation(Location location, String contentID) {
        try (Connection connection = getDataSource().getConnection()) {

            String query = "INSERT INTO LOCATIONS VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, contentID);
            statement.setString(2, location.getCountry());
            statement.setString(3, location.getCity());
            statement.setString(4, location.getStreet());
            statement.setString(5, String.valueOf(location.getLatitude()));
            statement.setString(6, String.valueOf(location.getLongitude()));

            statement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert Tag Object to DB for a specific
     *
     * @param contentID reference for a specific Content
     * @param friendID  reference for a specific Friend
     */
    public void addTag(String contentID, int friendID) {
        try (Connection connection = getDataSource().getConnection()) {

            String query = "INSERT INTO TAGS(CONTENT_ID, FRIEND_ID) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, contentID);
            statement.setInt(2, friendID);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert Friend Object to DB
     *
     * @param friend Friend object that's going to be stored
     * @return -1 if friend is already stored in db, otherwise the generated friendID
     */
    public int addFriend(Friend friend) {

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT FRIEND_ID FROM FRIENDS " +
                    "WHERE PROVIDER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, friend.getProviderID());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                sql = "INSERT INTO FRIENDS(PROVIDER_ID, USER_ID, NAME, RELATIONSHIP) " +
                        "VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(sql, new String[]{"FRIEND_ID"});
                statement.setString(1, friend.getProviderID());
                statement.setString(2, friend.getUserID());
                statement.setString(3, friend.getName());
                statement.setString(4, friend.getRelationship());

                statement.executeUpdate();

                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1); // returns friendID

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns a Content Object with details about Location and Tags.
     *
     * @param contentID reference for a specific Content
     * @return a Content Object
     */
    public Content getContent(String contentID) {
        Content content = new Content();

        try (Connection connection = getDataSource().getConnection()) {

            /* Get details from CONTENTS Table */
            String sql = "SELECT * FROM CONTENTS c " +
                    "JOIN USERS u ON c.USER_ID = u.USER_ID " +
                    "JOIN CONTENT_TYPES t ON t.TYPE_ID = c.TYPE_ID " +
                    "WHERE c.CONTENT_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contentID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                content = new Content(resultSet);
            } else return null;

            /* Get details from LOCATIONS Table */
            sql = "SELECT * FROM LOCATIONS WHERE CONTENT_ID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, contentID);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Location location = new Location(resultSet);
                content.setLocation(location);
            }

            /* Get details from TAGS Table */
            sql = "SELECT * FROM TAGS t " +
                    "JOIN FRIENDS f ON t.FRIEND_ID = f.FRIEND_ID " +
                    "WHERE CONTENT_ID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, contentID);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tag tag = new Tag(resultSet);
                content.addTag(tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * Returns a Content Object with a specific providerID
     *
     * @param providerID an id that is given by a
     *                   specific social media (facebook, twitter, etc.)
     * @return Content Object
     */
    public Content getContentFromProviderID(String providerID) {
        Content content = new Content();
        try (Connection connection = getDataSource().getConnection()) {

            String query = "SELECT * FROM CONTENTS c " +
                    "JOIN CONTENT_TYPES t ON c.TYPE_ID = t.TYPE_ID " +
                    "WHERE PROVIDER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, providerID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                content = new Content(resultSet);
            } else return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

    /**
     * Post Content operation update posted_date with current time for a Content
     *
     * @param contentID reference for a specific Content
     */
    public void postContent(String contentID) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "{ call CONTENT_UTILITY.POST_CONTENT(?) }";
            CallableStatement statement = connection.prepareCall(query);

            statement.setString(1, contentID);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pagination Method
     * <p>
     * Returns a list of contents(same type) for a specific user
     *
     * @param userID     reference for a specific User
     * @param typeID     reference for a specific Type
     * @param pageNumber the page number
     * @param pageSize   size of a page
     * @return list of contents from a certain page
     */
    public List<Content> getPage(String userID, int typeID, int pageNumber, int pageSize) {
        List<Content> contentList = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String sql = "SELECT * FROM ( " +
                    "SELECT a.*, rownum r FROM" +
                    "(SELECT * FROM CONTENTS WHERE TYPE_ID = ? AND USER_ID = ? ORDER BY CONTENT_ID DESC) a " +
                    "WHERE rownum < ((? * ?) + 1 ))WHERE r >= (((?-1) * ?) + 1)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, typeID);
            statement.setString(2, userID);
            statement.setInt(3, pageNumber);
            statement.setInt(4, pageSize);
            statement.setInt(5, pageNumber);
            statement.setInt(6, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Content content = new Content(resultSet);
                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (contentList.size() > 0) return contentList;
        else return null;
    }

    /**
     * Returns the number of pages that user is having for
     * a specific contents type and page size
     *
     * @param userID   reference for a specific User
     * @param typeID   reference for a specific Type
     * @param pageSize size of a page
     * @return number of pages
     */
    public int countPages(String userID, int typeID, int pageSize) {
        int contents = 0, pages;
        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT COUNT(*) FROM CONTENTS WHERE TYPE_ID = ? AND USER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, typeID);
            statement.setString(2, userID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                contents = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (contents <= pageSize) pages = 1;
        else {
            pages = contents / pageSize;
            if (contents % pageSize > 0) pages++;
        }
        return pages;
    }

    /**
     * Add contents to DB with location and tags
     *
     * @param photoContent Content Object
     */
    public void storeContent(Content photoContent) {

        /* add Content to CONTENTS TABLE */
        this.addContent(photoContent);
        String contentID = this.getContentFromProviderID(photoContent.getProviderID()).getContentID();

        /* add Content's location to LOCATIONS TABLE */
        if (photoContent.getLocation() != null) {
            this.addLocation(photoContent.getLocation(), contentID);
        }

        /* add Content's tags to TAGS TABLE */
        if (photoContent.getTagList() != null) {
            List<Tag> tagList = photoContent.getTagList();
            for (Tag tag : tagList) {

                Friend friend = new Friend(photoContent.getUserID(), tag);

                int friendID = this.addFriend(friend);
                this.addTag(contentID, friendID);
            }
        }
    }

    /**
     * Store a photo file(saving the file on the server side and storing it in DB)
     *
     * @param WEB_PATH      path corresponding to the given virtual path.
     * @param userID        reference for a specific User
     * @param socialContent content that is going to be stored
     */
    public Content savePhotoFile(String WEB_PATH, String userID, Content socialContent, String provider) {

        String filePath = "/resources/contents/" + userID + "/" + provider + "/images/" + socialContent.getProviderID() + ".jpg";
        new Thread(new FileSaver(socialContent.getPath(), WEB_PATH + filePath)).start();

        /* prepare Content object for storing */
        Content photoContent = new Content(userID, "1", provider, filePath, socialContent.getDescription(), socialContent.getProviderID());
        photoContent.setUploadedDate(socialContent.getUploadedDate());
        photoContent.setLocation(socialContent.getLocation());
        photoContent.setTagList(socialContent.getTagList());

        this.storeContent(photoContent);

        return this.getContentFromProviderID(socialContent.getProviderID());
    }

    /**
     * Store a photo from a specific URL
     *
     * @param WEB_PATH path corresponding to the given virtual path.
     * @param userID   reference for a specific User
     * @param photoURL photo URL
     * @return providerID of stored photo or null if the photo is already stored in db
     */
    public String storePhotoFile(String WEB_PATH, String userID, String photoURL) {
        String filePath = "/resources/contents/" + userID + "/uploads/images/" + photoURL.hashCode() + ".jpg";
        new Thread(new FileSaver(photoURL, WEB_PATH + filePath)).start();

        boolean isUploaded = this.getContentFromProviderID(String.valueOf(photoURL.hashCode())) == null;
        if (isUploaded) {
            Content photoContent = new Content(userID, "1", null, filePath, null, String.valueOf(photoURL.hashCode()));
            photoContent.setUploadedDate(new java.util.Date());
            this.addContent(photoContent);
            return String.valueOf(photoURL.hashCode());
        } else return null;
    }

    /**
     * Get a list of content that represents all photos that are owned by a specific user
     *
     * @param userID reference for a specific User
     * @return list of contents
     */
    public List<Content> getContents(String userID, int typeID) {
        List<Content> contentList = new LinkedList<>();

        try (Connection connection = getDataSource().getConnection()) {

            String sql =
                    "SELECT * FROM CONTENTS c " +
                            "JOIN CONTENT_TYPES t ON c.TYPE_ID = t.TYPE_ID  " +
                            "WHERE c.USER_ID = ? AND c.TYPE_ID = ? " +
                            "ORDER BY c.CONTENT_ID";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setInt(2, typeID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Content content = new Content(resultSet);
                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;
    }

    /**
     * Returns a content of a specific type from an user
     *
     * @param userID    reference for a specific User
     * @param contentID reference for a specific Content
     * @param typeID    reference for a specific Type Content
     * @return a content(photo, video, document)
     */
    public Content getContent(String userID, String contentID, int typeID) {
        Content content = new Content();

        try (Connection connection = getDataSource().getConnection()) {

            String sql =
                    "SELECT * FROM CONTENTS c " +
                            "JOIN CONTENT_TYPES t ON c.TYPE_ID = t.TYPE_ID " +
                            "WHERE c.USER_ID = ? AND c.CONTENT_ID = ? AND c.TYPE_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setString(2, contentID);
            statement.setInt(3, typeID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                content = new Content(resultSet);
            } else content = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * Delete a content of specific type from database with the corresponding contentID
     *
     * @param contentID reference for a specific Content
     * @param typeID    reference for a specific Type Content
     * @return true if the content was successfully deleted or false otherwise
     */
    public boolean deleteContent(String contentID, int typeID) {

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT * FROM CONTENTS WHERE CONTENT_ID = ? AND TYPE_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contentID);
            statement.setInt(2, typeID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sql = "DELETE FROM CONTENTS WHERE CONTENT_ID = ? AND TYPE_ID = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, contentID);
                statement.setInt(2, typeID);

                statement.executeUpdate();
                return true;
            } else return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Content> getPostedContent(String userID, String typeID) {

        List<Content> contentList = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT * FROM CONTENTS " +
                    "WHERE USER_ID = ? AND TYPE_ID = ? AND CONTENTS.POSTED_DATE IS NOT NULL";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setString(2, typeID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Content content = new Content(resultSet);
                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;
    }
}
