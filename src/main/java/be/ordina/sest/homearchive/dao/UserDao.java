package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.UserEntity;

/**
 * Defines CRUD operations with user
 * Created by sest on 25/11/14.
 */
public interface UserDao {

    UserEntity findUser(String userName);

    boolean insertUser(UserEntity userEntity);

    void deleteUser(String id);

    void updateUser(UserEntity userEntity);
}
