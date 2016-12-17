package com.digix.mvc.model.services.account;

import com.digix.mvc.model.entities.dao.User;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class LogOutService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Log Out user operation modify STATUS column in DB from ONLINE to OFFLINE
     *
     * @param user the User Object
     */
    public void execute(User user) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "{call  USER_UTILITY.LOG_OUT(?) }";
            CallableStatement statement = connection.prepareCall(query);
            statement.setString(1, user.getEmail());
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
