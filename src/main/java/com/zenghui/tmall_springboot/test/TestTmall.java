package com.zenghui.tmall_springboot.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC测试
 */
public class TestTmall {
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

            for (int i = 0; i < 100; i++) {
                String  sqlFormat = "insert into category values (null,'测试分类%d')";
                String sql = String.format(sqlFormat,i);
                s.execute(sql);
            }
            System.out.println("已经成功创建１０0条分类测试数据");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
