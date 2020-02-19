package com.zenghui.tmall_springboot.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC测试
 */
public class addProperty {
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

            for (int i = 401; i <= 500; i++) {
                String  sqlFormat = "insert into property(id,cid,name) values (%d,%d,'属性%d')";
                String sql = String.format(sqlFormat,i,i/46,i);
                s.execute(sql);
            }
            System.out.println("已经成功创建１０0条属性（property）数据");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
