package com.zenghui.tmall_springboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "productimage")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    @JsonBackReference      //双向关联注释，所谓的双向关联就是，产品里有分类，分类里有产品，产品里又有分类，分类里又有产品，无限循环了。
    private Product product;  // @JsonBackReference 这个仅仅表示对象转换成 json 数据的时候忽略了，但并不是所有时间都忽略了，平时还要用啊
    //product (one) 引用了　productImage (many) 在productImage中(many)也引用了product(one)

    @Column(name = "type")
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", product=" + product +
                ", type='" + type + '\'' +
                '}';
    }
}
