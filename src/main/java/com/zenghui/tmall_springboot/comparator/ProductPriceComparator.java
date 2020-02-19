package com.zenghui.tmall_springboot.comparator;

import com.zenghui.tmall_springboot.entity.Product;

import java.util.Comparator;

/**
 * 价格比较器,价格低的放前面
 */
public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return (int)(o1.getPromotePrice()-o2.getPromotePrice());
    }
}
