package shopping.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.demo.cache.UserCache;
import shopping.demo.dao.UserDao;
import shopping.demo.entities.User;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache userCache;
    private Logger logger = LoggerFactory.getLogger(Logger.class);

    //所有用户的预热进redis
    @PostConstruct
    public void init() {
        for(User user : userDao.findAll()) {
            userCache.setUserCache(user);
        }
        logger.info("User预热完毕");
    }

    public List<User> findAll() {
        List<User> users = userCache.getUsers();
        return users.size() == 0? userDao.findAll():users;
    }

    public String findPassword(String username) {
        User user = userCache.getUserCache(username);
        return user == null? userDao.findPassword(username):user.getPassword();
    }

    public User findByUsername(String username) {
        User user = userCache.getUserCache(username);
        return user == null?userDao.findByUsername(username):user;
    }

    public Integer findMoney(String username) {
        User user = userCache.getUserCache(username);
        return user == null? userDao.findMoney(username):user.getMoney();
    }

    public void deleteByUsername(String username) {
        userDao.deleteByUsername(username);
        userCache.deleteUserCache(username);
    }

    @Transactional
    public void changePassword(String username, String password) {
        userDao.changePassword(username, password);
        User user = userCache.getUserCache(username);
        user.setPassword(password);
        userCache.setUserCache(user);
    }

    @Transactional
    public void moneyDown(String username, Integer cost) {
        userDao.moneyDown(username, cost);
        logger.info("用户"+username+"花费了"+cost);
        User user = userCache.getUserCache(username);
        user.setMoney(user.getMoney()-cost);
        userCache.setUserCache(user);
    }

    public void moneyUp(String username) {
        userDao.moneyUp(username);
        logger.info("用户"+username+"充值了100元");
        User user = userCache.getUserCache(username);
        user.setMoney(user.getMoney()+100);
        userCache.setUserCache(user);
    }

    public void insertUser(String username, String password, Integer money) {
        userDao.insertUser(username, password, money);
        logger.info("创建了新用户"+username);
        User user = new User(username,password,money,"user");
        userCache.setUserCache(user);
    }

    public String getRole(String username) {
        User user = userCache.getUserCache(username);
        return user == null? userDao.getRole(username):user.getRole();
    }
}
