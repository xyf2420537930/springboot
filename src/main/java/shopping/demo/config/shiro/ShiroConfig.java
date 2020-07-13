package shopping.demo.config.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shopping.demo.realm.UserRealm;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean factoryBean(@Qualifier("manager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(manager);
        factoryBean.setLoginUrl("/login");
        factoryBean.setUnauthorizedUrl("/error");
        Map<String,String> map = new HashMap<>();
        //静态资源不拦截
        map.put("/css/**","anon");
        map.put("/js/**","anon");
        map.put("/druid/**","anon");
        //对user请求拦截
        map.put("/users","roles[admin]");
        map.put("/user","roles[admin]");
        //对order请求拦截
        map.put("/orders","roles[admin]");
        map.put("/order","roles[admin]");
        map.put("/order/username/*","roles[user]");
        //对goods请求拦截
        map.put("/goods","authc");
        map.put("/good","roles[admin]");
        //对kill请求拦截
        map.put("/kills","authc");
        map.put("/killing","roles[user]");
        map.put("/kill","roles[admin]");
        //对admin主页拦截
        map.put("/adminHome","roles[admin]");
        //对user主页拦截
        map.put("/userHome","roles[user]");
        //主页放行，和其他一些操作进行
        map.put("/index","anon");
        map.put("/register","anon");
        map.put("/user/**","anon");
        map.put("/password","anon");
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }

    @Bean
    public DefaultWebSecurityManager manager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        return manager;
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
