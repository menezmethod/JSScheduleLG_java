package Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.TextStyle;
import java.util.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Models.Appointment;
import Models.CacheDB;
import Models.Report;

/**
 * Controller for the Reports screen
 */
public class Reports implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
  private final CacheDB cacheDB;
  ObservableList<Report> monthList, locationList;
  private List<String> contacts = new ArrayList<>();

  @FXML private AnchorPane rootPane;
  @FXML private Button backButton;
  @FXML private ComboBox<String> contactBoxIn;
  @FXML private Label headTitle;
  @FXML private TableColumn<Appointment, Date> customerId;
  @FXML private TableColumn<Appointment, Date> endTime;
  @FXML private TableColumn<Appointment, Date> startTime;
  @FXML private TableColumn<Appointment, Integer> appointmentId;
  @FXML private TableColumn<Appointment, Integer> title;
  @FXML private TableColumn<Appointment, String> description;
  @FXML private TableColumn<Appointment, String> type;
  @FXML private TableColumn<Report, Integer> colLocationTotal;
  @FXML private TableColumn<Report, Integer> colMonthTotal;
  @FXML private TableColumn<Report, Integer> colTypeTotal;
  @FXML private TableColumn<Report, String> colLocation;
  @FXML private TableColumn<Report, String> colMonth;
  @FXML private TableColumn<Report, String> colType;
  @FXML private TableView<Appointment> appointmentTable;
  @FXML private TableView<Report> locationTableView;
  @FXML private TableView<Report> monthTotalTable;
  @FXML private TableView<Report> typeTotalTable;
  @FXML private Tab locationReports;
  @FXML private Tab ReportsByMonth;
  @FXML private Tab ReportsContactByMonth;
  @FXML private Tab ReportsSchedule;
  /**
   * Controller / constructor
   * @param cacheDB caches local data for faster access
   */
  public Reports(CacheDB cacheDB) {
    this.cacheDB = cacheDB;
  }
  /**
   * Initializes database data tables and the proper language.
   * @param url indicates that the protocol to use is http.
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentId.setText(rb.getString("id"));
    title.setText(rb.getString("title"));
    description.setText(rb.getString("description"));
    type.setText(rb.getString("type"));
    startTime.setText(rb.getString("startTime"));
    endTime.setText(rb.getString("endTime"));
    customerId.setText(rb.getString("customerId"));
    backButton.setText(rb.getString("back"));
    headTitle.setText(rb.getString("reports"));

    makeMonthList();
    makeLocationList();
    makeAppointmentContacts();
  }
  /**
   * Load all contacts
   * Lambda's used to filter by contact
   */
  private void makeAppointmentContacts() {
    this.contactBoxIn.getItems().clear();
    this.contacts.clear();
    contactBoxIn.getItems().add(rb.getString("any"));
    contactBoxIn.setValue(rb.getString("any"));
    for (int i = 0; i < cacheDB.getAllContacts().size(); i++ ) {
      contactBoxIn.getItems().add(cacheDB.getAllContacts().get(i).getName());
      this.contacts.add(cacheDB.getAllContacts().get(i).getName());
    }
    appointmentTable.setItems(cacheDB.getAllAppointments());
  }
  /**
   * Load the Location table
   * `Lambda's used to filter by contact
   */
  private void makeLocationList() {
    locationList = FXCollections.observableArrayList();
    Map<String, Integer> locationMap = new HashMap<String, Integer>();
    String location = "";
    int count;
    for (int i = 0; i < cacheDB.getAllAppointments().size(); i++) {
      location = cacheDB.getAllAppointments().get(i).getLocation();
      count = 0;
      if (!locationMap.containsKey(location)) {
        for (int j = 0; j < cacheDB.getAllAppointments().size(); j++) {
          if (cacheDB.getAllAppointments().get(j).getLocation().equals(location)) {
            count++;
          }
        }
        locationMap.put(location, count);
      }
    }
    for (Map.Entry<String, Integer> mapElement : locationMap.entrySet()) {
      locationList.add(new Report(
              new ReadOnlyObjectWrapper<>(mapElement.getValue()),
              new ReadOnlyStringWrapper(mapElement.getKey())
      ));
    }
    /**
     * Lambda's filtering by contact.
     */
    colLocation.setCellValueFactory(cellData -> {
      return cellData.getValue().getAttribute();
    });
    colLocationTotal.setCellValueFactory(cellData -> {
      return cellData.getValue().getCount();
    });
    locationTableView.setItems(locationList);
  }
  /**
   * Loads the Month's table
   */
  private void makeMonthList() {
    monthList = FXCollections.observableArrayList();
    Map<String, Integer> typesInMonth = new HashMap<String, Integer>();
    String month = "";
    for (int i = 0; i < cacheDB.getAllAppointments().size(); i++) {
      Locale locale = Locale.getDefault();
      month = cacheDB.getAllAppointments().get(i).getLocalStart().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, locale);
      if (!typesInMonth.containsKey(month)) {
        ArrayList<String> typeList = new ArrayList<>()  ;
        for (int j = 0; j < cacheDB.getAllAppointments().size(); j++) {
          if (cacheDB.getAllAppointments().get(j).getLocalStart().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, locale).equals(month)) {
            if (!typeList.contains(cacheDB.getAllAppointments().get(j).getType())) {
              typeList.add(cacheDB.getAllAppointments().get(j).getType());
            }
          }
        }
        typesInMonth.put(month, typeList.size());
      }
    }
    for (Map.Entry<String, Integer> mapElement : typesInMonth.entrySet()) {
      monthList.add(new Report(
              new ReadOnlyObjectWrapper<>(mapElement.getValue()),
              new ReadOnlyStringWrapper(mapElement.getKey())
      ));
    }
    colMonth.setCellValueFactory(cellData -> {
      return cellData.getValue().getAttribute();
    });
    colTypeTotal.setCellValueFactory(cellData -> {
      return cellData.getValue().getCount();
    });
    typeTotalTable.setItems(monthList);
  }
  /**
   * Toggles contact ComboBox. Returns correct appointment by type
   * Lambda allows the data to be filtered by contact
   * @param event ComboBox
   */
  @FXML
  void doContactBox(ActionEvent event) {
    String selectedContact = contactBoxIn.getSelectionModel().getSelectedItem();
    FilteredList<Appointment> filteredData = new FilteredList<>(cacheDB.getAllAppointments());
    /**
     * Lambda allows the data to be filtered by contact
     */
    filteredData.setPredicate(row -> {
      Integer contactId = row.getContactId();
      return selectedContact.equals("All") || selectedContact.equals(cacheDB.getAllContacts().get(contactId - 1).getName());
    });
    if (selectedContact.equals("All")) {
      appointmentTable.setItems(cacheDB.getAllAppointments());
    }
    else {
      appointmentTable.setItems(filteredData);
    }
  }
  /**
   * Sends user back to appointment page.
   * @param event on back button click
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
}