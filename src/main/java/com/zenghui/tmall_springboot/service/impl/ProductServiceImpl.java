package com.zenghui.tmall_springboot.service.impl;

import com.zenghui.tmall_springboot.dao.CategoryDAO;
import com.zenghui.tmall_springboot.dao.ProductDAO;
import com.zenghui.tmall_springboot.entity.Category;
import com.zenghui.tmall_springboot.entity.Product;
import com.zenghui.tmall_springboot.service.*;
import com.zenghui.tmall_springboot.util.PageNavigator;
import com.zenghui.tmall_springboot.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "products")
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDAO productDAO;
    //这里使用CategoryService 而不使用　CategoryDao,其实功能都能实现，
    //我想，应该是不直接去操作DAO,使用业务的逻辑
    @Autowired
    ProductImageService productImageService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;

    @Override
    @CacheEvict(allEntries = true)
    public void add(Product product) {
        productDAO.save(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'products-one-'+#p0")
    public Product get(int id) {
        return productDAO.findOne(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Product product) {
        productDAO.save(product);
    }

    @Override
    @Cacheable(key = "'products-cid-'+#p0+'-page-'+#p1+'-'+#p2")
    public PageNavigator<Product> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        System.out.println(category);
        Sort sort = new Sort(Sort.Direction.DESC,"id");

        Pageable pageable = new PageRequest(start,size,sort);

        Page<Product> pageFormJPA = productDAO.findByCategory(category,pageable);
        //工具分页类根据page对象 和　navigatePages　产生分页对象
        PageNavigator<Product> pageNavigator = new PageNavigator<>(pageFormJPA,navigatePages);
        return pageNavigator;
    }

    @Override
    @Cacheable(key = "'products-cid-'+#p0.id")
    public List<Product> listByCategory(Category category) {
        return productDAO.findByCategoryOrderById(category);
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category category :
                categories) {
            fill(category);
        }
    }

    /**
     * 查出对应category 的所有　product　　　
     * 将所有product　当写入　category 的products字段
     * @param category
     */
    @Override
    public void fill(Category category) {
        /**
         *如果某个方法调用 被 缓存管理的 方法;例如:fill调用listByCategory方法,而listByCategory被缓存机制管理了
         *不能直接调用,因为springboot的缓存机制是通过切面编程aop来实现的
         *如果直接调用 aop 是拦截不到的,不会走缓存了,所以需要诱发aop
         */
        ProductService productService = SpringContextUtil.getBean(ProductService.class);
        List<Product> products = productService.listByCategory(category);

        //为products首页图片赋值,该赋值是查询赋值，不会将该字段写入数据库
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    /**
     *  //分类导航，右边的部分
     *  {
     * //   {1,2,3,4},
     * //   {5,6,7,8},
     * //  }
     * @param categories
     */

    @Override
    public void fillByRow(List<Category> categories) {
        int productNumberEachRow = 8;
        //遍历category，得到category　对应的product
        for (Category category :
              categories) {
            List<Product> products = category.getProducts();

            //将得到的product,建立成为类似二维数组的Product列表
//            {
//                {1,2,3,4},
//                {5,6,7,8},
//            }
            List<List<Product>> productsByRow = new ArrayList<>();
//            System.out.println(products.size());
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                //对products的list的列表进行切片操作：python叫切片，java截取其中的字符串吧．．．
                int end = i+productNumberEachRow;   //截取列表的底部
                end = end>products.size() ? products.size():end;

                List<Product>  productsOfEachRow = products.subList(i,end);

                //将没行加入List<List<Product>> productsByRow中
                productsByRow.add(productsOfEachRow);
            }
            category.setproductsByRow(productsByRow);
        }
    }

    /**
     * 为订单的销量和评论数赋值
     * @param products
     */
    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    @Override
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        List<Product> products = productDAO.findByNameLike("%"+keyword+"%",pageable);
        for (Product product: products ) {
            System.out.println(product);
        }
        return products;
    }


}
