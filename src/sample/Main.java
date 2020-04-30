package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static sample.ExpLoader.deleteDirectory;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
        primaryStage.setMinHeight(640);
        primaryStage.setMinWidth(930);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            if (ExpLoader.temp == null) {
                System.exit(0);

            } else {
                deleteDirectory(ExpLoader.temp);
            }

        });

    }


    public static void main(String[] args) {
        launch(args);

    }
}
