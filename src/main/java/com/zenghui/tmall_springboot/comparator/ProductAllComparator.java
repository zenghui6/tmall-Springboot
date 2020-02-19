package com.zenghui.tmall_springboot.comparator;

import com.zenghui.tmall_springboot.entity.Product;

import java.util.Comparator;
/**
 * 比较器,综合比较器,销量*评价
 */
public class ProductAllComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getSaleCount()*o1.getReviewCount()-o2.getSaleCount()*o2.getReviewCount();
    }
}
