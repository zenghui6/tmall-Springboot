package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.CategoryDAO;
import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.service.CategoryService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//表示分类在缓存里的keys,都是归"categories"管理
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDAO categoryDAO;

    @Override
    @Cacheable(key="'categories-all'")
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll(sort);
    }

    @Override
    //这里保存的数据不再是一个对象,而是一个集合(保存在redis里是一个Json数组),例如: categories-page-0-5 就是第一页数据
    @Cacheable(key = "'categories-page-'+#p0+'-'+#p1")
    public PageNavigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page pageFormJPA = categoryDAO.findAll(pageable);


        return new PageNavigator<>(pageFormJPA,navigatePages);
    }

    @Override
    //@cachePut使用时会额外作为一个变量存储在内存中,取出来也没问题,但是分页数据中的数据没有刷新,就会出现数据不一致的现象
    //@CachePut(key = "'category-one-'+#p0")
    //使用CacheEvict会清除缓存,下次访问会从数据库中读取,再重新存入缓存,这样就牺牲了一点性能,保障了数据一致性
    @CacheEvict(allEntries = true)
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'categories-one-'+#p0")
    public Category get(int id) {
        Category c =categoryDAO.findOne(id);
        return c;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Category bean) {
        categoryDAO.save(bean);
    }

    @Override
    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category c :
                cs) {
            removeCategoryFromProduct(c);
        }
    }


    //消除循环嵌套，将product下的category字段赋空
    @Override
    public void removeCategoryFromProduct(Category category) {
        List<Product> products = category.getProducts();
        if (products!=null){
            for (Product product :
                    products) {
                product.setCategory(null);
            }
        }
    }
}
