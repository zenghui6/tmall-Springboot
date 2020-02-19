package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.OrderItemDAO;
import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.OrderItemService;
import com.zenghui.tmall_springboot.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemDAO orderItemDAO;
    @Autowired
    ProductImageService productImageService;

    @Override
    public OrderItem get(int id) {
        return orderItemDAO.getOne(id);
    }

    @Override
    public void add(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @Override
    public void delete(OrderItem orderItem) {
        orderItemDAO.delete(orderItem);
    }

    @Override
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

    @Override
    public void fill(List<Order> orders) {
        for (Order order: orders)
            fill(order);
    }

    @Override
    public void fill(Order order) {
        //找到该订单对应的所有订单项,然后将它赋值给Order实例
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : orderItems){
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
            //
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    /**
     *  获取销量
     *   1,首先通过产品获取产品所有订单项
     *   2,将每个订单项的产品数相加　得到　销量
     * @param product
     * @return int
     */
    @Override
    public int getSaleCount(Product product) {
       List<OrderItem> orderItems = listByProduct(product); //1
        int result =0;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getOrder()!=null && orderItem.getOrder().getPayDate()!=null)
                result +=  orderItem.getNumber();
        }
        return result;
    }

    /**
     * 获取用户购物车信息
     * @param user
     * @return
     */
    @Override
    public List<OrderItem> ListByUser(User user) {
        return orderItemDAO.findByUserAndAndOrderIsNull(user);
    }

    @Override
    public List<OrderItem> listByProduct(Product product){
        return orderItemDAO.findByProduct(product);
    }

}
