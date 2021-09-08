package com.liang.test;

import java.sql.*;

public class TestJDBC2 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/jdbc?useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, username, password);

        String sql = "insert into user (id, name, password, email, birthday) values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 4);
        preparedStatement.setString(2, "杨亮");
        preparedStatement.setString(3, "123456");
        preparedStatement.setString(4, "111@qq.com");
        preparedStatement.setDate(5, new Date(new java.util.Date().getTime()));
        int i = preparedStatement.executeUpdate();
        if(i>0){
            System.out.println("插入成功");
        }
        preparedStatement.close();
        connection.close();
    }
}
