package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {
    //通过订单，找到对应的订单项
    List<OrderItem> findByOrderOrderByIdDesc(Order  order);

    /**
     *  获取销量
     *   1,首先通过产品获取产品所有订单项
     *   2,将每个订单项的产品数相加　得到　销量
     * @param product
     * @return List<OrderItem>
     */
    List<OrderItem> findByProduct(Product product);

    //当订单order为空时,就是还没有创建订单,说明处于购物车状态

    /**
     * 获取用户的购物车信息
     * @param user
     * @return List<OrderItem>
     */
    List<OrderItem> findByUserAndAndOrderIsNull(User user);
}
