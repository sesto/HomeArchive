package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by sest on 25/11/14.
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.findUser(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
}
