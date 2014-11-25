package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.User;

/**
 * Defines CRUD operations with user
 * Created by sest on 25/11/14.
 */
public interface UserDao {

    User findUser(String userName);

    boolean insertUser(User user);

    void deleteUser(String id);

    void updateUser(User user);
}
