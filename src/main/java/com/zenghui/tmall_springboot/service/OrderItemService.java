package com.zenghui.tmall_springboot.service;


import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;

/**
 * １，通过订单查找订单项
 * ２，通过产品查找订单项
 * 3,订单项的字段赋值，计算总量，添加对应product首页图等
 * ４，获取对应product的销量
 */
public interface OrderItemService {
    OrderItem get(int id);
    void add(OrderItem orderItem);
    //更新数据
    void update(OrderItem orderItem);

    void delete(OrderItem orderItem);
    /**
     * 通过订单查找订单项
     * @param order
     * @return
     */
    List<OrderItem> listByOrder(Order order);

    List<OrderItem> listByProduct(Product product);

    /**
     * 计算订单项中的数据,还为每组订单项　对应的　product添加首页图（非数据库字段，每次调用都要提前赋值）
     * @param orders
     */
    void fill(List<Order> orders);

    void fill(Order order);
    //获取销量
    int getSaleCount(Product product);
    //获取用户购物车信息
    List<OrderItem> ListByUser(User user);

}
