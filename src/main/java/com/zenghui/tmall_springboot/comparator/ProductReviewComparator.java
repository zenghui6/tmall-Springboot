package com.zenghui.tmall_springboot.comparator;

import com.zenghui.tmall_springboot.entity.Product;

import java.util.Comparator;

/**
 * 人气比较器,评论多的放前面
 */
public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getReviewCount()-o2.getReviewCount();
    }
}
