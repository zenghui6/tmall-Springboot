package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.ProductImageDAO;
import com.zenghui.tmall_springboot.entity.OrderItem;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.ProductImage;
import com.zenghui.tmall_springboot.service.ProductImageService;
import com.zenghui.tmall_springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageServiceImpl implements ProductImageService {

    public static final String type_single = "single";
    public static final String type_detail = "detail";

    @Autowired
    ProductImageDAO productImageDAO;

    @Autowired
    ProductService productService;

    @Override
    @CacheEvict(allEntries = true)
    public void add(ProductImage productImage) {
        productImageDAO.save(productImage);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productImageDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'productImages-one-'+#p0")
    public ProductImage get(int id) {
        return productImageDAO.findOne(id);
    }

    @Override
    @Cacheable(key = "'productImage-single-pid-'+#p0.id")
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product,type_single);
    }

    @Override
    @Cacheable(key = "'productImages-detail-pid-'+#p0.id")
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product,type_detail);
    }


    @Override
    public void setFirstProductImages(List<Product> products) {
        for (Product product : products)
            setFirstProductImage(product);
    }

    /**
     * 因为图片与商品外键关联,每次使用都要为该字段赋值
     *
     * @param ois
     */
    @Override
    public void setFirstProductImagesOnOrderItems(List<OrderItem> ois) {
        for (OrderItem orderItem : ois)
            setFirstProductImage(orderItem.getProduct());
    }

    @Override
    public void setFirstProductImage(Product product) {
        //得到单个页所有图片
        List<ProductImage> singleImages = listSingleProductImages(product);
        if (!singleImages.isEmpty()){
            product.setFirstProductImage(singleImages.get(0));  //列表取值，取第一张
        }else
            //考虑到产品还没来的及设置图片，但是在订单后台管理查看订单项对应的产品图片,有对象，只是对象为空．
            product.setFirstProductImage(new ProductImage());
    }
}
