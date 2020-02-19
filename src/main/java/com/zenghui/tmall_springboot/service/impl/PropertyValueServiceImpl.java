package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.PropertyValueDAO;
import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.entity.PropertyValue;
import com.zenghui.tmall_springboot.service.PropertyService;
import com.zenghui.tmall_springboot.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueServiceImpl implements PropertyValueService {
    @Autowired
    PropertyValueDAO propertyValueDAO;

    @Autowired
    PropertyService propertyService;

    @Override
    @CacheEvict(allEntries = true)
    public void update(PropertyValue propertyValue) {
        propertyValueDAO.save(propertyValue);
    }

    /**
     * 首先根据产品获取分类，然后获取这个分类下的所有属性集合
     * 然后结合产品id和属性id去遍历查询，看看这个产品的这个属性是否已经存在值了？
     * 如果不存在，就创建一个属性值，并设置其属性和产品，接着插入到数据库中．
     * @param product
     */
    @Override
    public void init(Product product) {
        Category c = product.getCategory();
        List<Property> properties = propertyService.listByCategory(c);

        System.out.println(properties);
        for (Property property : properties){
            PropertyValue propertyValue = propertyValueDAO.getByPropertyAndProduct(property,product);

            System.out.println(propertyValue);
            if (propertyValue == null){
                //如果该产品下的该属性没有值,(就是返回的对象为空)，那么就创建该对象，并与property 和　product 关联
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);

                //保存，写入数据库后就将　每个属性值与属性和产品关联了
                propertyValueDAO.save(propertyValue);
            }
        }

    }

    @Override
    @Cacheable(key = "'propertyValues-pid-'+#p0.id")
    public List<PropertyValue> list(Product product) {
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }

    @Override
    @Cacheable(key = "'propertyValues-one-pid-'+#p0.id+'-ptid-'+#p1.id")
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDAO.getByPropertyAndProduct(property,product);
    }
}
