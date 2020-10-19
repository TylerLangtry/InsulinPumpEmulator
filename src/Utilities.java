import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

public class Utilities {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Connection getRemoteConnection() {
        String databaseURL = "jdbc:mysql://insulin-pump-db.ccywbop2kswa.ap-southeast-2.rds.amazonaws.com:3306/";
        String user = "master";
        String password = "Master1234";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                java.lang.System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            java.lang.System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
       }
        return conn;
    }

    public static void sendQuery(String sqlQuery) {
        ResultSet resultSet = null;
        Connection conn =  Utilities.getRemoteConnection();
        try {
            Statement statement = conn.createStatement();{
                // Execute a SELECT SQL statement.
                resultSet = statement.executeQuery(sqlQuery);
                // Print results from select statement
                while (resultSet.next()) {
                    java.lang.System.out.println(resultSet.getString(1));
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
