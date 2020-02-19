package com.zenghui.tmall_springboot.service;

import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    void add(Product product);
    void delete(int id);
    Product get(int id);
    void update(Product product);

    //根据category分类查询

    /**
     *
     * @param cid     根据cid分类查询需要
     * @param start     sort , start, size　三个参数为pageable 必须的三个参数
     * @param size
     * @param navigatePages     分页导航栏的长度
     * @return
     */
    PageNavigator<Product> list(int cid,int start, int size, int navigatePages);

    //查询某个分类下的所有产品
    List<Product> listByCategory(Category category);

    //为分类填充产品集合
    void fill(List<Category> categories);

    void fill(Category category);

    //将产品列表分成类似二维数组的形式，显示，每行有８个
    void fillByRow(List<Category> categories);

    //为销量和评价数量赋值
    void setSaleAndReviewNumber(List<Product> products);
    void setSaleAndReviewNumber(Product product);

    //搜索
    List<Product> search(String keyword,int start,int size);
}
