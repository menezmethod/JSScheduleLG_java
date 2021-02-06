package Models;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;



/**
 * Appointments Model
 * @author Luis J. Gimenez
 */
public class Appointment {
  private int appointmentId;
  private int contactId;
  private int customerId;
  private int userId;
  private String Created_By;
  private String description;
  private String formattedLocalEnd;
  private String formattedLocalStart;
  private String Last_Updated_By;
  private String location;
  private String title;
  private String type;
  private Timestamp createDate;
  private Timestamp end;
  private Timestamp Last_Update;
  private Timestamp localEnd;
  private Timestamp localStart;
  private Timestamp start;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
  /**
   * Creates a new Appointment
   * @param appointmentId the appointment ID
   * @param title the appointment title
   * @param description the appointment description
   * @param location the appointment office location
   * @param type the type of appointment
   * @param contactId the Contact ID
   * @param start the appointment start time
   * @param end the appointment end time
   * @param createDate the Date/time that appointment was created on
   * @param Created_By the user that appointment was created by
   * @param Last_Update the Date/time that appointment was last updated by
   * @param Last_Updated_By the user that appointment was last updated by
   * @param customerId the Customer ID
   * @param userId the User ID
   */
  public Appointment(int appointmentId, String title, String description, String location, String type, int contactId, Timestamp start, Timestamp end, Timestamp createDate, String Created_By, Timestamp Last_Update, String Last_Updated_By, int customerId, int userId) {
    super();
    setId(appointmentId);
    setName(title);
    setDescription(description);
    setLocation(location);
    setType(type);
    setContactId(contactId);
    setStart(start);
    setEnd(end);
    setLocalStart(start);
    setFormattedLocalStart(start);
    setLocalEnd(end);
    setFormattedLocalEnd(end);
    setCreateDate(createDate);
    setCreatedBy(Created_By);
    setLastUpdate(Last_Update);
    setLast_Updated_By(Last_Updated_By);
    setCustomerId(customerId);
    setUserId(userId);
  }
  /**
   * setter for appointment Id
   * @param appointmentId id
   */
  public void setId(int appointmentId) {
    this.appointmentId = appointmentId;
  }
  /**
   * setter for appointment name
   * @param title name
   */
  public void setName(String title) {
    this.title = title;
  }
  /**
   * Setter for appointment description
   * @param description description
   */
  public void setDescription(String description) {
    this.description = description;
  }
  /**
   * Setter for appointment location
   * @param location location
   */
  public void setLocation(String location) {
    this.location = location;
  }
  /**
   * Setter for appointment type
   * @param type type
   */
  public void setType(String type) {
    this.type = type;
  }
  /**
   * setter for start time
   * @param start start time
   */
  public void setStart(Timestamp start) {
    this.start = start;
  }
  /**
   * sets app in local time zone
   * @param localStart start time
   */
  public void setLocalStart(Timestamp localStart) {
    this.localStart = localStart;
  }
  /**
   * sets time in 12 hour, easily readable format
   * @param localStart time
   */
  public void setFormattedLocalStart(Timestamp localStart) {
    this.formattedLocalStart = localStart.toLocalDateTime().format(formatter);
  }
  /**
   * sets end time
   * @param end time
   */
  public void setEnd(Timestamp end) {
    this.end = end;
  }
  /**
   * sets app in local time zone
   * @param localEnd end time
   */
  public void setLocalEnd(Timestamp localEnd) {
    this.localEnd = localEnd;
  }
  /**
   * sets time to readable format
   * @param localEnd end
   */
  public void setFormattedLocalEnd(Timestamp localEnd) {
    this.formattedLocalEnd = localEnd.toLocalDateTime().format(formatter);
  }
  /**
   * sets create date
   * @param createDate date
   */
  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }
  /**
   * sets who created the appointment
   * @param Created_By user
   */
  public void setCreatedBy(String Created_By) {
    this.Created_By = Created_By;
  }
  /**
   * sets last updated
   * @param Last_Update time
   */
  public void setLastUpdate(Timestamp Last_Update) {
    this.Last_Update = Last_Update;
  }
  /**
   * sets who updated it last
   * @param Last_Updated_By user
   */
  public void setLast_Updated_By(String Last_Updated_By) {
    this.Last_Updated_By = Last_Updated_By;
  }
  /**
   * Gets id of  customer
   * @param customerId id
   */
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }
  /**
   * sets user id
   * @param userId id
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }
  /**
   * sets contact id
   * @param contactId id
   */
  public void setContactId(int contactId) {
    this.contactId = contactId;
  }
  /**
   * gets appointment id
   * @return id
   */
  public int getAppointmentId() {
    return appointmentId;
  }
  /**
   * gets appointment title
   * @return title
   */
  public String getTitle() {
    return title;
  }
  /**
   * gets appointment description
   * @return description
   */
  public String getDescription() {
    return description;
  }
  /**
   * gets location
   * @return location
   */
  public String getLocation() {
    return location;
  }
  /**
   * gets type
   * @return type
   */
  public String getType() {
    return type;
  }
  /**
   * gets starting time
   * @return start
   */
  public Timestamp getStart() {
    return start;
  }
  /**
   * gets start in local time zone
   * @return start time
   */
  public Timestamp getLocalStart() {
    return localStart;
  }
  /**
   * gets formatted local start
   * @return time
   */
  public String getFormattedLocalStart() {
    return formattedLocalStart;
  }
  /**
   * gets ending time
   * @return time
   */
  public Timestamp getEnd() {
    return end;
  }
  /**
   * gets local ending time
   * @return time
   */
  public Timestamp getLocalEnd() {
    return localEnd;
  }
  /**
   * gets formatted local time
   * @return end time
   */
  public String getFormattedLocalEnd() {
    return formattedLocalEnd;
  }
  /**
   * gets date and time it was created
   * @return date and time
   */
  public Timestamp getCreateDate() {
    return createDate;
  }
  /**
   * gets user who created app
   * @return user
   */
  public String getCreatedBy() {
    return Created_By;
  }
  /**
   * gets date and time last updated
   * @return last updated
   */
  public Timestamp getLastUpdate() {
    return Last_Update;
  }
  /**
   * gets user who updated it last
   * @return user
   */
  public String getLast_Updated_By() {
    return Last_Updated_By;
  }
  /**
   * gets id of user that created app
   * @return user id
   */
  public int getUserId() {
    return userId;
  }
  /**
   * gets id of contact
   * @return contact id
   */
  public int getContactId() {
    return contactId;
  }
  /**
   * gets id of customer
   * @return customer ID
   */
  public int getCustomerId() {
    return customerId;
  }
}