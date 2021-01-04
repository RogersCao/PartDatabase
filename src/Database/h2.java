package Database;

import Obj.Category;
import Obj.Customer;
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
    private static final String JDBC_URL = "jdbc:h2:./db"; //jdbc:h2:mem:数据库名称
//    private static final String JDBC_URL = "jdbc:h2:C:/H2/db"; //jdbc:h2:mem:数据库名称

    private static final String USER = "root";
    private static final String PASSWORD = "111000Cao";
    private static final String DRIVER_CLASS = "org.h2.Driver";

    private Connection conn;    // 全局数据库连接
    private Statement stmt;     // 数据库操作接口

    public void connection() throws Exception {
        Class.forName(DRIVER_CLASS);
        conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        System.out.println("connect");
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
        stmt.executeUpdate(query);
    }

    //query
    public void queryPart(String condition, String value) throws Exception {
        System.out.println("SELECT * FROM Part WHERE UPPER(" + condition + ") LIKE UPPER('%" + value + "%')");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Part WHERE UPPER(" + condition + ") LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            System.out.println(rs.getString("PartID") + "," + rs.getString("ModelNum")
                    + "," + rs.getString("Name") + "," + rs.getString("CategoryID")
                    + "," + rs.getString("Stock"));
        }
    }// 查询part

    public List<Category> queryCategory(String value) throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();
        List<Category> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Category WHERE UPPER(NAME) LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            list.add(new Category(rs.getString("CATEGORYID"), rs.getString("Name")));
        }
        h2.close();
        return list;
    }// 查询category

    public List<Customer> queryCustomer(String value) throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();
        List<Customer> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE UPPER(NAME) LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            list.add(new Customer(rs.getString("CUSTOMERID"), rs.getString("NAME")));
        }
        h2.close();
        return list;
    }// 查询customer

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
    }//查询CategoryList (all) (也是update的关键 生成category就可以生成下面的所有信息)

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

    public List<Customer> queryCustomerList() throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();
        List<Customer> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
        while (rs.next()) {
            list.add(new Customer(rs.getString("CUSTOMERID"), rs.getString("Name")));
        }
        h2.close();
        return list;
    }// 查询CategoryList

    public void close() throws Exception {
        stmt.close();
        conn.close();
    }// 释放资源 关闭连接

//    public static void main(String[] args) {
//        h2 h2 = new h2();
//        try {
//            h2.connection();
//            h2.statement();
////            h2.createTable();
//            h2.queryCategory();
////            h2.insertRecord("DATE '2020-1-11'","9e35fcf1-4bd7-47f0-af32-f870c6f18afd","LA0154",0,2,8,"");
////            h2.insertRecord("DATE '2020-1-11'","566d353d-4508-41ed-80b1-9d33e17647fd","LA0154",0,2,6,"");
//
////            h2.queryCategoryList();
////            h2.queryCustomer();
////            h2.queryPart();
////            h2.queryRecord();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("场面一度十分尴尬");
//        } finally {
//            try {
//                h2.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("关都关不上了");
//            }
//        }
//    }
}