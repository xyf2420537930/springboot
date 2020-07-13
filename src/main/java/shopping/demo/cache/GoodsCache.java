package shopping.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopping.demo.entities.Goods;
import shopping.demo.entities.User;
import shopping.demo.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GoodsCache {
    @Autowired
    private RedisUtil redisUtil;

    public void setGoodsCache(Goods goods) {
        redisUtil.set("goods_"+goods.getId(),goods);
    }

    public Goods getGoodsCache(Integer id) {
        String key = "goods_" + id;
        if (redisUtil.isExist(key)) {
            return (Goods) redisUtil.get(key);
        }
        return null;
    }

    public boolean deleteGoodsCache(Integer id) {
        String key = "goods_"+id;
        return redisUtil.delete(key);
    }

    public List<Goods> getGoods() {
        Set<String> keys = redisUtil.Keys("goods_*");
        List<Goods> goods = new ArrayList<>();
        for(String key : keys) {
            goods.add((Goods) redisUtil.get(key));
        }
        return goods;
    }

    public List<Goods> getKills() {
        Set<String> keys = redisUtil.Keys("kill_*");
        List<Goods> goods = new ArrayList<>();
        String[] strings = new String[2];
        for(String key : keys) {
            strings = key.split("_");
            goods.add(getGoodsCache(Integer.parseInt(strings[1])));
            strings = null;
        }
        strings = null;
        return goods;
    }


    public void setKill(Integer id) {
        Goods goods = getGoodsCache(id);
        redisUtil.setKillGoods("kill_"+goods.getId(),goods.getCount().toString());
    }
}
