package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Review;

import java.util.List;

public interface ReviewService {
    /**
     * 列出product对应的评价
     * @param product
     * @return
     */
    List<Review> list(Product product);

    void add(Review review);

    /**
     * 获取product的评价数量
     * @param product
     * @return
     */
    int getCount(Product product);
}
