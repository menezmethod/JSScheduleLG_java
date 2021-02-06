package Models;

import java.util.Date;
import java.sql.Timestamp;

/**
 * Customer Models
 * @author Luis J. Gimenez
 */
public class Customer {
  private Date createDate;
  private int customerId;
  private String address;
  private String country;
  private String Created_By;
  private String customerName;
  private String division;
  private String Last_Updated_By;
  private String phone;
  private String postalCode;
  private Timestamp Last_Update;
  /**
   * Create customer
   * @param customerId the customer ID
   * @param customerName the customer name
   * @param address the customer address
   * @param postalCode the customer postal code
   * @param phone the customer phone number
   * @param createDate the date/time that customer was created on
   * @param Created_By the user that customer was created by
   * @param Last_Update the date/time that customer was last updated by
   * @param Last_Updated_By the user that last updated customer information
   * @param division the customer division
   * @param country the customer country
   */
  public Customer(int customerId,
                  String customerName,
                  String address,
                  String postalCode,
                  String phone,
                  Date createDate,
                  String Created_By,
                  Timestamp Last_Update,
                  String Last_Updated_By,
                  String division,
                  String country) {
    super();
    setId(customerId);
    setName(customerName);
    setAddress(address);
    setPostalCode(postalCode);
    setPhone(phone);
    setCreateDate(createDate);
    setCreatedBy(Created_By);
    setLastUpdate(Last_Update);
    setLast_Updated_By(Last_Updated_By);
    setDivision(division);
    setCountry(country);
  }
  /**
   * sets customer id
   * @param customerId id
   */
  public void setId(int customerId) {
    this.customerId = customerId;
  }
  /**
   * sets customer name
   * @param customerName name
   */
  public void setName(String customerName) {
    this.customerName = customerName;
  }
  /**
   * sets customer address
   * @param address address
   */
  public void setAddress(String address) {
    this.address = address;
  }
  /**
   * sets customer postal code
   * @param postalCode postal code
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  /**
   * sets customer phone number
   * @param phone phone number
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }
  /**
   * sets date and time customer was created
   * @param createDate date and time
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  /**
   * sets user that customer was created by
   * @param Created_By user
   */
  public void setCreatedBy(String Created_By) {
    this.Created_By = Created_By;
  }
  /**
   * Sets date and time customer was updated
   * @param Last_Update date and time
   */
  public void setLastUpdate(Timestamp Last_Update) {
    this.Last_Update = Last_Update;
  }
  /**
   * sets user that updated customer
   * @param Last_Updated_By user
   */
  public void setLast_Updated_By(String Last_Updated_By) {
    this.Last_Updated_By = Last_Updated_By;
  }
  /**
   * sets customer division
   * @param division division
   */
  public void setDivision(String division) {
    this.division = division;
  }
  /**
   * sets country
   * @param country country
   */
  public void setCountry(String country) {
    this.country = country;
  }
  /**
   * gets id
   * @return ID
   */
  public int getCustomerId() {
    return customerId;
  }
  /**
   * Gets name
   * @return name
   */
  public String getCustomerName() {
    return customerName;
  }
  /**
   * Gets address
   * @return address
   */
  public String getAddress() {
    return address;
  }
  /**
   * Gets postal code
   * @return postal code
   */
  public String getPostalCode() {
    return postalCode;
  }
  /**
   * Gets phone number
   * @return phone number
   */
  public String getPhone() {
    return phone;
  }
  /**
   * Gets the date and time customer was created
   * @return date and time
   */
  public Date getCreateDate() {
    return createDate;
  }
  /**
   * Gets user
   * @return User that customer was created by
   */
  public String getCreatedBy() {
    return Created_By;
  }
  /**
   * Gets date and time customer was updated
   * @return date and time
   */
  public Timestamp getLastUpdate() {
    return Last_Update;
  }
  /**
   * gets user who updated customer
   * @return user
   */
  public String getLast_Updated_By() {
    return Last_Updated_By;
  }
  /**
   * gets division
   * @return division
   */
  public String getDivision() {
    return division;
  }
  /**
   * gets country
   * @return country
   */
  public String getCountry() {
    return country;
  }
}