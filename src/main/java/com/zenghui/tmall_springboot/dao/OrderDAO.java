package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order,Integer> {
    //获取用户的订单,即状态不是"delete"的订单(待付款,待发货....)
     List<Order>  findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
