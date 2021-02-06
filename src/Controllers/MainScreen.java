package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Models.CacheDB;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for MainScreen
 * @author Luis J. Gimenez
 */

public class MainScreen implements Initializable {
    private final CacheDB cacheDB;
    private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");

    @FXML private AnchorPane rootPane;
    @FXML private Button appointmentButton;
    @FXML private Button customerButton;
    @FXML private Button exitButton;
    @FXML private Button reportsButton;
    @FXML private Label locationLbl;
    @FXML private ToggleGroup radioToggleGroup;
    /**
     * Constructor
     * @param cacheDB critical local data
     */
    public MainScreen(CacheDB cacheDB) {
        this.cacheDB = cacheDB;
    }
    /**
     * Initializes the screen
     * @param url screen url
     * @param resourceBundle text localization resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeLabels();
        locationLbl.setText(this.cacheDB.getLocale().getDisplayCountry());
    }
    /**
     * Add labels in the proper language.
     */
    private void makeLabels() {
        appointmentButton.setText(rb.getString("appointments"));
        reportsButton.setText(rb.getString("reports"));
        customerButton.setText(rb.getString("customers"));
        exitButton.setText(rb.getString("exit"));
    }
    /**
     * Sends user to appointments screen
     * @param event button clicks
     * @throws IOException if unsuccessful
     */
    @FXML
    void doAppointment(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Appointment.fxml"));
        Appointments controller = new Appointments(cacheDB);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
    /**
     * Sends user to customers screen
     * @param event button click
     * @throws IOException if unsuccessful
     */
    @FXML
    void doCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Customer.fxml"));
        Customers controller = new Customers(cacheDB);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }    /**
     * Exits program
     * @param event button click
     */
    @FXML
    void doExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    /**
     * User is sent to the reports page
     * @param event button click
     * @throws IOException if unsuccessful
     */
    @FXML
    void doReports(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Reports.fxml"));
        Reports controller = new Reports(cacheDB);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}