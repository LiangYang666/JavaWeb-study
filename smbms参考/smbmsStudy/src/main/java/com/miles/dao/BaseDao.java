package com.miles.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 操作数据库的公共类
public class BaseDao {

    private static String driver;

    private static String url;

    private static String username;

    private static String password;

    // 静态代码块,在类加载的时候执行
    // 初始化连接参数,从配置文件里获得
    static {
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("user");
        password = properties.getProperty("password");
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 数据库查询公共类
     * 
     * @param connection
     * @param sql
     * @param params
     * @param resultSet
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet,
        String sql, Object[] params) throws SQLException {

        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }

        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    /**
     * 增删改公共类
     * 
     * @param connection
     * @param sql
     * @param params
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    public static int execute(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params)
        throws SQLException {

        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }

        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    /**
     * 关闭资源
     * 
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @return
     */
    public static boolean closeResources(Connection connection, PreparedStatement preparedStatement,
        ResultSet resultSet) {

        boolean flag = true;

        if (resultSet != null) {
            try {
                resultSet.close();
                // GC回收
                resultSet = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                // GC回收
                preparedStatement = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }

        if (connection != null) {
            try {
                connection.close();
                // GC回收
                connection = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        return true;
    }
}
