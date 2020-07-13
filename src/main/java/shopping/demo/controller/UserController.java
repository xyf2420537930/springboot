package shopping.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shopping.demo.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
//    @Autowired
//    private SecurityUtil securityUtil;
    private Logger logger = LoggerFactory.getLogger(Logger.class);

    //获取单个用户的信息，此处用于判断创建新用户时候用户名是否在数据库中已经存在
    @GetMapping("/user/{username}")
    @ResponseBody
    public String findByUsername(@PathVariable String username){
        logger.info("验证用户名中....");
        if(userService.findByUsername(username) == null) {
            return "none";
        } else {
            return "exist";
        }
    }

    //获取所有用户信息
    @GetMapping("/users")
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    //删除用户
    @DeleteMapping("/user")
    public String deleteByUsername(String username) {
        userService.deleteByUsername(username);
        return "redirect:/users";
    }

    //用于添加新用户
    @PostMapping("/register")
    public String insertUser(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("money") Integer money,
                             Model model) {
        userService.insertUser(username, password, money);
        model.addAttribute("message","注册成功");
        logger.info("添加了一位新用户"+username);
        return "login";
    }

    //用来验证两次输入密码是否一致
    @PostMapping("/password")
    @ResponseBody
    public String getPassword(String username,
                              String oldPwd){
        String pwd = userService.findPassword(username);
        logger.info("checking.....");
        if (!pwd.equals(oldPwd)) {
            return "no";
        }else {
            return "ok";
        }
    }

    //用来修改密码
    @PatchMapping("/password")
    public String changePassword(@RequestParam("username") String username,
                                 @RequestParam("newPassword_twice") String twice,
                                 Model model) {
        userService.changePassword(username,twice);
        model.addAttribute("message","修改密码成功");
        logger.info("用户" + username + "更改了密码");
        return "login";
    }

    //增加用余额
    @PatchMapping("/moneyUp")
    public String changeMoney(Model model,
                              String username) {
        userService.moneyUp(username);
        model.addAttribute("username",username);
        model.addAttribute("money",userService.findMoney(username));
        return "userHome";
    }
}
