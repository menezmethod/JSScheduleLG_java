package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import Models.Customer;
import Utility.DBConnection;

/**
 * Controller for new customer screen
 * @author Luis J. Gimenez
 */
public class NewCustomer implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
  private final CacheDB cacheDB;
  private List<String> countries = new ArrayList<>();
  private List<String> divisions = new ArrayList<>();

  @FXML private AnchorPane rootPane;
  @FXML private Button cancelCustomer;
  @FXML private Button saveCustomer;
  @FXML private ComboBox<String> countryTab;
  @FXML private ComboBox<String> divisionBoxIn;
  @FXML private Label addressLbl;
  @FXML private Label countryLbl;
  @FXML private Label customerIdLbl;
  @FXML private Label customerNameLbl;
  @FXML private Label divisionBoxLbl;
  @FXML private Label headTitle;
  @FXML private Label phoneLbl;
  @FXML private Label postalCodeLbl;
  @FXML private TextField addressIn;
  @FXML private TextField customerIdIn;
  @FXML private TextField customerNameIn;
  @FXML private TextField phoneIn;
  @FXML private TextField postalCodeIn;

  /**
   * Controller for customer
   * @param cacheDB Cache data with local data
   */
  public NewCustomer(CacheDB cacheDB) {
    this.cacheDB = cacheDB;
  }
  /**
   * Initializes the screen
   * @param url url indicates that the protocol to use is http
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    makeLabels();
    makeBoxes();
  }
  /**
   * Loads ComboBoxes
   */
  private void makeBoxes() {
    this.countryTab.getItems().clear();
    this.countries.clear();
    String s = "SELECT Country FROM countries";
    String s1 = "SELECT Division FROM first_level_divisions d";
    try {
      Statement statement = DBConnection.open().createStatement();
      Statement statement1 = DBConnection.open().createStatement();
      ResultSet resultSet = statement.executeQuery(s);
      ResultSet resultSet1 = statement1.executeQuery(s1);
      while (resultSet.next()) {
        String country = resultSet.getString("Country");
        this.countryTab.getItems().add(country);
        this.countries.add(country);
      }
      while (resultSet1.next()) {
        String division = resultSet1.getString("Division");
        this.divisionBoxIn.getItems().add(division);
        this.divisions.add(division);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Add labels in the proper language.
   */
  private void makeLabels() {
    customerIdLbl.setText(rb.getString("id"));
    customerNameLbl.setText(rb.getString("name"));
    addressLbl.setText(rb.getString("address"));
    postalCodeLbl.setText(rb.getString("postalCode"));
    phoneLbl.setText(rb.getString("phoneNumber"));
    countryLbl.setText(rb.getString("country"));
    divisionBoxLbl.setText(rb.getString("division"));
    customerIdIn.setPromptText(rb.getString("disabledAutoGen"));
    customerNameIn.setPromptText(rb.getString("customerName"));
    addressIn.setPromptText(rb.getString("address"));
    postalCodeIn.setPromptText(rb.getString("postalCode"));
    phoneIn.setPromptText(rb.getString("phoneNumber"));
    saveCustomer.setText(rb.getString("save"));
    cancelCustomer.setText(rb.getString("cancel"));
    headTitle.setText(rb.getString("addCustomer"));
  }
  /**
   * Increments and creates a new id. so the database doesnt have to be sorted
   * @return id
   * @throws SQLException if unsuccessful
   */
  public static int getNewId() throws SQLException {
    String sql = "SELECT * FROM customers";
    Statement stmt = DBConnection.open().createStatement();
    ResultSet result = stmt.executeQuery(sql);
    int count = 0;
    while (result.next()) {
      if (result.getInt("Customer_ID") > count) count = result.getInt("Customer_ID");
    }
    DBConnection.close();
    return count + 1;
  }
  /**
   * Saves customer
   * @param event save button click
   */
  @FXML
  void doSave(ActionEvent event) throws SQLException, IOException {
    boolean valid = true;
    int newId = getNewId();

    String customerNameText = customerNameIn.getText();
    String addressText = addressIn.getText();
    String postalCodeText = postalCodeIn.getText();
    String phoneText = phoneIn.getText();
    java.sql.Date current_date = new java.sql.Date(System.currentTimeMillis());
    String Created_By = cacheDB.getUser();
    Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
    String Last_Updated_By = cacheDB.getUser();
    String divisionText = divisionBoxIn.getSelectionModel().getSelectedItem();
    String countryText = countryTab.getSelectionModel().getSelectedItem();

    if (customerNameText.equals("") || addressText.equals("") || postalCodeText.equals("") || phoneText.equals("") || current_date == null ||
            Created_By.equals("") || current_date == null || Last_Updated_By.equals("") || divisionText == null || countryText.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      Customer customer = new Customer(newId, customerNameText, addressText, postalCodeText, phoneText, current_date, Created_By, current_time, Last_Updated_By, divisionText, countryText);
      try {
        String sqlOne = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionText + "';";
        Statement statement = DBConnection.open().createStatement();
        ResultSet resultOne = statement.executeQuery(sqlOne);
        int divisionId = 0;
        while (resultOne.next()) {
          divisionId = resultOne.getInt("Division_ID");
        }
        String sqlTwo = "INSERT INTO customers VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement insert = DBConnection.open().prepareStatement(sqlTwo);
        insert.setInt(1, newId);
        insert.setString(2, customerNameText);
        insert.setString(3, addressText);
        insert.setString(4, postalCodeText);
        insert.setString(5, phoneText);
        insert.setDate(6, current_date);
        insert.setString(7, Created_By);
        insert.setTimestamp(8, current_time);
        insert.setString(9, Last_Updated_By);
        insert.setInt(10, divisionId);
        try (var ps = insert) {
          ps.executeUpdate();
        }
        cacheDB.newCustomer(customer);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));

      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Customer.fxml"));
        Customers controller = new Customers(cacheDB);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
      } else {
        alert.showAndWait();
      }
    }
  }
  /**
   * Updates ComboBox
   * @param event update button click
   */
  @FXML
  void updateCombo(ActionEvent event) {
    String selectedCountry = countryTab.getValue();
    String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
    try {
      Statement statement = DBConnection.open().createStatement();
      ResultSet resultOne = statement.executeQuery(sql);
      this.divisionBoxIn.getItems().removeAll(this.divisions);
      this.divisions.clear();
      while (resultOne.next()) {
        String division = resultOne.getString("Division");
        this.divisionBoxIn.getItems().add(division);
        this.divisions.add(division);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Cancels customer update and sends user back to customer screen.
   * @param event cancel button click
   * @throws IOException if unsuccessful
   */
  @FXML
  void doCancel(ActionEvent event) throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(rb.getString("cancelConfirmation"));
    alert.setContentText(rb.getString("cancelCustomerMessage"));
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Customer.fxml"));
      Customers controller = new Customers(cacheDB);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
  }
}
