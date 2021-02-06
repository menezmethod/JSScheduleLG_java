package Controllers;

import Utility.AppointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Utility.DBConnection;
import Models.Appointment;
import Models.CacheDB;
import Utility.TimeConversion;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for updating an appointment
 * @author Luis J. Gimenez
 */
public class UpdateAppointment implements Initializable {

  private final CacheDB cacheDB;
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
  private final Appointment appointment;
  private String customer;
  private String contact;
  private String user;

  @FXML private AnchorPane rootPane;
  @FXML private Button cancelBtn;
  @FXML private Button saveBtn;
  @FXML private ComboBox<String> contactBoxIn;
  @FXML private ComboBox<String> customerBoxIn;
  @FXML private ComboBox<String> endHr;
  @FXML private ComboBox<String> endMin;
  @FXML private ComboBox<String> startHr;
  @FXML private ComboBox<String> startMin;
  @FXML private ComboBox<String> userBoxIn;
  @FXML private DatePicker endTimeIn;
  @FXML private DatePicker startTimeIn;
  @FXML private Label appointmentIdLbl;
  @FXML private Label contactLbl;
  @FXML private Label customerIdLbl;
  @FXML private Label descriptionLbl;
  @FXML private Label endLbl;
  @FXML private Label endTimeLabel;
  @FXML private Label locationLbl;
  @FXML private Label startLbl;
  @FXML private Label startTimeLabel;
  @FXML private Label titleLbl;
  @FXML private Label typeLbl;
  @FXML private Label userLbl;
  @FXML private TextField appointmentIdFld;
  @FXML private TextField descriptionIn;
  @FXML private TextField locationIn;
  @FXML private TextField titleIn;
  @FXML private TextField typeIn;
  /**
   * Initializes update appointment screen
   * @param cacheDB caches local data for faster access
   * @param appointment appointment to be updated
   */
  public UpdateAppointment(CacheDB cacheDB, Appointment appointment) {
    this.cacheDB = cacheDB;
    this.appointment = appointment;
  }
  /**
   * Initializes
   * @param url fxml file
   * @param resourceBundle language
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    makeBoxes();
    makeLabels();
    makeTimeConv();
    setData();
  }
  /**
   * Sets input fields
   */
  private void setData() {
    appointmentIdFld.setText(Integer.toString(appointment.getAppointmentId()));
    titleIn.setText(appointment.getTitle());
    descriptionIn.setText(appointment.getDescription());
    locationIn.setText(appointment.getLocation());
    customerBoxIn.setValue(this.customer);
    typeIn.setText(appointment.getType());
    startTimeIn.setValue(appointment.getLocalStart().toLocalDateTime().toLocalDate());
    startHr.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getHour()));
    startMin.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getMinute()));
    endTimeIn.setValue(appointment.getLocalEnd().toLocalDateTime().toLocalDate());
    endHr.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getHour()));
    endMin.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getMinute()));
    userBoxIn.setValue(this.user);
    contactBoxIn.setValue(this.contact);
  }

  /**
   * Loads time in correct time zone
   */
  private void makeTimeConv() {
    ObservableList<String> startHrs = FXCollections.observableArrayList();
    ObservableList<String> endHrs = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-31 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-31 22:00", formatter);
    ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
    ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
    ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
    ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());

    for (int i= localStart.getHour(); i <= localEnd.getHour(); i++) {
      if (i<10) {
        startHrs.add("0"+ i);
        endHrs.add("0"+ i);

      } else {
        endHrs.add(Integer.toString(i));
        if (i < localEnd.getHour() && localEnd.getMinute() == 0) {
          startHrs.add(Integer.toString(i));
        }
      }
    }

    startMinutes.addAll("00", "15", "30", "45");
    endMinutes.addAll("00", "15", "30", "45");
    startHr.setItems(startHrs);
    startMin.setItems(startMinutes);
    endHr.setItems(endHrs);
    endMin.setItems(endMinutes);
  }

  /**
   * Loads ComboBoxes
   */
  private void makeBoxes() {
    String s = "SELECT * FROM contacts";
    String s1 = "SELECT * FROM customers";
    String s2 = "SELECT * FROM users";
    try {
      Statement statement = DBConnection.open().createStatement();
      Statement statement1 = DBConnection.open().createStatement();
      Statement statement2 = DBConnection.open().createStatement();
      ResultSet resultSet = statement.executeQuery(s);
      ResultSet resultSet1 = statement1.executeQuery(s1);
      ResultSet resultSet2 = statement2.executeQuery(s2);

      this.contactBoxIn.getItems().clear();
      this.customerBoxIn.getItems().clear();
      this.userBoxIn.getItems().clear();
      while (resultSet.next()) {
        String contact = resultSet.getString("Contact_Name");
        int contactId = resultSet.getInt("Contact_ID");
        contactBoxIn.getItems().add(contact);
        if (contactId == appointment.getContactId()) this.contact = contact;
      }
      while (resultSet1.next()) {
        String customer = resultSet1.getString("Customer_Name");
        int customerId = resultSet1.getInt("Customer_ID");
        this.customerBoxIn.getItems().add(customer);
        if (customerId == appointment.getCustomerId()) this.customer = customer;
      }
      while (resultSet2.next()) {
        String user = resultSet2.getString("User_Name");
        int userId = resultSet2.getInt("User_ID");
        this.userBoxIn.getItems().add(user);
        if (userId == appointment.getUserId()) this.user = user;
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Loads labels with proper language
   */
  private void makeLabels() {
    titleLbl.setText(rb.getString("updateAppointment"));
    appointmentIdLbl.setText(rb.getString("id"));
    appointmentIdFld.setPromptText(rb.getString("disabledAutoGen"));
    titleLbl.setText(rb.getString("title"));
    titleIn.setPromptText(rb.getString("title"));
    descriptionLbl.setText(rb.getString("description"));
    descriptionIn.setPromptText(rb.getString("description"));
    locationLbl.setText(rb.getString("location"));
    locationIn.setPromptText(rb.getString("location"));
    contactLbl.setText(rb.getString("contact"));
    typeLbl.setText(rb.getString("type"));
    typeIn.setPromptText(rb.getString("type"));
    startLbl.setText(rb.getString("startTime"));
    endLbl.setText(rb.getString("endTime"));
    customerIdLbl.setText(rb.getString("customerId"));
    saveBtn.setText(rb.getString("save"));
    cancelBtn.setText(rb.getString("cancel"));
  }
  /**
   * Validates selected appointment time
   * @param utcStart Selected meeting start
   * @param utcEnd Selected meeting end
   * @param customerId Selected customer
   * @param appointmentId Selected appointment being validated
   * @return Boolean if selected appointment hours are valid or not
   */
  /**
   * Validates selected appointment time
   * @param utcStart Selected meeting start
   * @param utcEnd Selected meeting end
   * @param customerId Selected customer
   * @param appointmentId Selected appointment being validated
   * @return Boolean if selected appointment hours are valid or not
   */
  private boolean validAppointment(Timestamp utcStart, Timestamp utcEnd, int customerId, int appointmentId) throws SQLException {
    ZonedDateTime UtcStart = ZonedDateTime.of(utcStart.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstStart = UtcStart.withZoneSameInstant(ZoneId.of("America/New_York"));
    ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstEnd = UtcEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

    if (EstStart.getHour() > 22 || EstStart.getHour() < 8 || EstEnd.getHour() > 22 || EstEnd.getHour() < 8) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("businessHours"));
      alert.setHeaderText(rb.getString("businessHoursMessage"));
      alert.showAndWait();
      return false;
    }
    /**
     * Calls database to validate
     */
    if (!AppointmentDB.validate(utcStart, utcEnd, customerId, appointmentId)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("scheduleConflict"));
      alert.setHeaderText(rb.getString("customerScheduleConflict"));
      alert.showAndWait();
      return false;
    }
    return true;
  }
  /**
   * Validates then saves updated appointment
   * @param event on save button click
   */
  @FXML
  void doSave(ActionEvent event) throws IOException {
    boolean valid = false;
    int newId = Integer.parseInt(appointmentIdFld.getText());
    String titleFieldText = titleIn.getText();
    String descriptionFieldText = descriptionIn.getText();
    String locationFieldText = locationIn.getText();
    String typeFieldText = typeIn.getText();
    LocalDate startTime = startTimeIn.getValue();
    String startHrValue = startHr.getValue();
    String startMinValue = startMin.getValue();
    LocalDate endTime = endTimeIn.getValue();
    String endHrValue = endHr.getValue();
    String endMinValue = endMin.getValue();
    Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
    String Last_Updated_By = cacheDB.getUser();
    String contactFieldText = contactBoxIn.getSelectionModel().getSelectedItem();
    String customerIdFieldText = customerBoxIn.getSelectionModel().getSelectedItem();
    String userBoxFieldText = userBoxIn.getSelectionModel().getSelectedItem();

    if (titleFieldText.equals("") || descriptionFieldText.equals("") || locationFieldText.equals("") || contactFieldText.equals("")
            || typeFieldText.equals("") || startTime == null || startHrValue.equals("") || startMinValue.equals("") || endTime == null ||
            endHrValue.equals("") || endMinValue.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      TimeConversion localTime = new TimeConversion();
      Timestamp utcStartTime = localTime.convertToUTC(startTime, startHrValue, startMinValue);
      Timestamp utcEndTime = localTime.convertToUTC(endTime, endHrValue, endMinValue);

      Timestamp localStartTimestamp = localTime.convertToLocal(startTime, startHrValue, startMinValue);
      Timestamp localEndTimestamp = localTime.convertToLocal(endTime, endHrValue, endMinValue);

      try {
        String sqlOne = "SELECT * FROM contacts WHERE Contact_Name = '" + contactFieldText + "';";
        Statement statement = DBConnection.open().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlOne);
        int contactId = 0;
        while (resultSet.next()) {
          contactId = resultSet.getInt("Contact_ID");
        }
        String sqlTwo = "SELECT * FROM customers WHERE Customer_Name = '" + customerIdFieldText + "';";
        statement = DBConnection.open().createStatement();
        ResultSet resultSet1 = statement.executeQuery(sqlTwo);
        int customerId = 0;
        while (resultSet1.next()) {
          customerId = resultSet1.getInt("Customer_ID");
        }
        String sqlThree = "SELECT * FROM users WHERE User_Name = '" + userBoxFieldText + "';";
        statement = DBConnection.open().createStatement();
        ResultSet resultThree = statement.executeQuery(sqlThree);
        int userId = 0;
        while (resultThree.next()) {
          userId = resultThree.getInt("User_ID");
        }
        if (validAppointment(utcStartTime, utcEndTime, customerId, newId)) {
          String updateSql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Last_Update = ?, Last_Updated_By = ?, Start = ?, End = ?, Contact_ID = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?";
          PreparedStatement insert = DBConnection.open().prepareStatement(updateSql);
          insert.setString(1, titleFieldText);
          insert.setString(2, descriptionFieldText);
          insert.setString(3, locationFieldText);
          insert.setString(4, typeFieldText);
          insert.setTimestamp(5, current_time);
          insert.setString(6, Last_Updated_By);
          insert.setTimestamp(7, localStartTimestamp);
          insert.setTimestamp(8, localEndTimestamp);
          insert.setInt(9, contactId);
          insert.setInt(10, customerId);
          insert.setInt(11, userId);
          insert.setInt(12, newId);
          appointment.setContactId(contactId);
          appointment.setCustomerId(customerId);
          appointment.setDescription(descriptionFieldText);
          appointment.setEnd(utcEndTime);
          appointment.setFormattedLocalEnd(localEndTimestamp);
          appointment.setFormattedLocalStart(localStartTimestamp);
          appointment.setLastUpdate(current_time);
          appointment.setLast_Updated_By(Last_Updated_By);
          appointment.setLocalEnd(localEndTimestamp);
          appointment.setLocalStart(localStartTimestamp);
          appointment.setLocation(locationFieldText);
          appointment.setName(titleFieldText);
          appointment.setStart(utcStartTime);
          appointment.setType(typeFieldText);
          appointment.setUserId(userId);
          try (var ps = insert) {
            ps.executeUpdate();
          }
          valid = true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Appointment.fxml"));
        Appointments controller = new Appointments(cacheDB);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
      }
    }
  }
  /**
   * Update the applicable end-minute options.
   */
  @FXML
  void updateCombo(ActionEvent event) {
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    endMin.getItems().clear();
    String endHrValue = endHr.getValue();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
    ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
    ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
    ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
    ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());
    if (Integer.parseInt(endHrValue) == localEnd.getHour() && localEnd.getMinute() == 0) {
      endMinutes.addAll("00");
    } else if (Integer.parseInt(endHrValue) == localStart.getHour()  && localStart.getMinute() == 0) {
      endMinutes.addAll("15", "30", "45");
    }
    else {
      endMinutes.addAll("00", "15", "30", "45");
    }
    startMinutes.addAll("00", "15", "30", "45");
    endMin.setItems(endMinutes);
    startMin.setItems(startMinutes);
  }
 /**
   * Confirms with user to cancel then sends user back to appointment window
   * @param event click cancel button
   * @throws IOException if unsuccessful
   */
  @FXML
  void doCancel(ActionEvent event) throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(rb.getString("cancelConfirmation"));
    alert.setContentText(rb.getString("cancelAppointmentMessage"));
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Appointment.fxml"));
      Appointments controller = new Appointments(cacheDB);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
  }
}