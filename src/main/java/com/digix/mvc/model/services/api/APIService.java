package com.digix.mvc.model.services.api;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApiService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns an access token for a specific user from provider
     *
     * @param userID   reference for a specific User
     * @param provider name (e.g. facebook, twitter, google)
     * @return accesstoken
     */
    public String getAccessToken(String userID, String provider) {
        String accessToken = null;

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT uc.ACCESSTOKEN FROM USERCONNECTION uc " +
                    "JOIN USERS u ON uc.USERID = u.EMAIL " +
                    "WHERE u.USER_ID = ? AND uc.PROVIDERID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userID);
            statement.setString(2, provider);


            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                accessToken = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /**
     * Check if access token is found in database
     *
     * @param accessToken represents the token used by an user for API access
     * @return true if token exists in database or false otherwise
     */
    public boolean isValidAccessToken(String accessToken) {
        boolean isValid = false;

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT * FROM USERS WHERE ACCESSTOKEN = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, accessToken);

            ResultSet resultSet = statement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    /**
     * Verify if the user with the corresponding access token is the owner
     * of the current content
     *
     * @param contentID   reference for a specific Content
     * @param accessToken represents the token used by an user for API access
     * @return true if the user is the owner of the specific content or false otherwise
     */
    public boolean isValidContentRequest(String contentID, String accessToken) {
        boolean isValid = false;

        try (Connection connection = getDataSource().getConnection()) {
            String sql =
                    "SELECT u.ACCESSTOKEN FROM CONTENTS c " +
                            "JOIN USERS u ON c.USER_ID = u.USER_ID " +
                            "WHERE CONTENT_ID = ? AND u.ACCESSTOKEN = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contentID);
            statement.setString(2, accessToken);

            ResultSet resultSet = statement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    /**
     * Get userID of an user with a specific access token
     *
     * @param accessToken represents the token used by an user for API access
     * @return the userID
     */
    public String getUserID(String accessToken) {
        String userID = null;

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "SELECT USER_ID FROM USERS WHERE ACCESSTOKEN = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, accessToken);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userID = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }

}
