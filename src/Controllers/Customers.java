package Controllers;

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
import Models.CacheDB;
import Models.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for Customer screen
 * @author Luis J. Gimenez
 */
public class Customers implements Initializable {

  private final CacheDB cacheDB;
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");

  @FXML private AnchorPane rootPane;
  @FXML private Button addCustomer;
  @FXML private Button backButton;
  @FXML private Button deleteCustomer;
  @FXML private Button updateCustomer;
  @FXML private Label headTitle;
  @FXML private TableColumn<?, ?> country;
  @FXML private TableColumn<?, ?> customerAddress;
  @FXML private TableColumn<?, ?> customerID;
  @FXML private TableColumn<?, ?> customerName;
  @FXML private TableColumn<?, ?> divisionId;
  @FXML private TableColumn<?, ?> phoneNumber;
  @FXML private TableColumn<?, ?> postalCode;
  @FXML private TableView<Customer> customerTable;
  /**
   * Constructor for the customer screen
   * @param cacheDB Cache data with local data
   */
  public Customers(CacheDB cacheDB) { this.cacheDB = cacheDB; }
  /**
   * Initializes the screen
   * @param url indicates that the protocol to use is http.
   * @param resourceBundle Resource bundles contain locale-specific objects.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    makeLabels();
    customerTable.setItems(cacheDB.getAllCustomers());
  }
  /**
   * Shows label in proper language
   */
  private void makeLabels() {
    customerID.setText(rb.getString("id"));
    customerName.setText(rb.getString("name"));
    customerAddress.setText(rb.getString("address"));
    postalCode.setText(rb.getString("postalCode"));
    phoneNumber.setText(rb.getString("phoneNumber"));
    divisionId.setText(rb.getString("division"));
    country.setText(rb.getString("country"));
    addCustomer.setText(rb.getString("add"));
    updateCustomer.setText(rb.getString("update"));
    deleteCustomer.setText(rb.getString("delete"));
    backButton.setText(rb.getString("back"));
    headTitle.setText(rb.getString("customers"));
  }
  /**
   * Loads the add customer screen
   * @param event add button click
   * @throws IOException if unsuccessful
   */
  @FXML
  void doNewCustomer(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/NewCustomer.fxml"));
    NewCustomer controller = new NewCustomer(cacheDB);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }
  /**
   * Goes back to MainScreen window
   * @param event on back button click
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
   * Sends user to customer update screen
   * @param event update button click
   * @throws IOException if unsuccessful
   */
  @FXML
  private void doUpdateCustomer(ActionEvent event) throws IOException {
    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
    if (selectedCustomer != null) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/UpdateCustomer.fxml"));
      UpdateCustomer controller = new UpdateCustomer(cacheDB, selectedCustomer);
      loader.setController(controller);
      stage.setScene(new Scene(loader.load()));
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("selectCustomer"));
      alert.showAndWait();
    }
  }
  /**
   * Deletes selected customer
   * @param event on delete button click
   */
  @FXML
  void doDeleteCustomer(ActionEvent event) {
    ObservableList<Customer> data = FXCollections.observableArrayList();
    if (customerTable.getSelectionModel().getSelectedItem() != null) {
      data = customerTable.getItems();
      try {
        var sql = "DELETE FROM customers WHERE Customer_ID = "+customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        try (var ps = DBConnection.open().prepareStatement(sql)) {
          ps.executeUpdate();
        }
        data.remove(customerTable.getSelectionModel().getSelectedItem());
        customerTable.setItems(data);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(rb.getString("customerDeleteTitle"));
        alert.setHeaderText(rb.getString("customerDeleteTitle"));
        alert.setContentText(rb.getString("customerDeleteMessage"));
        alert.showAndWait();
      } catch(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(rb.getString("customerDeleteErrorTitle"));
        alert.setContentText(rb.getString("customerDeleteErrorMessage"));
        alert.showAndWait();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("customerNotFound"));
      alert.showAndWait();
    }
  }
}