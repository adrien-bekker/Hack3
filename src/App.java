import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage primaryStage) {
        try {
            // Loads up application
            Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
            Scene scene = new Scene(root, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            // If there is an error it prints it and stops the program
            System.out.println(e);
            System.exit(404);
        }
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
