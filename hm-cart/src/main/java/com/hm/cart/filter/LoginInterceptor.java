package com.hm.cart.filter;

import com.hm.auth.entity.UserInfo;
import com.hm.auth.utils.JwtUtils;
import com.hm.common.utils.CookieUtils;
import com.hm.cart.config.JwtProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties props;

    //创建线程域，存放登录用户的对象
    private static ThreadLocal<UserInfo> t1 = new ThreadLocal<>();
    public LoginInterceptor() {
    }

    public LoginInterceptor(JwtProperties props) {
        this.props = props;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        //1 先通过cookie查询token
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        //1.1判断token是否为空
        if (StringUtils.isBlank(token)) {
            //用户未登录,返回401，拦截
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            // 2 根据token与公钥解析用户信息
            UserInfo userInfo = JwtUtils.getUserInfo(props.getPublicKey(), token);
            // 2.1放入线程域中
            t1.set(userInfo);
            return true;

        }catch (Exception e){
            //抛出异常，未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            //3 如果能够解析成功返回true 并将用户信息存入到线程域，否则返回false
            return false;
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //过滤器完成后，从线程域中删除用户信息
       t1.remove();
    }

    /**
     * 获取登陆用户
     * @return
     */
    public static UserInfo getLoginUser() {
        return t1.get();
    }
}
