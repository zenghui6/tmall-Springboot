package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product,Integer> {
    //jpa的按类名查找并分页
    Page<Product> findByCategory(Category category, Pageable pageable);
    //前台接收全部按类分类的全部product
    List<Product> findByCategoryOrderById(Category category);
    //名字关键字模糊查询
    List<Product> findByNameLike(String keyword,Pageable pageable);           //?分页并不是,只是排序方式

}
