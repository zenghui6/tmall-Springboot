package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.aspectj.weaver.ast.Or;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    static final String waitPay = "waitPay";            //待付款
    static final String waitDelivery = "waitDelivery";  // 待发货
    static final String waitConfirm = "waitConfirm";    //待确认
    static final String waitReview = "waitReview";      //待评价
    static final String finish = "finish";              //完成交易
    static final String delete = "delete";              //删除

    //分页显示
    PageNavigator<Order> list(int start, int size , int navigatePages);

    Order get(int oid);

    void update(Order bean);

    //清楚序列化后JSON循环调用.
    //注意：　为什么不使用＠JsonIgnoreProperties 来标记这个字段呢？这样就序列化时就会忽略该字段了啊
    //      后续我们要整合Redis,如果标记成了@JsonIgnoreProperties会在和Redis整合的时候有Bug!具体到redis的时候实验
    void removeOrderFormOrderItem(List<Order> orders);
    void removeOrderFormOrderItem(Order order);

    //为订单添加订单项
//    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
//    float add(Order order, List<OrderItem> ois);


    //添加订单信息
    void add(Order order);

    //订单计算总金
    void cacl(Order order);

    /**
     * 获取用户订单信息
     * @param user
     * @return
     */
    List<Order> listByUserWithoutDelete(User user);

    List<Order> listByUserAndNotDeleted(User user);

}
