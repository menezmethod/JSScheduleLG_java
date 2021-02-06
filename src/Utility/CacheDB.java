package Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import Models.Appointment;
import Models.Contact;
import Models.Customer;

/**
 * Gets local data to initialize the program and improve performance.
 * @author Luis J. Gimenez
 */
public class CacheDB {

  /**
   * Loads data into the cache
   * @param cacheDB the main cache for the program
   * @throws SQLException if sql unsuccessful
   */
  public static void feedData(Models.CacheDB cacheDB) throws SQLException {
    try {
      String s = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Create_Date, c.Created_By, c.Last_Update, c.Last_Updated_By, c.Division_ID, d.Division, n.Country FROM customers c JOIN first_level_divisions d ON (c.Division_ID = d.Division_ID) JOIN countries n ON (d.COUNTRY_ID = n.Country_ID);";
      try (var ps = DBConnection.open().prepareStatement(s);
           ResultSet result = ps.executeQuery()) {
        while (result.next()) {
          int id = result.getInt("c.Customer_ID");
          String name = result.getString("c.Customer_Name");
          String address = result.getString("c.Address");
          String postal = result.getString("c.Postal_Code");
          String phone = result.getString("c.Phone");
          Date create_date = result.getDate("c.Create_Date");
          String Created_By = result.getString("c.Created_By");
          Timestamp last_update = result.getTimestamp("c.Last_Update");
          String Last_Updated_By = result.getString("c.Last_Updated_By");
          String division = result.getString("d.Division");
          String country = result.getString("n.Country");
          Customer customer = new Customer(id, name, address, postal, phone, create_date, Created_By, last_update, Last_Updated_By, division, country);
          cacheDB.newCustomer(customer);
        }
      }
      String s1 = "SELECT * FROM appointments a JOIN contacts c ON (a.Contact_ID = c.Contact_ID) JOIN customers m ON (a.Customer_ID = m.Customer_ID) JOIN users u ON (a.User_ID = u.User_ID);";
      try (var ps = DBConnection.open().prepareStatement(s1);
           ResultSet result = ps.executeQuery()) {
        while (result.next()) {
          int id = result.getInt("a.Appointment_ID");
          String title = result.getString("a.Title");
          String description = result.getString("a.Description");
          String location = result.getString("a.Location");
          String type = result.getString("a.Type");
          int contactId = result.getInt("a.Contact_ID");
          Timestamp start = result.getTimestamp("a.Start");
          Timestamp end = result.getTimestamp("a.End");
          Timestamp create_date = result.getTimestamp("a.Create_Date");
          String Created_By = result.getString("a.Created_By");
          Timestamp last_update = result.getTimestamp("a.Last_Update");
          String Last_Updated_By = result.getString("a.Last_Updated_By");
          int customerId = result.getInt("a.Customer_ID");
          int userId = result.getInt("a.User_ID");
          Appointment appointment = new Appointment(id, title, description, location, type, contactId, start, end, create_date, Created_By, last_update, Last_Updated_By, customerId, userId);
          cacheDB.newAppointment(appointment);
        }
      }
      String s2 = "SELECT * FROM contacts c;";
      try (var ps = DBConnection.open().prepareStatement(s2);
           ResultSet result = ps.executeQuery()) {
        while (result.next()) {
          int id = result.getInt("c.Contact_ID");
          String name = result.getString("c.Contact_Name");
          String email = result.getString("c.Email");
          Contact contact = new Contact(id, name, email);
          cacheDB.newContact(contact);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}