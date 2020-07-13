package shopping.demo.util;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import shopping.demo.entities.Goods;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//    private Logger log = LoggerFactory.getLogger(RedisUtil.class);

    public void set(@NotNull String key, @NotNull Object value) {
        redisTemplate.opsForValue().set(key,value);
//        log.info("redis设置了key:"+key+",value:"+value);
    }

    public Object get(@NotNull String key) {
        Object value = redisTemplate.opsForValue().get(key);
//        log.info("取出的value:"+value);
        return value;
    }

    public void setKillGoods(String key,String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public Object getKillGoods(String key) {
       return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean delete(@NonNull String key) {
        return redisTemplate.delete(key);
    }

    public boolean isExist(@NotNull String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<String> Keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    public Long increment(String key) {
//        ValueOperations<String, String> operations = redisTemplate.opsForValue();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return operations.increment(key);
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Long decrement(String key){
//        ValueOperations<String, String> operations = redisTemplate.opsForValue();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return operations.decrement(key);
        return stringRedisTemplate.opsForValue().decrement(key);
    }

}
