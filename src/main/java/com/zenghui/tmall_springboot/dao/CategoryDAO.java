package com.zenghui.tmall_springboot.dao;

import com.zenghui.tmall_springboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
}
