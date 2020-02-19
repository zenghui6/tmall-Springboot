package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.util.PageNavigator;

/**
 * 只提供查询操作
 * １，增加用户，应该由前台注册功能实现
 * ２，修改用户信息，也应该由用户自己完成,前台功能
 * ３，删除用户，用户信息是最重要的资料，暂不提供
 */
public interface UserService {
    PageNavigator<User> list (int start,int size,int NavigatePages);

    //注册时判断是否重名
    boolean isExist(String name);
    //通过名字获取user对象
    User getByName(String name);
    //注册添加到数据库
    void add(User user);
}
