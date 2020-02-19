package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.CategoryDAO;
import com.zenghui.tmall_springboot.dao.PropertyDAO;
import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.service.PropertyService;
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

import java.util.List;

@Service
@CacheConfig(cacheNames = "properties")
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    PropertyDAO propertyDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Override
    @CacheEvict(allEntries = true)
    public void add(Property property) {
        propertyDAO.save(property);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        propertyDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'properties-one-'+#p0")
    public Property get(int id) {
        //findOne：当查询一个不存在的id数据时，返回的是null
        //getOne:当查询一个不存在的id时，会直接抛出异常，因为他返回的是一个引用，简单点说就是一个代理对象．
        return propertyDAO.findOne(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Property property) {
        propertyDAO.save(property);
    }

    @Override
    @Cacheable(key = "'properties-cid-' +#p0+ '-page-' +#p1+ '-' + #p2")
    public PageNavigator<Property> list(int cid, int start, int size, int navigatePages) {
        //传入cid　是为了找到对应的category,然后JPA findByCategory,找到对应分类查询并排序，分页．
        Category category = categoryDAO.findOne(cid);

        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);

        Page<Property> pageFormJPA = propertyDAO.findByCategory(category,pageable);

        //分页工具，传入page查询结果和　导航栏长度
        return new PageNavigator<>(pageFormJPA,navigatePages);
    }

    @Override
    @Cacheable(key = "'properties-cid-'+#p0.id")
    public List<Property> listByCategory(Category category) {
        return propertyDAO.findByCategory(category);
    }
}
