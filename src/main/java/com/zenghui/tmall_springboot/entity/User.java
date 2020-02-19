package com.zenghui.tmall_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;

    //盐，用于和shiro 结合的时候，加密用的
    @Column(name = "salt")
    private String salt;

    //anonymousName没有和数据库关联，用于获取匿名，其实就是前后保留，中间换成星星，如果长度只有2或者1，单独处理一下
    //就是后台显示的名字，通常都会隐藏一部分
    @Transient
    private String anonymousName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    //对名字的处理
    public String getAnonymousName() {
        if (anonymousName != null)
            return anonymousName;
        if (name == null)
            anonymousName=null;
        else if (name.length()<=1)
            anonymousName = "*";
        else if (name.length()==2)
            anonymousName = name.substring(0,1) + "*";
        else {//z****i 这样子
            char[] cs = name.toCharArray();
            for (int i = 1; i <cs.length-1 ; i++) {
                cs[i]='*';  //java中''代表char   ""代表String
            }
            anonymousName = new String(cs);
        }
        return anonymousName;
    }


    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", salt='" + salt + '\'' +
                ", anonymousName='" + anonymousName + '\'' +
                '}';
    }
}
