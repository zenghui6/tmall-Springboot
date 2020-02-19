package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.entity.PropertyValue;
import com.zenghui.tmall_springboot.service.ProductService;
import com.zenghui.tmall_springboot.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包含查询所有的和修改功能
 */
@RestController
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    ProductService productService;

    //查询product下所有的属性
    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid){
        Product product = productService.get(pid);  //获取对应product

        propertyValueService.init(product); //这应该是在所有产品下创建所有种类属性．
        List<PropertyValue> propertyValues = propertyValueService.list(product);
        return propertyValues;
    }

    @PutMapping("/propertyValues")
    public PropertyValue update(@RequestBody PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return propertyValue;
    }
}
