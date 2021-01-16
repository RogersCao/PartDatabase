package Database;

import Obj.Category;
import Obj.Customer;
import Obj.Part;
import Obj.Record;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class h2 {
    // 数据库连接URL，当前连接的是C:/H2目录下的db数据库(h2数据存储有两种模式,一种是存储在硬盘上,一种是存储在内存中)
    private static final String JDBC_URL = "jdbc:h2:./db"; //jdbc:h2:mem:数据库名称
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

    public void insertPart(String PartID, String ModelNum, String Name, String categoryID) throws Exception {
        String query = "INSERT INTO Part VALUES('" + PartID + "', " + "'" + ModelNum + "', " + "'" + Name + "', " + "'" + categoryID + "', 0)";
        System.out.println(query);
        stmt.executeUpdate(query);
    }

    public void insertRecord(String Date, String CustomerID, String PartID, int QuaIN, int QuaOUT, int CurrentStock, String remark) throws Exception {
        String query = "INSERT INTO Record VALUES('" + UUID.randomUUID() + "', " + Date + ", " + "'" + CustomerID + "', " + "'" + PartID + "', " + QuaIN + ", " + QuaOUT + ", " + CurrentStock + ", " + "'" + remark + "'" + ")";
        stmt.executeUpdate(query);
    }

    //query
    public List<Category> queryCategory(String value) throws Exception {
        List<Category> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Category WHERE UPPER(NAME) LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            list.add(new Category(rs.getString("CATEGORYID"), rs.getString("Name")));
        }
        return list;
    }// 查询category 已完成

    public List<Customer> queryCustomer(String value) throws Exception {
        List<Customer> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE UPPER(NAME) LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            list.add(new Customer(rs.getString("CUSTOMERID"), rs.getString("NAME")));
        }
        return list;
    }// 查询customer reserve

    public void queryPart(String condition, String value) throws Exception {
        System.out.println("SELECT * FROM Part WHERE UPPER(" + condition + ") LIKE UPPER('%" + value + "%')");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Part WHERE UPPER(" + condition + ") LIKE UPPER('%" + value + "%')");
        while (rs.next()) {
            System.out.println(rs.getString("PartID") + "," + rs.getString("ModelNum")
                    + "," + rs.getString("Name") + "," + rs.getString("CategoryID")
                    + "," + rs.getString("Stock"));
        }
    }// 查询part reserve

    public int queryStockQuantity(String partID) {
        try {
            String query = "SELECT * FROM Record WHERE PARTID = " + "'" + partID + "'";
            ResultSet rs = stmt.executeQuery(query);
            int stock;
            rs.last();
            stock = Integer.parseInt(rs.getString("CURRENTSTOCK"));
            System.out.println(stock);
            return stock;
        } catch (SQLException e) {
//            if(e.getSQLState().startsWith("02")){
            return 0;
//            }
        }

    }// 查询数量 已完成

    //update part
    public void updatePartID(String value) throws Exception {
        ResultSet rs = stmt.executeQuery("");
        while (rs.next()) {

        }
    }// updatePartID WIP

    public void updatePartName(String id, String value) throws Exception {
        stmt.executeUpdate("UPDATE Part SET NAME = '" + value + "' WHERE PARTID = '" + id + "'");
    }// updatePartName done

    public void updatePartModelNUM(String id, String value) throws Exception {
        stmt.executeUpdate("UPDATE Part SET MODELNUM  = '" + value + "' WHERE PARTID = '" + id + "'");
    }// updatePartModelNUM done

    //update part
    public void updateRecordDate(String id, String value) throws Exception {
        stmt.executeUpdate("UPDATE Record SET Date  = '" + value + "' WHERE RECORDID   = '" + id + "'");
    }// updateRecordDate WIP

    public String updateRecordCustomerGetID(String value) throws Exception {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE NAME = '" + value + "'");
            String customerID = null;
            rs.last();
            customerID = rs.getString("CUSTOMERID");
            System.out.println("!" + customerID);
            return customerID;
        } catch (SQLException e) {
            return null;
        }
    }

    public void updateRecordCustomer(String id, String value) throws Exception {
        stmt.executeUpdate("UPDATE Record SET CUSTOMERID = '" + value + "' WHERE RECORDID   = '" + id + "'");
    }// updatePartCustomer WIP

    public void updateRecordQtyIn(String id, String value) throws Exception {
        List<Record> recordList = new ArrayList<>();
        ResultSet partIDRS = stmt.executeQuery("SELECT * FROM Record WHERE RECORDID = '" + id + "'");
        partIDRS.last();
        String partID = partIDRS.getString("PARTID");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Record WHERE PARTID = '" + partID + "'");
        while (rs.next()) {
            recordList.add(new Record(rs.getString("RECORDID"), rs.getString("DATE"),
                    rs.getString("CUSTOMERID"), rs.getString("PARTID"),
                    Integer.parseInt(rs.getString("QUANTITYIN")), Integer.parseInt(rs.getString("QUANTITYOUT")),
                    Integer.parseInt(rs.getString("CURRENTSTOCK")), rs.getString("REMARK")));
        }
        boolean start = false;
        boolean after = false;
        int currentStock = 0;
        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).RecordID.equals(id)) {
                start = true;
            }
            if (after) {
                currentStock = currentStock + recordList.get(i).QuantityIN - recordList.get(i).QuantityOUT;
                stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
            }
            if (start) {
                if (i >= 1) {
                    currentStock = recordList.get(i - 1).CurrentStock + Integer.parseInt(value) - recordList.get(i).QuantityOUT;
                    stmt.executeUpdate("UPDATE Record SET QUANTITYIN = " + value + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    after = true;
                    start = false;
                } else if (i == 0) {
                    currentStock = Integer.parseInt(value);
                    stmt.executeUpdate("UPDATE Record SET QUANTITYIN = " + value + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    after = true;
                    start = false;
                }
            }
        }
    }// updatePartQtyIn DONE

    public void updateRecordQtyOut(String id, String value) throws Exception {
        List<Record> recordList = new ArrayList<>();
        ResultSet partIDRS = stmt.executeQuery("SELECT * FROM Record WHERE RECORDID = '" + id + "'");
        partIDRS.last();
        String partID = partIDRS.getString("PARTID");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Record WHERE PARTID = '" + partID + "'");
        while (rs.next()) {
            recordList.add(new Record(rs.getString("RECORDID"), rs.getString("DATE"),
                    rs.getString("CUSTOMERID"), rs.getString("PARTID"),
                    Integer.parseInt(rs.getString("QUANTITYIN")), Integer.parseInt(rs.getString("QUANTITYOUT")),
                    Integer.parseInt(rs.getString("CURRENTSTOCK")), rs.getString("REMARK")));
        }
        boolean start = false;
        boolean after = false;
        int currentStock = 0;
        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).RecordID.equals(id)) {
                start = true;
            }
            if (after) {
                currentStock = currentStock + recordList.get(i).QuantityIN - recordList.get(i).QuantityOUT;
                stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
            }
            if (start) {
                if (i >= 1) {
                    currentStock = recordList.get(i - 1).CurrentStock + recordList.get(i).QuantityIN - Integer.parseInt(value);
                    stmt.executeUpdate("UPDATE Record SET QUANTITYOUT = " + value + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    after = true;
                    start = false;
                } else if (i == 0) {
                    currentStock = Integer.parseInt(value);
                    stmt.executeUpdate("UPDATE Record SET QUANTITYOUT = " + value + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    stmt.executeUpdate("UPDATE Record SET CURRENTSTOCK  = " + currentStock + " WHERE RECORDID   = '" + recordList.get(i).RecordID + "'");
                    after = true;
                    start = false;
                }
            }
        }
    }// updatePartQtyOut WIP

    public void updateRecordRemarks(String id, String value) throws Exception {
        stmt.executeUpdate("UPDATE Record SET REMARK = '" + value + "' WHERE RECORDID   = '" + id + "'");
    }// updatePartRemarks WIP


    //不要动，这些是生成全部数据使用的----------------------------------------------------------------------------------------
    public List<Category> queryCategoryList() throws Exception {
        List<Category> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Category");
        while (rs.next()) {
            list.add(new Category(rs.getString("CATEGORYID"), rs.getString("Name")));
        }
        return list;
    }//查询CategoryList (all) (也是update的关键 生成category就可以生成下面的所有信息)

    public List<Part> queryPartByCategory(String categoryID) throws Exception {
        List<Part> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Part WHERE categoryID = '" + categoryID + "'");
        while (rs.next()) {
            list.add(new Part(rs.getString("PartID"), rs.getString("ModelNum"), rs.getString("Name"), rs.getString("CategoryID"), Integer.parseInt(rs.getString("Stock"))));
        }
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
        List<Customer> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
        while (rs.next()) {
            list.add(new Customer(rs.getString("CUSTOMERID"), rs.getString("Name")));
        }
        return list;
    }// 查询CategoryList
    //------------------------------------------------------------------------------------------------------------------

    public void close() throws Exception {
        stmt.close();
        conn.close();
    }// 释放资源 关闭连接

    public static void main(String[] args) throws Exception {
//        h2 h2 = new h2();//h2 db connection
//        try {
//            h2.connection();
//            h2.statement();
//            h2.createTable();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("场面一度十分尴尬");
//        }
//        int a = h2.queryStockQuantity("fe0cf13f-44cc-4855-be39-724a85692e73", "LA0131");
//        System.out.println(a);

    }
}