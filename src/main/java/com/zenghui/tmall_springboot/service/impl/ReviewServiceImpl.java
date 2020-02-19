package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.ReviewDAO;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.entity.Review;
import com.zenghui.tmall_springboot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewDAO reviewDAO;

    @Override
    @Cacheable(key = "'reviews-pid-'+#p0.id")
    public List<Review> list(Product product) {
        List<Review> reviews = reviewDAO.findByProductOrderByIdDesc(product);
        return reviews;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void add(Review review) {
        reviewDAO.save(review);
    }

    @Override
    @Cacheable(key = "'reviews-count-pid-'+#p0.id")
    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }
}
