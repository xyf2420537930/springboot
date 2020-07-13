package shopping.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shopping.demo.entities.Goods;
import shopping.demo.util.RedisUtil;


@Component
public class KillCache {
    @Autowired
    private RedisUtil redisUtil;
    public void setKillCache(Goods goods) {
        redisUtil.setKillGoods("kill_"+goods.getId(), goods.getCount().toString());
    }

}
