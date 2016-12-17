package com.digix.mvc.model.services.home;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.entities.dao.SearchFilter;
import com.digix.mvc.model.entities.utility.StringParser;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ContentService contentService;

    public ContentService getContentService() {
        return contentService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public String getStatement(SearchFilter searchFilter) {
        String returnedStatement = "";

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT * FROM CONTENTS c FULL OUTER JOIN USERS U ON u.USER_ID = c.USER_ID ";
            String whereClause = "WHERE ";
            String join = "";

            if (searchFilter.getLocation() != null) {
                join += "FULL OUTER JOIN LOCATIONS loc ON loc.CONTENT_ID = c.CONTENT_ID ";
                whereClause += "(LOWER(loc.CITY) = LOWER('" + searchFilter.getLocation() + "') OR LOWER(loc.COUNTRY) = LOWER('" + searchFilter.getLocation() + "')) AND ";
            }
            if (searchFilter.getUsername() != null) {
                join += "JOIN USERS u ON u.USER_ID = c.USER_ID ";
                whereClause += "(DB_UTILITY.normalize(u.FIRST_NAME) = DB_UTILITY.normalize('" + searchFilter.getUsername() + "') OR DB_UTILITY.normalize(u.LAST_NAME) = DB_UTILITY.normalize('" + searchFilter.getUsername() + "')) AND ";
            }
            if (searchFilter.getContentType() != 0) {
                whereClause += "c.TYPE_ID = '" + searchFilter.getContentType() + "' AND ";
            }
            if (searchFilter.getYear() != null) {
                whereClause += "EXTRACT(YEAR FROM c.UPLOADED_DATE) = '" + searchFilter.getYear() + "' AND ";
            }
            if (searchFilter.getMonth() > 0) {
                whereClause += "EXTRACT(MONTH FROM c.UPLOADED_DATE) = '" + searchFilter.getMonth() + "' AND ";
            }
            if (searchFilter.getMaxAge() > 0) {
                join += "JOIN USER_PROFILES up ON up.USER_ID = u.USER_ID ";
                whereClause += "months_between(c.UPLOADED_DATE, TO_DATE(up.BIRTHDAY, 'mm/dd/yyyy'))/12 BETWEEN " + searchFilter.getMinAge() + " AND " + searchFilter.getMaxAge() + " AND ";
            }
            if (searchFilter.getTags().size() > 0 || searchFilter.getRelatives() != null) {
                join += "JOIN TAGS t ON t.CONTENT_ID = c.CONTENT_ID JOIN FRIENDS f on f.FRIEND_ID=t.FRIEND_ID ";
                if (searchFilter.getRelatives() != null) {
                    whereClause += "f.RELATIONSHIP = '" + searchFilter.getRelatives() + "' AND ";
                }
                if (searchFilter.getTags().size() > 0) {
                    whereClause += "(";
                    for (String tag : searchFilter.getTags()) {
                        whereClause += "DB_UTILITY.normalize(f.NAME) like '%" + StringParser.ToASCII(tag.toLowerCase()) + "%' OR ";
                    }
                    whereClause = whereClause.substring(0, whereClause.length() - 3);
                    whereClause += ") AND ";
                }
            }
            whereClause = whereClause.substring(0, whereClause.length() - 4);
            whereClause += " AND c.POSTED_DATE IS NOT NULL ORDER BY c.POSTED_DATE DESC";
            returnedStatement = sql.concat(join).concat(whereClause);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnedStatement;
    }

    public List<Content> getContents(String statement) {
        List<Content> contentList = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Content content = new Content(resultSet);
                Content fullContent = contentService.getContent(content.getContentID());
                contentList.add(fullContent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(new LinkedHashSet<>(contentList));
    }

}
