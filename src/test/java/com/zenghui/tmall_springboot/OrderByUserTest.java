package com.zenghui.tmall_springboot;

import com.zenghui.tmall_springboot.dao.OrderDAO;
import com.zenghui.tmall_springboot.dao.UserDAO;
import com.zenghui.tmall_springboot.entity.Order;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.OrderService;
import com.zenghui.tmall_springboot.service.UserService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TmallSpringbootApplication.class)
public class OrderByUserTest {
    @Autowired
    UserDAO userDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    OrderService orderService;

    @Test
    public void test(){
//        User user = userService.getByName("zenghui");
//        System.out.println(user.getId());
        System.out.println("111");
        User user = userDAO.findByName("zenghui");
        System.out.println(user);
//        List<Order> orders = orderDAO.findByUserAndStatusNotOrderByIdDesc(user,"delete");
//        PageNavigator<Order> pageNavigator = orderService.list(0,5,5);
//        System.out.println(pageNavigator);
       List<Order> orders = orderService.listByUserWithoutDelete(user);
        for (Order o : orders) {
            System.out.println(o);
        }
//        System.out.println(orders);
    }
}
