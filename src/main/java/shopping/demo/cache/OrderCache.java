package shopping.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopping.demo.entities.Order;
import shopping.demo.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class OrderCache {
    @Autowired
    private RedisUtil redisUtil;

    public void setOrderCache(Order order) {
        redisUtil.set("order_"+order.getId()+"_"+order.getUsername(),order);
    }

    public Order getCache(Integer id,String username) {
        String key = "order_"+id+"_"+username;
        return (Order)redisUtil.get(key);
    }

    public List<Order> getCacheByName(String username) {
        Set<String> keys = redisUtil.Keys("order_*_"+username);
        List<Order> orders = new ArrayList<>();
        for(String key : keys) {
            orders.add((Order)redisUtil.get(key));
        }
        return orders;
    }

    public void deleteOrderCache(Order order) {
        String key = "order_"+order.getId()+"_"+order.getUsername();
        redisUtil.delete(key);
    }

    public List<Order> getOrders() {
        Set<String> keys = redisUtil.Keys("order_*");
        List<Order> orders = new ArrayList<>();
        for(String key : keys) {
            orders.add((Order)redisUtil.get(key));
        }
        return orders;
    }
}
