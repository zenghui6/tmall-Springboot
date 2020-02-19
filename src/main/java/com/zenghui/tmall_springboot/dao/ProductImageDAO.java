package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer> {
    //JPA中findByxxx时，需要传入　XXX 的实例作为参数
     List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);
}
