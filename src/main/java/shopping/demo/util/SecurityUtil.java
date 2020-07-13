//package shopping.demo.util;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class SecurityUtil {
//    public void addUser(String username, String password) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        password = new BCryptPasswordEncoder().encode(password);
//        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username,password,authorities);
//    }
//
//    public void addAdmin(String username, String password) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        password = new BCryptPasswordEncoder().encode(password);
//        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username,password,authorities);
//    }
//}
