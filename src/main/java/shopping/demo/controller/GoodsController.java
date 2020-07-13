package shopping.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shopping.demo.entities.Goods;
import shopping.demo.entities.User;
import shopping.demo.service.GoodsService;
import java.util.List;
import java.util.Map;

/**
 * @author 24205
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @GetMapping("/goods")
    public String findAll(Map<String, List<Goods>> map) {
        List<Goods> goods = goodsService.FindAll();
        map.put("goods", goods);
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        String level = user.getRole();
        return level.equals("admin") ? "admin_goodsList" : "user_goodsList";
    }

    @PostMapping("/good")
    public String insertGoods(Goods goods) {
        goodsService.insertGoods(goods);
        return "redirect:/goods";
    }

    @DeleteMapping("/good")
    public String deleteById(Integer id) {
        goodsService.DeleteById(id);
        return "redirect:/goods";
    }

    @PatchMapping("/good")
    public String changePrice(Integer id,
                              Integer price) {
        goodsService.changePrice(id, price);
        return "redirect:/goods";
    }

}
