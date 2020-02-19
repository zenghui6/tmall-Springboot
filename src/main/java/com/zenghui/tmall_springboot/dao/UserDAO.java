package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//crud
public interface UserDAO extends JpaRepository<User,Integer> {
    User findByName(String name);
    //登录：通过用户名和密码查询
    User findByNameAndPassword(String name,String password);
}
