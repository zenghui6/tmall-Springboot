package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.entity.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {
    //业务上需要：１,根据产品查询属性值（有多个属性值）
    //          2,根据产品和属性　确定唯一的属性值；
    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    PropertyValue getByPropertyAndProduct(Property property,Product product);

}
