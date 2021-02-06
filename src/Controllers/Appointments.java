package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;


import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import Models.Appointment;
import Models.CacheDB;
import Utility.DBConnection;

/**
 * Controller for Appointment screen
 * @author Luis J. Gimenez
 */
public class Appointments implements Initializable {
  private final CacheDB cacheDB;
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");

  @FXML private AnchorPane rootPane;
  @FXML private Button addAppointment;
  @FXML private Button backButton;
  @FXML private Button deleteAppointment;
  @FXML private Button updateAppointment;
  @FXML private Label headTitle;
  @FXML private RadioButton allRadio;
  @FXML private RadioButton monthRadio;
  @FXML private RadioButton weekRadio;
  @FXML private TableColumn<?, ?> appointmentId;
  @FXML private TableColumn<?, ?> contact;
  @FXML private TableColumn<?, ?> customerId;
  @FXML private TableColumn<?, ?> description;
  @FXML private TableColumn<?, ?> endTime;
  @FXML private TableColumn<?, ?> location;
  @FXML private TableColumn<?, ?> startTime;
  @FXML private TableColumn<Appointment, String> title;
  @FXML private TableColumn<?, ?> type;
  @FXML private TableView<Appointment> appointmentTable;
  @FXML private TextField filterField;
  @FXML private ToggleGroup radioToggleGroup;
  private ObservableList<Appointment> masterData = FXCollections.observableArrayList();
  private ObservableList<Appointment> filteredData = FXCollections.observableArrayList();


  /**
   * Constructor for Appointments
   * @param cacheDB caches local data
   */
  public Appointments(CacheDB cacheDB) {
    this.cacheDB = cacheDB;
    masterData = new FilteredList<>(cacheDB.getAllAppointments());
    filteredData.addAll(masterData);

    // Listen for changes in master data.
    // Whenever the master data changes we must also update the filtered data.
    masterData.addListener(new ListChangeListener<Appointment>() {
      @Override
      public void onChanged(ListChangeListener.Change<? extends Appointment> change) {
        updateFilteredData();
      }
    });
  }
  /**
   * Initializes the screen
   * @param url indicates that the protocol to use is http.
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    makeLabels();
    radioToggleGroup = new ToggleGroup();
    allRadio.setToggleGroup(radioToggleGroup);
    weekRadio.setToggleGroup(radioToggleGroup);
    monthRadio.setToggleGroup(radioToggleGroup);
    allRadio.setSelected(true);
    weekRadio.setSelected(false);
    monthRadio.setSelected(false);
    headTitle.setText("Appointments");
    SortedList<Appointment> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(appointmentTable.comparatorProperty());
    appointmentTable.setItems(sortedData);

    appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
    title.setCellValueFactory(new PropertyValueFactory<>("title"));
    description.setCellValueFactory(new PropertyValueFactory<>("description"));
    location.setCellValueFactory(new PropertyValueFactory<>("location"));
    type.setCellValueFactory(new PropertyValueFactory<>("type"));
    contact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    startTime.setCellValueFactory(new PropertyValueFactory<>("start"));
    endTime.setCellValueFactory(new PropertyValueFactory<>("end"));

// Listen for text changes in the filter text field
    filterField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
                          String oldValue, String newValue) {
        updateFilteredData();
      }
    });
  }

  /**
   * Updates the filteredData to contain all data from the masterData that
   * matches the current filter.
   */
  private void updateFilteredData() {
    filteredData.clear();
    for (Appointment a : masterData) {
      if (matchesFilter(a)) {
        filteredData.add(a);
      }
    }
    // Must re-sort table after items changed
    reapplyTableSortOrder();
  }
  /**
   * Returns true if the Appointment matches the current filter. Lower/Upper case
   * is ignored.
   *
   * @param appointment
   * @return
   */
  private boolean matchesFilter(Appointment appointment) {
    String filterString = filterField.getText();
    if (filterString == null || filterString.isEmpty()) {
      // No filter --> Add all.
      return true;
    }
    String lowerCaseFilterString = filterString.toLowerCase();
    if (appointment.getTitle().toLowerCase().contains(lowerCaseFilterString)) {
      return true;
    } else if (appointment.getDescription().toLowerCase().contains(lowerCaseFilterString)) {
      return true;
    } else if (appointment.getLocation().toLowerCase().contains(lowerCaseFilterString)) {
      return true;
    }
    return false; // Does not match
  }

  private void reapplyTableSortOrder() {
    ArrayList<TableColumn<Appointment, ?>> sortOrder = new ArrayList<>(appointmentTable.getSortOrder());
    appointmentTable.getSortOrder().clear();
    appointmentTable.getSortOrder().addAll(sortOrder);
  }
  /**
   * Add labels in the proper language.
   */
  private void makeLabels() {
    appointmentId.setText(rb.getString("id"));
    title.setText(rb.getString("title"));
    description.setText(rb.getString("description"));
    location.setText(rb.getString("location"));
    contact.setText(rb.getString("contact"));
    type.setText(rb.getString("type"));
    headTitle.setText(rb.getString("appointments"));
    startTime.setText(rb.getString("startTime"));
    endTime.setText(rb.getString("endTime"));
    customerId.setText(rb.getString("customerId"));
    addAppointment.setText(rb.getString("add"));
    updateAppointment.setText(rb.getString("update"));
    deleteAppointment.setText(rb.getString("delete"));
    backButton.setText(rb.getString("back"));
  }
  /**
   * Sends user to add customer screen
   * @param event when add button clicks
   * @throws IOException if unsuccessful
   */
  @FXML
  void doNewAppointment(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/NewAppointment.fxml"));
    NewAppointment controller = new NewAppointment(cacheDB);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }
  /**
   * Removes selected appointment from the table
   * @param event button click
   * @throws SQLException if unsuccessful to delete
   */
  @FXML
  void doDeleteAppointment(ActionEvent event) throws SQLException {
    if (appointmentTable.getSelectionModel().getSelectedItem() != null) {
      var sql = "DELETE FROM appointments WHERE Appointment_ID = "+appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();
      try (var ps = DBConnection.open().prepareStatement(sql)) {
        ps.executeUpdate();
      }
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle(rb.getString("appointmentDeleteTitle"));
      alert.setHeaderText(rb.getString("appointmentDeleteTitle"));
      alert.setContentText(rb.getString("appointmentDeleteTitle")+" "+appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId()+", "+appointmentTable.getSelectionModel().getSelectedItem().getType());
      alert.showAndWait();
      DBConnection.close();
      filteredData.remove(appointmentTable.getSelectionModel().getSelectedItem());
      appointmentTable.setItems(filteredData);
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("appointmentNotFound"));
      alert.showAndWait();
    }
  }
  /**
   * Goes back to MainScreen window
   * @param event on button click
   * @throws IOException if unsuccessful
   */
  @FXML
  void doBack(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MainScreen.fxml"));
    MainScreen controller = new MainScreen(cacheDB);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }
  /**
   * Loads all appointments on to the table.
   * @param event radio change
   */
  @FXML
  void doAllRadio(ActionEvent event) {
    appointmentTable.setItems(cacheDB.getAllAppointments());
  }
  /**
   * loads the appointments in current month
   * @param event radio change
   */
  @FXML
  void doMonthRadio(ActionEvent event) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneMonth = now.plusMonths(1);
    FilteredList<Appointment> data = new FilteredList<>(cacheDB.getAllAppointments());
    data.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(oneMonth);
    });
    appointmentTable.setItems(data);
  }
  /**
   * Loads appointment in current week
   * @param event button click
   */
  @FXML
  void doWeekRadio(ActionEvent event) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeek = now.plusWeeks(1);
    FilteredList<Appointment> data = new FilteredList<>(cacheDB.getAllAppointments());
    data.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(oneWeek);
    });
    appointmentTable.setItems(data);
  }
  /**
   * Checks if the appointment is selected and then the update screen opens. If no appointment selected, shows alert.
   * @param event update button click
   */
  @FXML
  void doUpdateAppointment(ActionEvent event) throws IOException {
    Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/UpdateAppointment.fxml"));
      UpdateAppointment controller = new UpdateAppointment(cacheDB, selectedAppointment);
      loader.setController(controller);
      stage.setScene(new Scene(loader.load()));
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("selectAppointment"));
      alert.showAndWait();
    }
  }
}