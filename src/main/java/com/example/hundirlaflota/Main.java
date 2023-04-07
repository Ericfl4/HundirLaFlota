package com.example.hundirlaflota;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GameController controller = new GameController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tablero.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Hundir la flota");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
