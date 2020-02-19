package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.UserService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public PageNavigator<User> list(@RequestParam(value = "start" ,defaultValue = "0") int start,
                                    @RequestParam(value = "size",defaultValue = "7") int size){
        start = start<0 ? 0 :start;
        PageNavigator<User>  page = userService.list(start,size,7);
        return page;
    }
}
