package shopping.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import shopping.demo.entities.User;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    @Select("select * from user")
    public List<User> findAll();

    @Select("select password from user where username=#{username}")
    public String findPassword(String username);

    @Select("select money from user where username=#{username}")
    public Integer findMoney(String username);

    @Select("select * from user where username=#{username}")
    public User findByUsername(String username);

    @Select("select role from user where username=#{username}")
    public String getRole(String username);

    @Update("update user set password=#{password} where username=#{username}")
    public void changePassword(String username, String password);

    @Update("update user set money=money-#{cost} where username=#{username}")
    public void moneyDown(String username, Integer cost);

    @Update({"update user set money=money+100 where username=#{username}"})
    public void moneyUp(String username);

    @Delete("delete from user where username=#{username}")
    public void deleteByUsername(String username);

    @Insert("insert into user values(#{username},#{password},#{money},'user'")
    public void insertUser(String username, String password, Integer money);
}
