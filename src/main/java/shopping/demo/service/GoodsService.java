package shopping.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.demo.cache.GoodsCache;
import shopping.demo.dao.GoodsDao;
import shopping.demo.entities.Goods;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsCache goodsCache;

    private Logger logger = LoggerFactory.getLogger(GoodsService.class);
    @PostConstruct
    public void init() {
        for(Goods goods:goodsDao.findAll()) {
            goodsCache.setGoodsCache(goods);
        }
        logger.info("Goods预热完毕");
    }

    public List<Goods> FindAll() {
        List<Goods> goods = goodsCache.getGoods();
        return goods.size() == 0? goodsDao.findAll():goods;
    }

    public List<Goods> FindKilled() {
        List<Goods> kills = goodsCache.getKills();
        return kills.size() == 0?goodsDao.findKilled():kills;
    }

    public Goods FindById(Integer id) {
        Goods goods = goodsCache.getGoodsCache(id);
        if(goods == null) {
            //缓存未命中后，需要从数据库中进行查询并缓存入redis
            Goods new_goods = goodsDao.findById(id);
            goodsCache.setGoodsCache(new_goods);
            return new_goods;
        }
        return goods;
    }

    @Transactional
    public void DeleteById(Integer id) {
        //对应删除缓存
        goodsDao.deleteById(id);
        goodsCache.deleteGoodsCache(id);
    }

    @Transactional
    public void changePrice(Integer id, Integer cost) {
        goodsDao.changePrice(id, cost);
        //数据库更新完毕后，需要缓存也要进行更新
        Goods goods = goodsCache.getGoodsCache(id);
        goods.setCost(cost);
        goodsCache.setGoodsCache(goods);
    }

    @Transactional
    public void buyIt(Integer id) {
        goodsDao.buyIt(id);
        //数据库更新完毕后，需要缓存也要进行更新（将货物count进行-1操作）
        Goods goods = goodsCache.getGoodsCache(id);
        goods.setCount(goods.getCount()-1);
        goodsCache.setGoodsCache(goods);
    }

    @Transactional
    public void setKilled(Integer id) {
        //数据库中将Kill字段变为1
        goodsDao.setKilled(id);
        //将缓存中的goods对象的kill属性也变为1
        Goods goods = goodsCache.getGoodsCache(id);
        goods.setKill(1);
        goodsCache.setGoodsCache(goods);
        //在缓存层中进行一个加载
        goodsCache.setKill(id);
    }

    @Transactional
    public void insertGoods(Goods goods) {
        goodsDao.insertGoods(goods.getId(),goods.getName(), goods.getCount(), goods.getCost());
        //缓存层加载
        goodsCache.setGoodsCache(goods);
    }
}
