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
        stmt.execute("CREATE TABLE IF NOT EXISTS Category (CategoryID uuid default random_uuid() PRIMARY KEY, Name VARCHAR(50) NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Customer (CustomerID uuid default random_uuid() PRIMARY KEY, Name VARCHAR(50) NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Part (PartID VARCHAR(50) NOT NULL PRIMARY KEY, ModelNum VARCHAR(50) NOT NULL, Name VARCHAR(50) NOT NULL, CategoryID uuid NOT NULL, Stock int NOT NULL, foreign key (CategoryID) references Category(CategoryID))");
        stmt.execute("CREATE TABLE IF NOT EXISTS Record (RecordID uuid default random_uuid() PRIMARY KEY, Date DATE NOT NULL, CustomerID uuid NOT NULL, PartID VARCHAR(50) NOT NULL, QuantityIN int NOT NULL, QuantityOUT int NOT NULL, CurrentStock int NOT NULL, Remark VARCHAR(300), foreign key (CustomerID) references Customer(CustomerID), foreign key (PartID) references Part(PartID))");
    }


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
            System.out.println(rs.getString("RECORDID") + "," + rs.getString("DATE")+ ","
                    + rs.getString("CUSTOMERID")+ "," + rs.getString("PARTID")+ ","
                    + rs.getString("QUANTITYIN")+ "," + rs.getString("QUANTITYOUT")+ ","
                    + rs.getString("CURRENTSTOCK")+ "," + rs.getString("REMARK"));
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

//            h2.insertCategory("Bearing");
//            h2.insertCustomer("cao");
//            h2.insertPart("LA0131", "6222-2Z/C3", "Ball Bearing", "f8718f66-abd2-44bf-9cc6-400557d71cb8", 10);
//            h2.insertRecord("DATE '2004-12-31'", "fe0cf13f-44cc-4855-be39-724a85692e73", "LA0131", 0, 5, 5, "");

            h2.queryCategory();
            h2.queryCustomer();
            h2.queryPart();
            h2.queryRecord();

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