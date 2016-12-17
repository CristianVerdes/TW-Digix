package com.digix.mvc.model.services.home;

import com.digix.mvc.model.entities.dao.contents.Content;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedService {

    private DataSource dataSource;

    private ContentService contentService;

    public ContentService getContentService() {
        return contentService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Pagination method for News Feed
     *
     * @param pageNumber page number
     * @return a list of contents (different types)
     */
    public List<Content> getNewsFeed(int pageNumber, int pageSize) {

        List<Content> contentList = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {
            String sql =
                    "SELECT * " +
                    "FROM ( " +
                            "SELECT rownum AS rn, c.* " +
                            "FROM ( " +
                                    "SELECT * FROM CONTENTS con " +
                                    "JOIN USERS u ON u.USER_ID = con.USER_ID " +
                                    "WHERE POSTED_DATE IS NOT NULL " +
                                    "ORDER BY POSTED_DATE DESC " +
                                  ") c " +
                          ") " +
                    "WHERE rownum <= ? " +
                    "AND rn > (? - 1) * ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, pageSize);
            statement.setInt(2, pageNumber);
            statement.setInt(3, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Content content = new Content(resultSet);
                Content fullContent = contentService.getContent(content.getContentID());
                contentList.add(fullContent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }
}
