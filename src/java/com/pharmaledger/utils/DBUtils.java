package com.pharmaledger.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility for SQL Server
 */
public class DBUtils {
    
    private static final String DB_NAME = "PharmaLedger";
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "12345";
    
    /**
     * Get database connection
     * @return Connection object
     * @throws ClassNotFoundException if driver not found
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";databaseName=" + DB_NAME + ";encrypt=false";
        conn = DriverManager.getConnection(url, USER_NAME, PASSWORD);
        return conn;
    }
    
    /**
     * Close connection safely
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
