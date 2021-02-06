package Models;

import Controllers.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Loads login screen
 * @author Luis J. Gimenez
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        CacheDB main = new CacheDB();
        Utility.CacheDB.feedData(main);
        try {
            ResourceBundle rb = ResourceBundle.getBundle("Lang/rb");
            Locale local = Locale.getDefault();
            main.updateLocale(local);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/Login.fxml"));
        Login controller = new Login(main);
        loader.setController(controller);
        primaryStage.setTitle("John and Smiths Scheduling System");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }
    /**
     * Models.Main function. calls start.
     * @param args argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}