package shopping.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import shopping.demo.entities.Order;

import java.util.List;

@Repository
@Mapper
public interface OrderDao {
    @Select("select * from `order`")
    public List<Order> findAll();

    @Select("select * from `order` where id=#{id}")
    public Order findById(Integer id);

    @Select("select * from `order` where username=#{username}")
    public List<Order> findByUserName(String username);

    @Select("select * from `order` where gid=#{gid}")
    public List<Order> findByGId(Integer gid);

    @Select("select * from `order` where time=#{time}")
    public Order findByTime(String time);

    @Update("update`order` set status='finished' where id=#{id}")
    public void changeStatus(Integer id);

    @Delete("delete from `order` where id=#{id}")
    public void deleteById(Integer id);

    @Insert("insert into `order`(id,time,username,gid,price,status) values(#{id},#{time},#{username},#{gid},#{price},#{status})")
    public void createOrder(Integer id,String time, String username, Integer gid, Integer price,String status);
}
