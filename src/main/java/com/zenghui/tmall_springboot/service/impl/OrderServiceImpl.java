package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.OrderDAO;
import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.OrderItemService;
import com.zenghui.tmall_springboot.service.OrderService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    OrderItemService orderItemService;

    @Override
    public PageNavigator<Order> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page pageFromJPA = orderDAO.findAll(pageable);
        return new PageNavigator<>(pageFromJPA,navigatePages);
    }

    @Override
    public Order get(int oid) {
        return orderDAO.findOne(oid);
    }

    @Override
    public void update(Order bean) {
        orderDAO.save(bean);
    }


    @Override
    public void removeOrderFormOrderItem(List<Order> orders) {
        for (Order order :
                orders) {
            removeOrderFormOrderItem(order);
        }
    }

    @Override
    public void removeOrderFormOrderItem(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem  : orderItems ) {
            orderItem.setOrder(null);
            orderItem.getProduct().getFirstProductImage().setProduct(null);
        }
    }


    @Override
    public void add(Order order) {
        orderDAO.save(order);
    }

    /**
     * 计算订单的总金额
     * @param order
     */
    @Override
    public void cacl(Order order) {
        List<OrderItem> ois = order.getOrderItems();
        float total = 0;
        for (OrderItem orderItem : ois) {
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        order.setTotal(total);
        System.out.println(ois);

    }

    /**
     * 获取用户订单信息
     *
     * @param user
     * @return
     */
    @Override
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    @Override
    public List<Order> listByUserAndNotDeleted(User user){
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user,OrderService.delete);
    }

    //    @Override
////    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
//    public float add(Order order, List<OrderItem> ois) {
//        float total = 0;
//        add(order);
//
//        for (OrderItem oi : ois){
//            oi.setOrder(order);
//            orderItemService.update(oi);
//            total += oi.getProduct().getPromotePrice()*oi.getNumber();
//        }
//        return total;
//    }
}
