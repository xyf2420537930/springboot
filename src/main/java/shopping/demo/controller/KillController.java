package shopping.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shopping.demo.cache.KillCache;
import shopping.demo.entities.KillDetail;
import shopping.demo.service.GoodsService;
import shopping.demo.service.OrderService;
import shopping.demo.service.UserService;
import shopping.demo.util.ByteUtil;
import shopping.demo.util.RedisUtil;


@Controller
public class KillController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //测试使用部分
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KillCache killCache;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    //测试使用部分

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    @GetMapping("/kills")
    public String findKilled(Model model) {
        model.addAttribute("kill", goodsService.FindKilled());
        return "kill";
    }

    @PatchMapping("/kill")
    public String setKilled(Integer id) {
        goodsService.setKilled(id);
        return "redirect:/goods";
    }

    @PostMapping("/killing")
    @ResponseBody
    public String createKillOrder(@RequestParam("username") String username,
                                  @RequestParam("gid") Integer gid,
                                  @RequestParam("cost") Integer cost) {
//        if(!redisUtil.kill(username, gid, cost))
//            logger.info("购买失败");
//        return "redirect:/order/"+username;
        String topic = "fei";
        KillDetail killDetail = new KillDetail(username,gid,cost);
        kafkaTemplate.send(topic, ByteUtil.bean2Byte(killDetail));
        logger.info("用户"+username+"已经发送信息到fei队列中");
        return "已确认秒杀，请稍后......";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        Long count = redisUtil.decrement("kill_1");
        if (count >= 0) {
            orderService.createOrder(OrderService.order_id.incrementAndGet(),"nnk", 1, 1);
            goodsService.buyIt(1); //将商品的数量减少
            userService.moneyDown("nnk",1);
            logger.info("------秒杀成功，剩余库存" + count);
            return "ok";
        } else {
            redisUtil.increment("kill_1");
            logger.info("失败");
            return "false";
        }
    }
}
