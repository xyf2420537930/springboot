package shopping.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shopping.demo.entities.Order;
import shopping.demo.entities.User;
import shopping.demo.service.GoodsService;
import shopping.demo.service.OrderService;
import shopping.demo.service.UserService;
import shopping.demo.util.CookiesUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(Logger.class);

    //返回的是所有用户的所有订单（针对管理员）
    @GetMapping("/orders")
    public String findAll(Model model) {
        model.addAttribute("orders",orderService.findAll());
        return "admin_orderList";
    }

    //修改订单状态为完成订单状态（针对管理员）
    @PatchMapping("/order")
    public String changeStatus(Integer id,String username) {
        //完成订单
        orderService.changeStatus(id,username);
        return "redirect:/orders";
    }

    //删除订单（针对管理员）
    @DeleteMapping("/order")
    public String deleteOrder(Integer id,String username) {
        orderService.deleteById(id,username);
        return "redirect:/orders";
    }

    //返回的是用户的所有订单（针对用户）
    @GetMapping("/order/username/{username}")
    public String findByUserName(@PathVariable("username") String username,
                                 Model model) {
        List<Order> orders = orderService.findByUserName(username);
        model.addAttribute("orders", orders);
        if(orders == null) {
            model.addAttribute("message","您还没买东西呢");
        }
        return "user_orderList";
    }

    //购买物品，对应创造订单，货物减少，余额减少（针对用户）
    @PostMapping("/toBuy")
    @ResponseBody
    public String createOrder(@RequestParam("username") String username,
                              @RequestParam("gid") Integer gid,
                              @RequestParam("cost") Integer cost) {
        if (userService.findMoney(username) >= cost) {

            orderService.createOrder(OrderService.order_id.incrementAndGet(),username, gid, cost);
            goodsService.buyIt(gid); //将商品的数量减少
            userService.moneyDown(username, cost); //用户的余额减少
            Integer left_money = userService.findMoney(username);
            return "购买成功！您的余额还剩余：" + left_money + "元";
        } else {
            return "余额不足，请尽快充值！";
        }
    }

}
