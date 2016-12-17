package com.digix.mvc.model.services.account;

import com.digix.mvc.model.entities.dao.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogInService implements AuthenticationProvider {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Performs authentication with the same contract as AuthenticationManager.authenticate(Authentication).
     *
     * @param authentication the authentication request object.
     * @return fully authenticated object including credentials. May return null if
     * the AuthenticationProvider is unable to support authentication of the passed
     * Authentication object. In such a case, the next AuthenticationProvider that supports
     * the presented Authentication class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        /* prepare user object */
        User user = new User();
        user.setEmail(authentication.getName());
        user.setPassword(authentication.getCredentials().toString());

        /* check if user object is valid*/
        if (this.isValidUser(user)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), grantedAuths);
            return auth;
        } else {
            return null;
        }
    }

    /**
     * Returns true if this AuthenticationProvider supports the indicated Authentication object.
     *
     * @param authentication the authentication request object.
     * @return true if the implementation can more closely evaluate the Authentication class presented
     */
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    /**
     * Checks if a user with a specific email and password is in DB.
     *
     * @param user the User Object
     * @return returns true if user is found in DB, false otherwise
     */
    private boolean isValidUser(User user) {

        try (Connection connection = getDataSource().getConnection()) {
            String query = "{call USER_UTILITY.LOG_IN(?, ?) }";
            CallableStatement statement = connection.prepareCall(query);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) return false;
        }
        return true;
    }

}
