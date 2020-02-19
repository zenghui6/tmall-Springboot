package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.service.OrderItemService;
import com.zenghui.tmall_springboot.service.OrderService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import com.zenghui.tmall_springboot.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 提供订单分页查询和订单发货
 */
@RestController
public class OrderController {
    @Autowired
    OrderService  orderService;

    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/orders")
    public PageNavigator<Order> list(@RequestParam(value = "start",defaultValue = "0") int start,
                                     @RequestParam(value = "size",defaultValue = "5") int size){
        start = start<0?0:start;
        PageNavigator<Order> pageNavigator = orderService.list(start,size,5);

        //计算每个订单的数据
        orderItemService.fill(pageNavigator.getContent());
        //将循环调用部分赋值为空
        orderService.removeOrderFormOrderItem(pageNavigator.getContent());
        return pageNavigator;
    }

    //订单发货
    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable int oid){
        Order o = orderService.get(oid);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        //更新daos数据
        orderService.update(o);
        return Result.success();
    }
}
