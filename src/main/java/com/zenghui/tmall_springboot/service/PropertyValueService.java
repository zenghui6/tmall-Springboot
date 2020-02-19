package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.entity.PropertyValue;

import java.util.List;

/**
 * 修改，查询和初始化
 * 属性值，因为只有　一个产品下一个属性值只能有一个，即产品＋属性　得到唯一属性值，所以这个值不能增加，只有修改
 * 所以需要通过初始化来自动的增加，以便后面的修改
 *
 */
public interface PropertyValueService{

    void update(PropertyValue propertyValue);

    void init(Product product);


    //根据产品查询属性值
    List<PropertyValue> list(Product product);

    //产品＋属性　－－＞ 属性值
    PropertyValue getByPropertyAndProduct(Product product, Property property);

}
