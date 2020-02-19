package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.util.PageNavigator;

import java.util.List;

public interface PropertyService {
    void add(Property property);

    void delete(int id);

    Property get(int id);

    void update(Property property);

    //在业务上需要查询某个分类下的属性，所有list方法要带上对应的分类id--cid
    PageNavigator<Property> list(int cid,int start,int size, int navigatePages);

    //通过Category 获取下面的所有属性
    List<Property> listByCategory(Category category);

    //删除propertyValue中的product
}
