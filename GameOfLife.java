//THIS IS THE MAIN CLASS
//Author: Sergei Vilensky
//Date: 14.11.21
//Game's constants and default parameters can be changed in Constants.java file

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOfLife extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GameOfLife.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Game of life");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
