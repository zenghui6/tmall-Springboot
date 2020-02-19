package com.zenghui.tmall_springboot.interceptor;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.CategoryService;
import com.zenghui.tmall_springboot.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;

    //在业务处理器处理请求之前被调用.预处理,可以进行编码,安全控制,权限校验等操作
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //注意,这个地方要将放回值设为true才能继续运行
        return true;
    }

    //在业务处理器处理请求完成后,生成视图之前执行.
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //将一些网站多个页面通用的数据直接放入session中,例如,首页的地址,购物车物品数量,
        //直接写入session就可以很方便的操作了
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        //购物车多少件商品
        int cartTotalItemNumber = 0;
        if (user != null){
            List<OrderItem> ois = orderItemService.ListByUser(user);
            cartTotalItemNumber = ois.size();
        }

        List<Category> cs = categoryService.list();
        String contextPath = httpServletRequest.getServletContext().getContextPath();

        httpServletRequest.getServletContext().setAttribute("categories_below_search",cs);
        session.setAttribute("cartTotalItemNumber",cartTotalItemNumber);
        httpServletRequest.getServletContext().setAttribute("contextPath",contextPath);

    }


    //在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
