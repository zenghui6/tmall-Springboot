package com.zenghui.tmall_springboot.interceptor;

import com.zenghui.tmall_springboot.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//处理者拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
       // 完整的请求路径 = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        //                      协议                   主机名                    端口号                            应用名称
                //获得当前请求路径的应用名
        String contextPath = session.getServletContext().getContextPath();
        //需要认证页面组成的数组
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };

        String uri = httpServletRequest.getRequestURI();        //获取请求路径的URI
        uri = StringUtils.remove(uri,contextPath+"/");  //删除前面的应用名
        String page = uri;

        //判断uri是否是验证数组中的某个字符串开头
        if(begingWith(page,requireAuthPages)){
            //判断登录
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()){ //不在登录状态
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }
        return true;
    }

    /**
     * 判断uri是否是验证数组中的某个字符串开头
     * @param page
     * @param requiredAuthPages
     * @return
     */
    private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            //判断uri是否验证字符数组中的某个字符串开头
            if(StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
