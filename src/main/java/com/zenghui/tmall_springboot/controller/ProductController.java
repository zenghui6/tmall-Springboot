package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.service.CategoryService;
import com.zenghui.tmall_springboot.service.ProductImageService;
import com.zenghui.tmall_springboot.service.ProductService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;

    @PostMapping("/products")
    public Product add(@RequestBody Product product){
        product.setCreateDate(new Date()); //后端自动生成时间，前端没有传时间数据过来
        productService.add(product);
        return product;
    }

    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") int id){
        productService.delete(id);
        return null;
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product){
        productService.update(product);
        return product;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id){
        Product product= productService.get(id);
        return product;
    }

    //按category 分类查找
    @GetMapping("/categories/{cid}/products")
    public PageNavigator<Product> list(@PathVariable("cid") int cid,@RequestParam(value = "start",defaultValue = "0") int start,@RequestParam(value = "size",defaultValue = "5") int size){
        start = start<0?0:start;
        int NAVIGATEPAGE= 7;//分页导航栏页数．

        PageNavigator<Product> page = productService.list(cid,start,size,NAVIGATEPAGE);

        //page.getContent()得的是　product对象，包含firstProductImage.
        // 这个时候还没有赋值，传入到productImageServiceImpl中完成对该字段的赋值,取所有图片的第一张；
        productImageService.setFirstProductImages(page.getContent());

        return page;
    }
}
