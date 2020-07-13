package shopping.demo.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookiesUtil {
    public Cookie getByName(String name, HttpServletRequest request){
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name))
                return cookie;
        }
        return null;
    }
    public boolean isExist(String name, HttpServletRequest request){
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name))
                return true;
        }
        return false;
    }

    public Cookie remove(String name, HttpServletRequest request) {
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name)){
                cookie.setMaxAge(0);
                return cookie;
            }
        }
        return null;
    }
}
