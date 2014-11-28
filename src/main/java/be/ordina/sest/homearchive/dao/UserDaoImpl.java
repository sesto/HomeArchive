package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * implementation of {@link be.ordina.sest.homearchive.dao.UserDao}
 * Created by sest on 25/11/14.
 */
@Component
@Log4j
public class UserDaoImpl implements UserDao {

    public static final String USER_COLLECTION = "users";


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    private void initDefaultUsers() {
        log.debug("Inserting default users");
        User user;
        //add admin
        user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.getRoles().add("ROLE_ADMIN");
        user.getRoles().add("ROLE_USER");
        insertUser(user);
        //add user
        user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.getRoles().add("ROLE_USER");
        insertUser(user);
    }

    @Override
    public User findUser(String userName) {
        log.debug("Searching for user: " + userName);
        Query query = new Query(Criteria.where("_id").is(userName));
        return mongoTemplate.findOne(query, User.class, USER_COLLECTION);
    }

    @Override
    public void insertUser(User user) {
        mongoTemplate.insert(user, USER_COLLECTION);
    }


    @Override
    public void deleteUser(String userName) {
        log.debug("Deleting user: " + userName);
        Query query = new Query(Criteria.where("_id").is(userName));
        mongoTemplate.remove(query, USER_COLLECTION);
    }

    @Override
    public void updateUser(User user) {
        Update update = new Update();
        update.set("password", user.getPassword());
        update.set("roles", user.getRoles());
        Query query = new Query(Criteria.where("_id").is(user.getUsername()));
        User foundUser = mongoTemplate.findAndModify(query, update, User.class, USER_COLLECTION);
        log.info("Updating user: " + foundUser);
    }
}
