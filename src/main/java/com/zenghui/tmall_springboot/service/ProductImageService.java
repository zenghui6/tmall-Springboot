package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    void add(ProductImage productImage);

    void delete(int id);

    ProductImage get(int id);

    /**
     * 根据product 查找到对应的简单图
     *
     * @param product
     * @return
     */
    List<ProductImage> listSingleProductImages(Product product);

    /**
     * 根据product　查找对应的详情图
     * @param product
     * @return
     */
    List<ProductImage> listDetailProductImages(Product product);

    //为product设置　首页图片，当然要传入对应的product对象
    void setFirstProductImage(Product product);

    void setFirstProductImages(List<Product> products);

    /**
     * 因为图片与商品外键关联,每次使用都要为该字段赋值
     * @param ois
     */
    void setFirstProductImagesOnOrderItems(List<OrderItem> ois);

}