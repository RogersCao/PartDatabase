import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnect {
    Connection conn;
    SQLConnect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/noxon?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "111000cao");

//        Statement statement = conn.createStatement();
//        ResultSet st = statement.executeQuery("select * from parts;");
//        while (st.next()) {
//            int id = st.getInt("id");
//            String name = st.getString("name");
//            String partId = st.getString("partId");
//            System.out.println("sqlID: " + id + ", Name: " + name + ", partID: " + partId);
//        }
//        statement.close();
    }

    public boolean closeConnection (){
        try {
            conn.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}