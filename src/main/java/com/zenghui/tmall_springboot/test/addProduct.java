package com.zenghui.tmall_springboot.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC测试
 */
public class addProduct {
    public static void main(String[] args) {
        //准备分类测试数据：
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmall_springboot?useUnicode=true&characterEncoding=utf8","root","a1210128434");
            Statement s = c.createStatement();

            for (int i = 301; i <= 400; i++) {
                String  sqlFormat = "REPLACE into product(id,cid,name,subTitle,originalPrice,promotePrice,stock,createDate) values (%d,%d,'产品%d','产品标题%d','%f','%f','%d','2019-11-28')";
                int round = (int)(1+Math.random()*(10-1+1));
                String sql = String.format(sqlFormat,i,i/4,i,i,10.4,8.6,round);
                s.execute(sql);
            }
            System.out.println("已经成功创建１０0条产品（product）数据");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
