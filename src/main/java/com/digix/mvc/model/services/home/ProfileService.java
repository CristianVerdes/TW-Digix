package com.digix.mvc.model.services.home;


import com.digix.mvc.model.entities.dao.UserProfile;

import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ProfileService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param email of a User
     * @return userID reference for a specific User
     */
    public String getUserID(String email) {
        String userID = null;
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getString("USER_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * Get UserProfile Object for a specific userID
     *
     * @param userID reference for a specific User
     * @return user profiles details for an user
     */
    public UserProfile getUserProfile(String userID) {
        UserProfile userProfile = new UserProfile();
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT * FROM USER_PROFILES p " +
                    "JOIN USERS u ON p.USER_ID = u.USER_ID WHERE p.USER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userProfile.setUserID(resultSet.getString("USER_ID"));
                userProfile.setTimestamp(resultSet.getTimestamp("JOIN_DATE"));
                userProfile.setAvatarPath(resultSet.getString("AVATAR_PATH"));
                userProfile.setEmail(resultSet.getString("EMAIL"));
                userProfile.setFirstName(resultSet.getString("FIRST_NAME"));
                userProfile.setLastName(resultSet.getString("LAST_NAME"));
                userProfile.setStatus(resultSet.getString("STATUS"));
                userProfile.setBirthday(resultSet.getString("BIRTHDAY"));
                userProfile.setGender(resultSet.getString("GENDER"));
                userProfile.setLocation(resultSet.getString("LOCATION"));
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfile;
    }

    /**
     * Edit User Profile fields (firstname, lastname, etc.)
     *
     * @param userProfile Object
     */
    public void editUserProfile(UserProfile userProfile) {
        try (Connection connection = getDataSource().getConnection()) {
            String sql = "UPDATE DIGIX.USERS SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ? WHERE USER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, userProfile.getFirstName());
            statement.setString(2, userProfile.getLastName());
            statement.setString(3, userProfile.getEmail());
            statement.setString(4, userProfile.getUserID());

            statement.execute();

            String sql2 = "UPDATE DIGIX.USER_PROFILES SET LOCATION = ? WHERE USER_ID = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement2.setString(1,userProfile.getLocation());
            statement2.setString(2, userProfile.getUserID());

            statement.execute();


        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    /**
     * update USER_PROFILES table with birthday, gender, location and avatar_path
     *
     * @param userID      reference for a specific User
     * @param userProfile UserProfile object
     */
    public void saveFacebookProfile(String userID, UserProfile userProfile) {

        try (Connection connection = getDataSource().getConnection()) {

            String sql = "UPDATE USER_PROFILES " +
                    "SET BIRTHDAY = ?, GENDER = ?, LOCATION = ?, AVATAR_PATH = ?" +
                    "WHERE USER_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userProfile.getBirthday());
            statement.setString(2, userProfile.getGender());
            statement.setString(3, userProfile.getLocation());
            statement.setString(4, userProfile.getAvatarPath());
            statement.setString(5, userID);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getProfileURL(String email, String provider) {

        String profileURL = null;

        try (Connection connection = getDataSource().getConnection()) {
            String sql = "SELECT PROFILEURL FROM USERCONNECTION " +
                    "WHERE USERID = ? AND PROVIDERID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, provider);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                profileURL = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileURL;
    }

}
