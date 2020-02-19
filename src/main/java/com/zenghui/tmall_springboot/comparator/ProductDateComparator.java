package com.zenghui.tmall_springboot.comparator;

import com.zenghui.tmall_springboot.entity.Product;

import java.util.Comparator;

/**
 * 新品比较器,日期近的放前面
 */
public class ProductDateComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getCreateDate().compareTo(o2.getCreateDate());
    }
}
