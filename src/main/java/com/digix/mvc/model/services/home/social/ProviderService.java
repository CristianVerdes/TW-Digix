package com.digix.mvc.model.services.home.social;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProviderService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Total number of social connections for a specific user
     *
     * @param userID reference for a specific User
     * @return number of social connections
     */
    public int countConnection(String userID) {
        int connections = 0;
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT COUNT(*) FROM USERCONNECTION WHERE USERID=?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            connections = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connections;
    }

    /**
     * Get access token of social media(facebook, twitter, etc.) for a specific user
     *
     * @param email    user's email
     * @param provider provider's name (ex: "facebook", "twitter", etc.)
     * @return access token
     */
    public String getAccessToken(String email, String provider) {
        String accessToken = null;
        try (Connection connection = getDataSource().getConnection()) {
            String sql = "SELECT ACCESSTOKEN FROM USERCONNECTION WHERE USERID = ? AND PROVIDERID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, provider);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) accessToken = resultSet.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}
