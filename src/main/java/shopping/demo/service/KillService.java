package shopping.demo.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import shopping.demo.cache.KillCache;
import shopping.demo.entities.Goods;
import shopping.demo.entities.KillDetail;
import shopping.demo.util.ByteUtil;
import shopping.demo.util.RedisUtil;

import javax.annotation.PostConstruct;

@Service
public class KillService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KillCache killCache;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(KillService.class);
    //在项目的开始对已设置为秒杀的商品进行一个redis预热
    @PostConstruct
    public void killInit(){
        for (Goods goods : goodsService.FindKilled()) {
           killCache.setKillCache(goods);
        }
        logger.info("Kill预热完毕");
    }

    //作为kafka的consumer对fei通道进行监听
    @KafkaListener(topics = "fei")
    public void kill(ConsumerRecord<String,Object> consumerRecord) {
        logger.info("接收到kafka信息");
        byte[] bytes = (byte[]) consumerRecord.value();
        KillDetail killDetail = (KillDetail) ByteUtil.byte2Obj(bytes);
        Long count = redisUtil.decrement("kill_"+killDetail.getGid());
        if (count >= 0) {
            orderService.createOrder(OrderService.order_id.incrementAndGet(),killDetail.getUsername(), killDetail.getGid(), killDetail.getCost());
            goodsService.buyIt(killDetail.getGid()); //将商品的数量减少
            userService.moneyDown(killDetail.getUsername(),killDetail.getCost());
            logger.info("用户"+killDetail.getUsername()+"秒杀成功，剩余库存" + count);
        } else {
           redisUtil.increment("kill_"+killDetail.getGid());
            logger.info("用户"+killDetail.getUsername()+"秒杀失败");
        }
    }
}
