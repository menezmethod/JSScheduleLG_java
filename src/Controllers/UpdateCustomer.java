package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
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
 * Controller for updating a customer
 * @author Luis J. Gimenez
 */
public class UpdateCustomer implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
  private final CacheDB cacheDB;
  private final Customer customer;
  private List<String> countries = new ArrayList<>();
  private List<String> divisions = new ArrayList<>();

  @FXML private AnchorPane rootPane;
  @FXML private Button cancelBtn;
  @FXML private Button saveBtn;
  @FXML private ComboBox<String> countryBoxIn;
  @FXML private ComboBox<String> divisionBoxIn;
  @FXML private Label addressLbl;
  @FXML private Label countryLabel;
  @FXML private Label customerIdLbl;
  @FXML private Label customerNameLabel;
  @FXML private Label divisionBoxLbl;
  @FXML private Label modifyCustomerTitle;
  @FXML private Label phoneLbl;
  @FXML private Label postalCodeLabel;
  @FXML private TextField addressInput;
  @FXML private TextField customerIdInput;
  @FXML private TextField customerNameInput;
  @FXML private TextField phoneIn;
  @FXML private TextField postalCodeInput;
  /**
   * constructor for update customer
   * @param cacheDB Data store
   * @param customer Selected customer
   */
  public UpdateCustomer(CacheDB cacheDB, Customer customer) {
    this.cacheDB = cacheDB;
    this.customer = customer;
  }
  /**
   * Initializes screen
   * @param url indicates that the protocol to use is http.
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    makeLabels();
    makeBoxes();
    setData();
  }
  /**
   * Sets input fields with existing data
   */
  private void setData() {
    addressInput.setText(customer.getAddress());
    countryBoxIn.setValue(customer.getCountry());
    customerIdInput.setText(Integer.toString(customer.getCustomerId()));
    customerNameInput.setText(customer.getCustomerName());
    divisionBoxIn.setValue(customer.getDivision());
    phoneIn.setText(customer.getPhone());
    postalCodeInput.setText(customer.getPostalCode());
  }
  /**
   * Loads ComboBoxes
   */
  private void makeBoxes() {
    this.countryBoxIn.getItems().clear();
    this.countries.clear();
    String sqlOne = "SELECT Country_ID, Country FROM countries";
    String sqlTwo = "SELECT Division_ID, Division FROM first_level_divisions d";
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      Statement stmtTwo = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sqlOne);
      ResultSet resultTwo = stmtTwo.executeQuery(sqlTwo);
      while (resultOne.next()) {
        String country = resultOne.getString("Country");
        this.countryBoxIn.getItems().add(country);
        this.countries.add(country);
      }
      while (resultTwo.next()) {
        String division = resultTwo.getString("Division");
        this.divisionBoxIn.getItems().add(division);
        this.divisions.add(division);
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }
   /**
   * Initializes labels with correct language
   */
  private void makeLabels() {
    addressInput.setPromptText(rb.getString("address"));
    addressLbl.setText(rb.getString("address"));
    cancelBtn.setText(rb.getString("cancel"));
    countryLabel.setText(rb.getString("country"));
    customerIdInput.setPromptText(rb.getString("disabledAutoGen"));
    customerIdLbl.setText(rb.getString("id"));
    customerNameInput.setPromptText(rb.getString("customerName"));
    customerNameLabel.setText(rb.getString("name"));
    divisionBoxLbl.setText(rb.getString("division"));
    phoneIn.setPromptText(rb.getString("phoneNumber"));
    phoneLbl.setText(rb.getString("phoneNumber"));
    postalCodeInput.setPromptText(rb.getString("postalCode"));
    postalCodeLabel.setText(rb.getString("postalCode"));
    saveBtn.setText(rb.getString("save"));
  }
  /**
   * Saves customer with the modified information
   * @param event on save button click
   */
  @FXML
  void doSave(ActionEvent event) throws IOException {
    boolean valid = false;
    int nCustomerId = Integer.parseInt(customerIdInput.getText());
    String nCustomerName = customerNameInput.getText();
    String nAddress = addressInput.getText();
    String nPostalCode = postalCodeInput.getText();
    String nPhone = phoneIn.getText();
    String Last_Updated_By = cacheDB.getUser();
    String divisionText = divisionBoxIn.getSelectionModel().getSelectedItem();
    String countryText = countryBoxIn.getSelectionModel().getSelectedItem();
    Timestamp currentTime = new Timestamp( new Date().getTime());
    if (nCustomerName.equals("") || nAddress.equals("") || nPostalCode.equals("")
            || nPhone.equals("") || currentTime == null || Last_Updated_By.equals("") || divisionText.equals("")
            || countryText.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      try {
        String rq1 = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionText + "';";
        Statement st1 = DBConnection.open().createStatement();
        ResultSet rs1 = st1.executeQuery(rq1);
        int divisionId = 0;
        while (rs1.next()) {
          divisionId = rs1.getInt("Division_ID");
        }
        String updateSql = "UPDATE customers SET Customer_Name = ?, ADDRESS = ?, Phone = ?, Postal_Code = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement insert = DBConnection.open().prepareStatement(updateSql);
        insert.setString(1, nCustomerName);
        insert.setString(2, nAddress);
        insert.setString(3, nPhone);
        insert.setString(4, nPostalCode);
        insert.setTimestamp(5, currentTime);
        insert.setString(6, Last_Updated_By);
        insert.setInt(7, divisionId);
        insert.setInt(8, nCustomerId);
        customer.setId(nCustomerId);
        customer.setName(nCustomerName);
        customer.setAddress(nAddress);
        customer.setPostalCode(nPostalCode);
        customer.setPhone(nPhone);
        customer.setLastUpdate(currentTime);
        customer.setLast_Updated_By(Last_Updated_By);
        customer.setDivision(divisionText);
        customer.setCountry(countryText);
        try (var ps = insert) {
          ps.executeUpdate();
        }
        valid = true;
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Customer.fxml"));
        Customers controller = new Customers(cacheDB);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
      }
    }
  }
  /**
   * Updates division's ComboBox based on the selected country.
   * @param event change country in ComboBox
   */
  @FXML
  private void updateCombo(ActionEvent event) {
    String selectCountry = countryBoxIn.getValue();
    this.divisionBoxIn.getItems().removeAll(this.divisions);
    this.divisions.clear();
    String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectCountry+"';";
    try {
      Statement statement = DBConnection.open().prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        String division = resultSet.getString("Division");
        this.divisionBoxIn.getItems().add(division);
        this.divisions.add(division);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Cancels modification of customer and sends user back to customer window
   * @param event cancel button click
   * @throws IOException if unsuccessful
   */
  @FXML
  private void doCancel(ActionEvent event) throws IOException {
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