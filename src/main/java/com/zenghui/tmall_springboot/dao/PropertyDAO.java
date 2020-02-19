package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.PropertyEditor;
import java.util.List;


public interface PropertyDAO extends JpaRepository<Property, Integer> {
            //分类查询
 // JPA 是根据返回的类型自动判断是否分页，如果返回类型为Page,则返回的数据是带分页参数的集合（需要在最后面传入pageable），如果返回类型是List ,则返回的数据是list集合．
    Page<Property> findByCategory(Category category,Pageable pageable);

    //拿出单纯的数据进行处理
    List<Property> findByCategory(Category category);
}
