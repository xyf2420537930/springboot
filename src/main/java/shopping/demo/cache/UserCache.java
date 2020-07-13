package shopping.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopping.demo.entities.User;
import shopping.demo.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserCache {
    @Autowired
    private RedisUtil redisUtil;

    public void setUserCache(User user) {
        redisUtil.set("user_"+user.getUsername(),user);
    }

    public User getUserCache(String username) {
        String key = "user_"+username;
        if (redisUtil.isExist(key)) {
            return (User)redisUtil.get(key);
        }
        return null;
    }

    public boolean deleteUserCache(String username) {
        String key = "user_"+username;
        return redisUtil.delete(key);
    }

    public List<User> getUsers() {
        Set<String> keys = redisUtil.Keys("user_*");
        List<User> users = new ArrayList<>();
        for(String key : keys) {
            users.add((User)redisUtil.get(key));
        }
        return users;
    }
}
