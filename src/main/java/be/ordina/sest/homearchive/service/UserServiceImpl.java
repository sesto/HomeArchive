package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link be.ordina.sest.homearchive.service.UserService}
 * Created by sest on 25/11/14.
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * Loads user details by user name, used by Spring security
     * @param username user name
     * @return {@link org.springframework.security.core.userdetails.UserDetails}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.findUser(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
}
