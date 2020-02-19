package com.zenghui.tmall_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
//Elasticsearch 索引注解@Document
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //外键
    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;

    @Column(name = "name")
    private String name;
    @Column(name = "subTitle")
    private String subTitle;
    @Column(name = "originalPrice")
    private float originalPrice;
    @Column(name = "promotePrice")      //促销价
    private float promotePrice;
    @Column(name = "stock")
    private int stock; //库存
    @Column(name = "createDate")
    private Date createDate;

    //注意，注意：　@Transient注解的字段，是没有存入数据库的，即使你为它赋值了，也没办法存入数据库
    //              只是代表着　一个实例会有该字段，但都是默认为空，
    //              每次需要使用的时候都必须要先为该字段赋值．

    @Transient      //表示数据库中没这个字段就忽略
    //图片首页显示．
    private ProductImage firstProductImage;

    /**
    前台商品详情页需要的属性字段
     **/
    //商品的单页面的集合
    @Transient
    private List<ProductImage> productSingleImages;

    //商品的详情图片集合
    @Transient
    private List<ProductImage> productDetailImages;

    //销量
    @Transient
    private int saleCount;

    //累计评价数量
    @Transient
    private int reviewCount;

    public List<ProductImage> getProductSingleImages() {
        return productSingleImages;
    }

    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public List<ProductImage> getProductDetailImages() {
        return productDetailImages;
    }

    public void setProductDetailImages(List<ProductImage> productDetailImages) {
        this.productDetailImages = productDetailImages;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public ProductImage getFirstProductImage() {
        return firstProductImage;
    }

    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(float promotePrice) {
        this.promotePrice = promotePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", originalPrice=" + originalPrice +
                ", promotePrice=" + promotePrice +
                ", stock=" + stock +
                ", createDate=" + createDate +
                ", firstProductImage=" + firstProductImage +
                ", productSingleImages=" + productSingleImages +
                ", productDetailImages=" + productDetailImages +
                ", saleCount=" + saleCount +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
