package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.User;

/**
 * Defines CRUD operations with user
 * Created by sest on 25/11/14.
 * @author sest
 */
public interface UserDao {
    /**
     * searches user by username
     * @param userName username
     * @return {@link be.ordina.sest.homearchive.model.User}
     */
    User findUser(String userName);

    /**
     * Inserts user in db
     * @param user {@link be.ordina.sest.homearchive.model.User}
     *
     */
    void insertUser(User user);

    /**
     * deletes user
     * @param username username
     */
    void deleteUser(String username);

    /**
     * updates user info
     * @param user {@link be.ordina.sest.homearchive.model.User} updated user
     */
    void updateUser(User user);
}
