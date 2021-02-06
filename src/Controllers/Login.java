package Controllers;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Utility.DBConnection;
import Models.Appointment;
import Models.CacheDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for Login screen
 * @author Luis J. Gimenez
 */
public class Login implements Initializable {

  private final CacheDB cacheDB;
  private ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");

  @FXML private Button loginButton;
  @FXML private Button exitButton;
  @FXML private Label locationLbl;
  @FXML private Label passwordLabel;
  @FXML private Label usernameLabel;
  @FXML private Label welcomeLogin;
  @FXML private PasswordField passwordInput;
  @FXML private TextField usernameInput;
  /**
   * Initializes pane with correct language
   * @param url the proper fxml
   * @param resourceBundle language
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    locationLbl.setText(this.cacheDB.getLocale().getDisplayCountry());
    loginButton.setText(rb.getString("login"));
    exitButton.setText(rb.getString("exit"));
    usernameLabel.setText(rb.getString("username"));
    usernameInput.setPromptText(rb.getString("username"));
    passwordLabel.setText(rb.getString("password"));
    passwordInput.setPromptText(rb.getString("password"));
    welcomeLogin.setText(rb.getString("welcomeLogin"));
  }
  /**
   * Constructor - sets the cache
   * @param cacheDB caches local data for faster access
   */
  public Login(CacheDB cacheDB) {
    this.cacheDB = cacheDB;
  }
  /**
   * Validates user / pass and logs user in. Logs the activity in auth_log.txt
   * @param event
   */
  @FXML
  void doLogin(ActionEvent event) throws IOException {
    boolean valid = false;
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    String loginAct = "";
    Timestamp time = new Timestamp(new Date(System.currentTimeMillis()).getTime());
    try {
      String sql = "SELECT * FROM users WHERE User_Name = '" + username + "';";
      Statement stmt = DBConnection.open().createStatement();
      ResultSet result = stmt.executeQuery(sql);
      while (result.next()) {
        String user = result.getString("User_Name");
        String pass = result.getString("Password");
        if (user.matches(username) && password.matches(pass)) {
          valid = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (valid) {
      apptAlert();
      loginAct = time + ": successful login by " + username + "\n";
      cacheDB.newUser(username);

      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MainScreen.fxml"));
      MainScreen controller = new MainScreen(cacheDB);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
    else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle(rb.getString("invalidLoginTitle"));
      alert.setHeaderText(rb.getString("invalidLoginMessage"));
      alert.showAndWait();
      loginAct = time+": fail by " + username + "\n";
    }
    /**
     * Print to auth_log.txt
     */
    PrintWriter pw = new PrintWriter(new FileOutputStream(
            new File("auth_log.txt"), true));
    pw.append(loginAct);
    pw.close();
  }
  /**
   * Alerts the customer that if meeting is 15 minutes out'
   * Lambda function filters results between now and 15 minutes.
   */
  private void apptAlert() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime in15 = now.plusMinutes(15);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd KK:mm:ss a");
    FilteredList<Appointment> processedData = new FilteredList<>(cacheDB.getAllAppointments());
    /**
     * Lambda function filters results between now and 15 minutes.
     */
    processedData.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(in15);
    });
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    if (!processedData.isEmpty()) {
      StringBuilder appointment_message = new StringBuilder(rb.getString("appointmentAlertMessage") + "\n");
      for (Appointment filteredDatum : processedData) {
        appointment_message.append(rb.getString("appointment")).append(" ").append(filteredDatum.getAppointmentId()).append(": ").append(filteredDatum.getLocalStart().toLocalDateTime().format(formatter)).append(" \n");
      }
      alert.setTitle(rb.getString("appointmentAlertTitle"));
      alert.setHeaderText(rb.getString("appointmentAlertTitle"));
      alert.setContentText(appointment_message.toString());
    } else {
      alert.setTitle(rb.getString("noAppointmentTitle"));
      alert.setHeaderText(rb.getString("noAppointmentTitle"));
      alert.setContentText(rb.getString("noAppointmentMessage")+ "\n");
    }
    alert.showAndWait();
  }
  /**
   * Exits the program
   * @param event exit button click
   */
  @FXML
  void doExit(ActionEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }
}