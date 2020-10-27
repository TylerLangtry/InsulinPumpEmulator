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

    // Connect to the database query
    private static Connection getRemoteConnection() {
        String databaseURL = "jdbc:mysql://insulin-pump-db.ccywbop2kswa.ap-southeast-2.rds.amazonaws.com:3306/insulinpumpdb";
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

    // Method to send the query requested
    private static ResultSet sendQuery(String sqlQuery) {
        ResultSet resultSet = null;
        Connection conn =  Utilities.getRemoteConnection();
        try {
            Statement statement = conn.createStatement();{
                // Execute a SELECT SQL statement.
                resultSet = statement.executeQuery(sqlQuery);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method to get tje configuration settings
    public static String[] getConfiguration() {
        String[] result = new String[5];
        // Query the latest configuration file made and send it back in an array of strings
        String query = "SELECT * FROM configuration ORDER BY config_id DESC LIMIT 1";
        ResultSet configurationFile = sendQuery(query);
        try {
            for (int i = 0; i < 5; i++) {
                result[i] = configurationFile.getString(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Method to insert to the data table
    public static void insertData(double sugarLevel, double injectAmount) {
        int data_id = getLatestID("data", "data_ID");
        String query = "INSERT INTO data (data_ID,blood_sug_lvl,collection_date,collection_time,inject_amnt) VALUES ("
                + data_id + "," + sugarLevel + ",now(),now()," + injectAmount + ")";
        sendQuery(query);
    }

    // Method to insert to the status table
    public static void insertStatus(int charge, int reserve, String alert) {
        String query;
        int status_id= getLatestID("status", "status_ID");
        if (alert == null) {
            query = "INSERT INTO status (status_id,battery_charge,reserves_amnt,status_time,status_date) VALUES ("
                    + status_id + "," + charge + "," + reserve + ",now(),now())";
        } else {
            query = "INSERT INTO status (status_id,battery_charge,reserves_amnt,alert,status_time,status_date) " +
                    "VALUES (" + status_id + "," + charge + "," + reserve + "," + alert + ",now(),now())";
        }
        sendQuery(query);
    }
    // Get the next ID for the PK
    private static int getLatestID(String table, String column) {
        String query = "SELECT " + column + " FROM " + table + " ORDER BY " + column + " DESC LIMIT 1";
        ResultSet id = sendQuery(query);
        Number result = 0;
        try {
            result = ((Number) id.getObject(1)).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.intValue();
    }
    // Custom Query for deletion or changing
    public static void customQuery(String query) {
        sendQuery(query);
    }

}
