package com.digix.mvc.model.services.account;

import com.digix.mvc.model.entities.dao.User;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterService {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a User Object in DB
     *
     * @param user (email, password, firstname, lastname)
     * @return true if the user was registered successfully or false if email is already in use.
     */
    public boolean execute(User user) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "{call  USER_UTILITY.REGISTER(?, ?, ?, ?) }";
            CallableStatement statement = connection.prepareCall(query);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 20002) return false;
        }
        return true;
    }

    /**
     * Check is password fields are the same
     *
     * @param user (password, confirmedPassword)
     * @return true if passwords match or false otherwise
     */
    public boolean passwordMatch(User user) {
        return user.getPassword().equals(user.getConfirmedPassword());
    }
}
