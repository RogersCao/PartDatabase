package Database;

import Obj.Category;
import Obj.Part;
import Obj.Record;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class h2 {
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
        stmt.execute("CREATE TABLE IF NOT EXISTS Category (CategoryID uuid default random_uuid() PRIMARY KEY, Name VARCHAR(50) NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Customer (CustomerID uuid default random_uuid() PRIMARY KEY, Name VARCHAR(50) NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Part (PartID VARCHAR(50) NOT NULL PRIMARY KEY, ModelNum VARCHAR(50) NOT NULL, Name VARCHAR(50) NOT NULL, CategoryID uuid NOT NULL, Stock int NOT NULL, foreign key (CategoryID) references Category(CategoryID))");
        stmt.execute("CREATE TABLE IF NOT EXISTS Record (RecordID uuid default random_uuid() PRIMARY KEY, Date DATE NOT NULL, CustomerID uuid NOT NULL, PartID VARCHAR(50) NOT NULL, QuantityIN int NOT NULL, QuantityOUT int NOT NULL, CurrentStock int NOT NULL, Remark VARCHAR(300), foreign key (CustomerID) references Customer(CustomerID), foreign key (PartID) references Part(PartID))");
    }

    //insert
    public void insertCategory(String categoryName) throws Exception {
        String query = "INSERT INTO Category VALUES('" + UUID.randomUUID() + "', '" + categoryName + "')";
        stmt.executeUpdate(query);
    }

    public void insertCustomer(String customerName) throws Exception {
        String query = "INSERT INTO Customer VALUES('" + UUID.randomUUID() + "', '" + customerName + "')";
        stmt.executeUpdate(query);
    }

    public void insertPart(String PartID, String ModelNum, String Name, String categoryID, int stock) throws Exception {
        String query = "INSERT INTO Part VALUES('" + PartID + "', " + "'" + ModelNum + "', " + "'" + Name + "', " + "'" + categoryID + "', " + stock + ")";
        System.out.println(query);
        stmt.executeUpdate(query);
    }

    public void insertRecord(String Date, String CustomerID, String PartID, int QuaIN, int QuaOUT, int CurrentStock, String remark) throws Exception {
        String query = "INSERT INTO Record VALUES('" + UUID.randomUUID() + "', " + Date + ", " + "'" + CustomerID + "', " + "'" + PartID + "', " + QuaIN + ", " + QuaOUT + ", " + CurrentStock + ", " + "'" + remark + "'" + ")";
        System.out.println(query);
        stmt.executeUpdate(query);
    }

    //query
    public void queryCategory() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM Category");
        while (rs.next()) {
            System.out.println(rs.getString("CATEGORYID") + "," + rs.getString("Name"));
        }
    }// 查询数据

    public void queryCustomer() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
        while (rs.next()) {
            System.out.println(rs.getString("CUSTOMERID") + "," + rs.getString("Name"));
        }
    }// 查询数据

    public void queryPart() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM Part");
        while (rs.next()) {
            System.out.println(rs.getString("PartID") + "," + rs.getString("ModelNum")
                    + "," + rs.getString("Name") + "," + rs.getString("CategoryID")
                    + "," + rs.getString("Stock"));
        }
    }// 查询数据

    public void queryRecord() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM Record");
        while (rs.next()) {
            System.out.println(rs.getString("RECORDID") + "," + rs.getString("DATE") + ","
                    + rs.getString("CUSTOMERID") + "," + rs.getString("PARTID") + ","
                    + rs.getString("QUANTITYIN") + "," + rs.getString("QUANTITYOUT") + ","
                    + rs.getString("CURRENTSTOCK") + "," + rs.getString("REMARK"));
        }
    }// 查询数据


    public List<Category> queryCategoryList() throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();
        List<Category> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Category");
        while (rs.next()) {
            list.add(new Category(rs.getString("CATEGORYID"), rs.getString("Name")));
        }
        h2.close();
        return list;
    }// 查询CategoryList

    public List<Part> queryPartByCategory(String categoryID) throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();
        List<Part> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Part WHERE categoryID = '" + categoryID + "'");
        while (rs.next()) {
            list.add(new Part(rs.getString("PartID"), rs.getString("ModelNum"), rs.getString("Name"), rs.getString("CategoryID"), Integer.parseInt(rs.getString("Stock"))));
        }
        h2.close();
        return list;
    }// 查询Category中的Part

    public List<Record> queryDisplayRecord(String PartID) throws Exception {
        List<Record> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Record WHERE PartID = '" + PartID + "'");
        while (rs.next()) {
            list.add(new Record(rs.getString("RECORDID"), rs.getString("DATE"),
                    rs.getString("CUSTOMERID"), rs.getString("PARTID"),
                    Integer.parseInt(rs.getString("QUANTITYIN")),
                    Integer.parseInt(rs.getString("QUANTITYOUT")),
                    Integer.parseInt(rs.getString("CURRENTSTOCK")), rs.getString("REMARK")));
        }
        return list;
    }// 查询每一条record return record list

    public void close() throws Exception {
        stmt.close();
        conn.close();
    }// 释放资源 关闭连接
}