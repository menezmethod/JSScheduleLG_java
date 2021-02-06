package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

/**
 * Cache of local data used for easy access without having to access database every time
 * @author Luis J. Gimenez
 */
public class CacheDB {
  private String user;
  private Locale locale;
  private ObservableList<Customer> allCustomers;
  private ObservableList<Contact> allContacts;
  private ObservableList<Appointment> allAppointments;
  /**
   * creates and loads new cache
   */
  public CacheDB() {
    allAppointments = FXCollections.observableArrayList();
    allContacts = FXCollections.observableArrayList();
    allCustomers = FXCollections.observableArrayList();
  }
  /**
   * updates local time of user
   * @param newLocale local code
   */
  public void updateLocale(Locale newLocale) {
    if (newLocale != null) {
      locale = newLocale;
    }
  }
  /**
   * adds the user logged in
   * @param auth user logged in
   */
  public void newUser(String auth) {
    if (auth != null) {
      user = auth;
    }
  }
  /**
   * adds new appointment
   * @param newAppointment app
   */
  public void newAppointment(Appointment newAppointment) {
    if (newAppointment != null) {
      allAppointments.add(newAppointment);
    }
  }
  /**
   * adds new customer
   * @param newCustomer new customer
   */
  public void newCustomer(Customer newCustomer) {
    if (newCustomer != null) {
      allCustomers.add(newCustomer);
    }
  }
  /**
   * adds new contact
   * @param newContact new contact
   */
  public void newContact(Contact newContact) {
    if (newContact != null) {
      allContacts.add(newContact);
    }
  }
  /**
   * gets logged in user's location
   * @return locale of user
   */
  public Locale getLocale() {
    return locale;
  }
  /**
   * gets username
   * @return user name
   */
  public String getUser() {
    return user;
  }
  /**
   * gets list of all appointments
   * @return all appointments
   */
  public ObservableList<Appointment> getAllAppointments() {
    return allAppointments;
  }
  /**
   * gets list of all customers
   * @return all customers
   */
  public ObservableList<Customer> getAllCustomers() {
    return allCustomers;
  }
  /**
   * gets list of all contacts
   * @return all contacts
   */
  public ObservableList<Contact> getAllContacts() {
    return allContacts;
  }
}