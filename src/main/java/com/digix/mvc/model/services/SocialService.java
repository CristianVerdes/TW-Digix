package com.digix.mvc.model.services;


import com.digix.mvc.model.entities.dao.contents.Friend;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Verdes on 6/5/2016.
 */
public class SocialService {
    private DataSource dataSource;

    public DataSource getDataSource() { return dataSource; }

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public List<Friend> getFamily(String userID){
        List<Friend> friends = new ArrayList<>();


        try(Connection connection = getDataSource().getConnection()) {
            String sql = "SELECT * FROM FRIENDS " +
                                "WHERE USER_ID=? AND RELATIONSHIP NOT LIKE 'friend'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Friend friend = new Friend(resultSet);
                friends.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    public List<Friend> getFriends(String userID){
        List<Friend> friends = new ArrayList<>();


        try(Connection connection = getDataSource().getConnection()) {
            String sql = "SELECT * FROM FRIENDS " +
                    "WHERE USER_ID=? AND RELATIONSHIP LIKE 'friend'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Friend friend = new Friend(resultSet);
                friends.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }
}
