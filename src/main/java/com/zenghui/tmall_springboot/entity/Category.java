package com.zenghui.tmall_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    //注意，注意：　@Transient注解的字段，是没有存入数据库的，即使你为它赋值了，也没办法存入数据库
    //              只是代表着　一个实例会有该字段，但都是默认为空，
    //              每次需要使用的时候都必须要先为该字段赋值．

    //一个分类对应多个product
    @Transient
    private List<Product> products;

    //一个分类对应多行产品，而每行产品又有多个产品
    @Transient
    private List<List<Product>> productsByRow;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<List<Product>> getproductsByRow() {
        return productsByRow;
    }

    public void setproductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
