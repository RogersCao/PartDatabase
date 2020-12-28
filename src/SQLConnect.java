import java.sql.*;

public class SQLConnect {
    Connection conn;

    SQLConnect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/noxon?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "111000cao");

        query("select * from parts;");
        closeConnection();
    }

    public static void main(String[] args) throws Exception {
        new SQLConnect();
    }
    private void query(String queryLine) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet st = statement.executeQuery(queryLine);
        while (st.next()) {
            int id = st.getInt("id");
            String name = st.getString("name");
            String partId = st.getString("partId");
            System.out.println("sqlID: " + id + ", Name: " + name + ", partID: " + partId);
        }
        statement.close();
    }

    public boolean closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}