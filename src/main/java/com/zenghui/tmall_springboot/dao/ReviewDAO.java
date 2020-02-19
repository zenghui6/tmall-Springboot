package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 提供两个方法
 * 一个返回某产品对应的评价集合
 * 一个返回某产品对应的评价数量
 */
public interface ReviewDAO extends JpaRepository<Review,Integer> {
    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
