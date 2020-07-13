package shopping.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shopping.demo.entities.User;
import shopping.demo.util.CookiesUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private CookiesUtil cookiesUtil;

    //spring security自定义登录页面的请求格式，get为跳转至自定义页面
    @GetMapping("/login")
    public String toIndex(){
        return "login";
    }

    @GetMapping("/error")
    public String toError(){
        return "error";
    }

    @GetMapping("/logout")
    public String toLogout(HttpServletRequest request,HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //退出后需要删除cookie(这里删除cookie方法是先获取然后设置存活时间为0然后再响应回去)
        response.addCookie(cookiesUtil.remove("username",request));
        return "index";
    }

    @GetMapping("/adminHome")
    public String toAdminHome(){
        return "adminHome";
    }

    @GetMapping("/userHome")
    public String toUserHome(){
        return "userHome";
    }

    //post方法是提交表单信息，交由spring security进行一个权限处理
    @PostMapping("/login")
    public String check(String username,
                        String password,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        logger.info("用户" + username + "登陆中....");
//        if(!password.equals(userService.findPassword(username))) {
//            model.addAttribute("message","用户名或者密码错误，请重新登陆");
//            return "login";
//        }


        //将token传入shiro
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if(!cookiesUtil.isExist(username,request)) {
                Cookie usernameCookie = new Cookie("username",username);
                response.addCookie(usernameCookie);
            }
            //此为数据库中查询到的对象
            User user = (User) subject.getPrincipal();
            Integer money = user.getMoney();
            model.addAttribute("username",username);
            model.addAttribute("money",money);
            return "admin".equals(user.getRole())?"adminHome":"userHome";
        }catch (UnknownAccountException e) {
            model.addAttribute("message","用户名不存在");
            return "login";
        }catch (IncorrectCredentialsException e) {
            model.addAttribute("message","权限不足");
            return "login";
        }
    }

}
