package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService  {
     //不带分页导航的
     List<Category> list();
     //带分页导航的
     PageNavigator<Category> list(int start,int size,int navigatePages);

     //添加
     void add(Category bean);

     void delete(int id);

     //详情
     Category get(int id);

     //更新
     void update(Category bean);

     //清除Product中调Category 然后Category中又有products，又调用category的现象
     void removeCategoryFromProduct(List<Category> cs);

     void removeCategoryFromProduct(Category category);
}
