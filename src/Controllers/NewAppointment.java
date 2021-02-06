package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Models.CacheDB;
import Utility.AppointmentDB;
import Utility.DBConnection;
import Utility.TimeConversion;

/**
 * Controller to add new appointments
 * @author Luis J. Gimenez
 */
public class NewAppointment implements Initializable {
  private final CacheDB cacheDB;
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
  private List<String> contacts = new ArrayList<>();
  private List<String> customers = new ArrayList<>();
  private List<String> users = new ArrayList<>();

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
  @FXML private Label endTimeLbl;
  @FXML private Label headTitle;
  @FXML private Label locationLbl;
  @FXML private Label startLbl;
  @FXML private Label startTimeLbl;
  @FXML private Label titleLbl;
  @FXML private Label typeLbl;
  @FXML private Label userLbl;
  @FXML private TextField appointmentIdFld;
  @FXML private TextField descriptionIn;
  @FXML private TextField locationIn;
  @FXML private TextField titleIn;
  @FXML private TextField typeIn;
  /***
   * Second initialization step.
   * @param url indicates that the protocol to use is http.
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    makeBoxes();
    makeLabels();
    doTimeConv();
  }
  /**
   * The constructor
   * @param cacheDB the local data for performance
   */
  public NewAppointment(CacheDB cacheDB) {
    this.cacheDB = cacheDB;
  }
  /**
   * Loads ComboBoxes
   */
  private void makeBoxes() {
    this.contactBoxIn.getItems().clear();
    this.contacts.clear();
    this.customerBoxIn.getItems().clear();
    this.customers.clear();
    this.userBoxIn.getItems().clear();
    this.users.clear();
    for (int i = 0; i < cacheDB.getAllContacts().size(); i++) {
      String contact = cacheDB.getAllContacts().get(i).getName();
      contactBoxIn.getItems().add(contact);
      this.contacts.add(i, contact);
    }
    for (int i = 0; i < cacheDB.getAllCustomers().size(); i++) {
      String customer = cacheDB.getAllCustomers().get(i).getCustomerName();
      this.customerBoxIn.getItems().add(customer);
      this.customers.add(customer);
    }
    try {
      Statement statement = DBConnection.open().createStatement();
      String sql = "SELECT * FROM users";
      ResultSet result = statement.executeQuery(sql);
      while (result.next()) {
        String user = result.getString("User_Name");
        this.userBoxIn.getItems().add(user);
        this.users.add(user);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Loads labels with proper language
   */
  private void makeLabels() {
    titleLbl.setText(rb.getString("addAppointment"));
    appointmentIdLbl.setText(rb.getString("id"));
    appointmentIdFld.setPromptText(rb.getString("disabledAutoGen"));
    titleLbl.setText(rb.getString("addCustomer"));
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
    headTitle.setText(rb.getString("addAppointment"));
  }
  /**
   * Loads the proper formatted time
   */
  private void doTimeConv() {
    ObservableList<String> startHrs = FXCollections.observableArrayList();
    ObservableList<String> endHrs = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
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
   * Cancels and user is sent back to the appointment screen
   * @param event cancel button.
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
      stage.setScene(new Scene(loader.load()));
      stage.show();
    }
  }
  /**
   * Appointment validation process. Check to make sure that appointments are withing business hours and also do not over lap.
   * @param utcStart the start time
   * @param utcEnd the end time
   * @param customerId the customer ID
   * @param appointmentId the appointment ID
   * @return returns true or false
   * @throws SQLException if unsuccessful
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
     * Calls database for validation
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
   * Creates auto-increment ID
   * @return id
   * @throws SQLException if unsuccessful
   */
  public static int getNewId() throws SQLException {
    String sql = "SELECT * FROM appointments";
    Statement stmt = DBConnection.open().createStatement();
    ResultSet result = stmt.executeQuery(sql);
    int count = 0;
    while (result.next()) {
      if (result.getInt("Appointment_ID") > count) count = result.getInt("Appointment_ID");
    }
    DBConnection.close();
    return count + 1;
  }
  @FXML
  void doSave(ActionEvent event) throws SQLException, IOException {
    int newId = 0;
    boolean valid = false;
    newId = getNewId();
    String titleFieldText = titleIn.getText();
    String locationFieldText = locationIn.getText();
    String descriptionFieldText = descriptionIn.getText();
    LocalDate startTime = startTimeIn.getValue();
    String startHrValue = startHr.getValue();
    String startMinValue = startMin.getValue();
    LocalDate endTime = endTimeIn.getValue();
    String endHrValue = endHr.getValue();
    String endMinValue = endMin.getValue();
    String contactFieldText = contactBoxIn.getSelectionModel().getSelectedItem();
    String customerFieldText = customerBoxIn.getSelectionModel().getSelectedItem();
    String typeFieldText = typeIn.getText();
    if (titleFieldText.equals("") || descriptionFieldText.equals("") || locationFieldText.equals("") || contactFieldText == null
            || customerFieldText == null || typeFieldText.equals("") || startTime == null || startHrValue == null || startMinValue== null || endTime == null ||
            endHrValue== null || endMinValue== null) {
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

      Timestamp current_date = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
      String Created_By = cacheDB.getUser();
      String Last_Updated_By = cacheDB.getUser();
      String customerIdFieldText = customerBoxIn.getSelectionModel().getSelectedItem();
      String userBoxFieldText = userBoxIn.getSelectionModel().getSelectedItem();
      String sql = "SELECT * FROM contacts WHERE Contact_Name = '"+contactFieldText+"';";
      Statement statement = DBConnection.open().createStatement();
      ResultSet resultOne = statement.executeQuery(sql);
      int contactId = 0;
      while (resultOne.next()) {
        contactId = resultOne.getInt("Contact_ID");
      }
      String sqlTwo = "SELECT * FROM customers WHERE Customer_Name = '"+customerIdFieldText+"';";
      statement = DBConnection.open().createStatement();
      ResultSet resultSet1 = statement.executeQuery(sqlTwo);
      int customerId = 0;
      while (resultSet1.next()) {
        customerId = resultSet1.getInt("Customer_ID");
      }
      String sqlThree = "SELECT * FROM users WHERE User_Name = '"+userBoxFieldText+"';";
      statement = DBConnection.open().createStatement();
      ResultSet resultThree = statement.executeQuery(sqlThree);
      int userId = 0;
      while (resultThree.next()) {
        userId = resultThree.getInt("User_ID");
      }
      if (validAppointment(utcStartTime, utcEndTime, customerId, newId)) {
        cacheDB.newAppointment(AppointmentDB.makeAppointment(newId, titleFieldText, descriptionFieldText, locationFieldText, typeFieldText, localEndTimestamp, localStartTimestamp, current_date, contactId, utcStartTime, utcEndTime, Created_By, Last_Updated_By, customerId, userId));
        valid=true;
      }
      if (valid) {
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
  /**
   * Updates ComboBoxes
   * @param event on combobox click
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
}