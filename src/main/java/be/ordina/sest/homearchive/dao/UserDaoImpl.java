package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.UserEntity;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by sest on 25/11/14.
 */
@Log4j
public class UserDaoImpl implements UserDao {

    public static final String USER_COLLECTION = "users";

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public UserEntity findUser(String userName) {
        log.debug("Searching for user: " + userName);
        Query query = new Query(Criteria.where("_id").is(userName));
        return mongoTemplate.findOne(query, UserEntity.class, USER_COLLECTION);
    }

    @Override
    public boolean insertUser(UserEntity userEntity) {
        boolean isUserNotFound = true;
        if (findUser(userEntity.getUsername()) != null) {
            isUserNotFound = false;
        } else {
            mongoTemplate.insert(userEntity, USER_COLLECTION);
        }
        log.debug(String.format("User %s does not exists: %s", userEntity.getUsername(), isUserNotFound));
        return isUserNotFound;
    }

    @Override
    public void deleteUser(String userName) {
        log.debug("Deleting user: "+ userName);
        Query query = new Query(Criteria.where("_id").is(userName));
        mongoTemplate.remove(query, USER_COLLECTION);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        Update update = new Update();
        update.set("password", userEntity.getPassword());
        update.set("roles", userEntity.getRoles());
        Query query = new Query(Criteria.where("_id").is(userEntity.getUsername()));
        UserEntity foundUser = mongoTemplate.findAndModify(query, update, UserEntity.class, USER_COLLECTION);
        log.info("Updating user: " + foundUser);
    }
}
