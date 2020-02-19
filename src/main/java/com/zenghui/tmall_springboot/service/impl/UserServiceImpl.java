package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.UserDAO;
import com.zenghui.tmall_springboot.entity.User;
import com.zenghui.tmall_springboot.service.UserService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDAO;

    @Override
    @Cacheable(key = "'user-page-'+#p0+'-'+#p1")
    public PageNavigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);

        Page pageFromJPA = userDAO.findAll(pageable);
        PageNavigator<User> pageNavigator = new PageNavigator<>(pageFromJPA,navigatePages);
        return pageNavigator;
    }

    @Override
    public boolean isExist(String name) {
        User user  = getByName(name);
        return user!=null;
    }

    @Override
    @Cacheable(key = "'users-one-name-'+#p0")
    public User getByName(String name) {
        return userDAO.findByName(name);    //find　如果找到就返回对象，没找到就返回空　，get 没找到会报错
    }

    @Override
    @CacheEvict(allEntries = true)
    public void add(User user) {
        userDAO.save(user);
    }

}
