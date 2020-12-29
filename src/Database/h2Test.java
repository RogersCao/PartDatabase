package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class h2Test {
    // 数据库连接URL，当前连接的是C:/H2目录下的db数据库(h2数据存储有两种模式,一种是存储在硬盘上,一种是存储在内存中)
    private static final String JDBC_URL = "jdbc:h2:C:/H2/db"; //jdbc:h2:mem:数据库名称

    private static final String USER = "root";
    private static final String PASSWORD = "111000Cao";
    private static final String DRIVER_CLASS = "org.h2.Driver";

    private Connection conn;    // 全局数据库连接
    private Statement stmt;     // 数据库操作接口

    public void connection() throws Exception {
        Class.forName(DRIVER_CLASS);
        conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public void statement() throws Exception {
        stmt = conn.createStatement();// 创建操作
    }

    public void createTable() throws Exception {
        stmt.execute("DROP TABLE IF EXISTS TEST_H2TABLE");
        stmt.execute("CREATE TABLE TEST_H2TABLE(id VARCHAR(50) PRIMARY KEY,title VARCHAR(50))");
    }


    public void insertData() throws Exception {
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','TestLine1')");
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','头发在哪里')");
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','我的头发呢?')");
    }// 插入数据


    public void queryData() throws Exception {
        ResultSet rs = stmt.executeQuery("SHOW TABLES");
        System.out.println(rs);
        while (rs.next()) {
            System.out.println(rs.getString("id") + "," + rs.getString("title"));
        }
    }// 查询数据


    public void close() throws Exception {
        stmt.close();
        conn.close();
    }// 释放资源 关闭连接

    public static void main(String[] args) {
        h2Test h2 = new h2Test();
        try {
            h2.connection();
            h2.statement();
            h2.createTable();
            h2.insertData();
            h2.queryData();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("场面一度十分尴尬");
        } finally {
            try {
                h2.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("关都关不上了");
            }
        }
    }
}