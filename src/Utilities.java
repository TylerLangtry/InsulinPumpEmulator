import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utilities {
    private Connection conn = null;
    private Statement statement = null;
    ResultSet resultSet = null;

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Connect to the database query
    private void getRemoteConnection() {
        String databaseURL = "jdbc:mysql://insulin-pump-db.ccywbop2kswa.ap-southeast-2.rds.amazonaws.com:3306/insulinpumpdb";
        String user = "master";
        String password = "Master1234";
        try {
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                java.lang.System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            java.lang.System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
       }
    }

    public void closeConnection() {
        try { resultSet.close(); } catch (Exception e) { /* ignored */ }
        try { statement.close(); } catch (Exception e) { /* ignored */ }
        try { conn.close(); } catch (Exception e) { /* ignored */ }
        java.lang.System.out.println("Closed connection to the database");
    }

    // Method to send the query requested
    private void sendQuery(String sqlQuery) {
        getRemoteConnection();
        try {
            statement = conn.createStatement();{
                // Execute a SELECT SQL statement.
                resultSet = statement.executeQuery(sqlQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to send the update requested
    private int sendUpdate(String sqlQuery) {
        int result = 0;
        getRemoteConnection();
        try {
            statement = conn.createStatement();{
                // Execute a SELECT SQL statement.
                result = statement.executeUpdate(sqlQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Method to get the configuration settings
    private String[] getConfiguration() {
        String[] result = new String[6];
        // Query the latest configuration file made and send it back in an array of strings
        String query = "SELECT * FROM configuration ORDER BY configuration_id DESC LIMIT 1";
        sendQuery(query);
        try {
            if(resultSet.next()) {
                for (int i = 1; i < 7; i++) {
                        result[i-1] = resultSet.getString(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateConfig() {
        String[] config = getConfiguration();
        Config.MEASUREMENT_INTERVAL = (int)Double.parseDouble(config[2]);
        Config.MAX_INJECTION = Double.parseDouble(config[3]);
        Config.MIN_INJECTION = Double.parseDouble(config[4]);
        Config.MAX_CUMULATIVE_DOSE = Double.parseDouble(config[5]);
    }

    // Method to insert to the data table
    public void insertData(double sugarLevel, double injectAmount) {
        int data_id = getLatestID("data", "data_ID");
        closeConnection();
        String query = "INSERT INTO data (data_ID,blood_sug_lvl,last_update,inj_amnt,users_user_id) VALUES ("
                + data_id + "," + sugarLevel + ",now()," + injectAmount + ",1)";
        sendUpdate(query);
    }

    // Method to insert to the status table
    public void insertStatus(int charge, int reserve, String alert) {
        String query;
        int status_id= getLatestID("status", "status_ID");
        closeConnection();
        if (alert.equals("")) {
            query = "INSERT INTO status (status_id,last_update,battery_charge,reserve_amnt,users_user_id) VALUES ("
                    + status_id + ",now()," + charge + "," + reserve + ",1)";
        } else {
            query = "INSERT INTO status (status_id,last_update,battery_charge,reserve_amnt,alert,users_user_id) " +
                    "VALUES (" + status_id + ",now()," + charge + "," + reserve + ",'" + alert + "',1)";
        }
        sendUpdate(query);
    }
    // Get the next ID for the PK
    private int getLatestID(String table, String column) {
        String query = "SELECT " + column + " FROM " + table + " ORDER BY " + column + " DESC LIMIT 1";
        sendQuery(query);
        Number result = 0;
        try {
            if(resultSet.next()){
                result = ((Number) resultSet.getObject(1)).intValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.intValue() + 1;
    }
    // Custom Query for deletion or changing
    public void customQuery(String query) {
        sendQuery(query);
    }

    public void customUpdate(String query) {
        sendUpdate(query);
    }

    public void shutDownExecutors(ScheduledExecutorService executorService) {
        //Code for shutting down executor service
        try {
            java.lang.System.out.println("attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            java.lang.System.err.println("tasks interrupted");
        }
        finally {
            if (!executorService.isTerminated()) {
                java.lang.System.err.println("cancel non-finished tasks");
            }
            executorService.shutdownNow();
            java.lang.System.out.println("shutdown finished");
        }
    }
}
