package shopping.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import shopping.demo.entities.Goods;

import java.util.List;

@Repository
@Mapper
public interface GoodsDao {
    @Select("select * from goods")
    public List<Goods> findAll();

    @Select("select * from goods where id=#{id}")
    public Goods findById(Integer id);

    @Select("select * from goods where `kill`=1")
    public List<Goods> findKilled();

    @Update("update goods set cost=#{cost} where id=#{id}")
    public void changePrice(Integer id, Integer cost);

    @Update("update goods set count=count-1 where id=#{id}")
    public void buyIt(Integer id);

    @Update("update goods set `kill`=1 where id=#{id}")
    public void setKilled(Integer id);

    @Delete("delete from goods where id=#{id}")
    public void deleteById(Integer id);

    @Insert("insert into goods values(#{id},#{name},#{count},#{cost},0)")
    public void insertGoods(Integer id, String name, Integer count, Integer cost);
}
