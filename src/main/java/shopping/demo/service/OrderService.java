package shopping.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.demo.cache.OrderCache;
import shopping.demo.dao.OrderDao;
import shopping.demo.entities.Order;
import shopping.demo.util.RedisUtil;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCache orderCache;

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    //作为order订单的Id（如果使用自增主键的话那么就无法将新增订单存入缓存，因为不知道的唯一主键Id）
    public volatile static AtomicInteger order_id = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        for(Order order:orderDao.findAll()) {
            orderCache.setOrderCache(order);
        }
        logger.info("Order预热完毕");
    }

    public List<Order> findAll() {
        List<Order> orders = orderCache.getOrders();
        return orders.size() == 0?orderDao.findAll():orders;
    }

//    public Order findById(Integer id) {
//        Order order = orderCache.getCacheById(id);
//        if(order == null) {
//            Order new_order = orderDao.findById(id);
//            orderCache.setOrderCache(new_order);
//            return new_order;
//        }
//        return order;
//    }

    public List<Order> findByUserName(String username) {
        List<Order> orders = orderCache.getCacheByName(username);
        return orders.size() == 0?orderDao.findByUserName(username):orders;
    }

    @Transactional
    public void deleteById(Integer id,String username) {
        orderDao.deleteById(id);
        Order order = orderCache.getCache(id,username);
        orderCache.deleteOrderCache(order);
        logger.info("删除了订单，订单号为："+order.getId());
    }

    @Transactional
    public void changeStatus(Integer id,String username) {
        orderDao.changeStatus(id);
        Order order = orderCache.getCache(id,username);
        order.setStatus("finished");
        orderCache.setOrderCache(order);
    }

    @Transactional
    public void createOrder(Integer id,String username, Integer gid, Integer price) {
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        orderDao.createOrder(id,time, username, gid, price,"doing");
        //订单我们数据库采取的是自增主键，所以我们需要进行一次查询查找到对应Order，然后存入缓存中
        Order order = new Order(id,time,username,gid,price,"doing");
        orderCache.setOrderCache(order);
    }


}