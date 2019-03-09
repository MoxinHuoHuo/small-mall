package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther lyd
 * @createDate 2019/3/9 16:53
 */
@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".happymmall.com";
    private final static String COOKIE_NAME="mmall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");

        ck.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookieName:{}, cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for (Cookie cookie : cks) {
                log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                if(StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    log.info("return cookieName:{}，cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks) {
                ck.setDomain(COOKIE_DOMAIN);
                ck.setPath("/");
                // 0代表cookie的生存时间为零，即删除cookie
                ck.setMaxAge(0);
                response.addCookie(ck);
                return ;
            }
        }
    }
}
