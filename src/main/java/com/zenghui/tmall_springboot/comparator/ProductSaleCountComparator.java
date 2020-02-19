package com.zenghui.tmall_springboot.comparator;

import com.zenghui.tmall_springboot.entity.Product;

import java.util.Comparator;

/**
 * 销量比较器,销量高的放前面
 */
public class ProductSaleCountComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o2.getSaleCount()-o1.getSaleCount();
    }
}
