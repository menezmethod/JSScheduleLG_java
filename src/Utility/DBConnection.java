package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that connects to the database
 * @author Luis J. Gimenez
 */
public class DBConnection {
  // Database Information
  private static final String databaseName="WJ07WY0";
  private static final String DB_URL="jdbc:mysql://wgudb.ucertify.com/" + databaseName + "?useSSL=false";
  private static final String username="U07WY0";
  private static final String password="53689151104";
  public static Connection conn;
  /**
   * Opens connection to the database
   * @return the connection so data can be accessed
   * @throws SQLException exception if unsuccessful
   */
  public static Connection open() throws SQLException {
    conn = DriverManager.getConnection(DB_URL, username, password);
    return conn;
  }
  /**
   * Closes connection to the database
   * @throws SQLException exception if unsuccessful
   */
  public static void close() throws SQLException {
    conn.close();
  }
}