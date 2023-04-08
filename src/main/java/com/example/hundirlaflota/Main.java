package com.example.hundirlaflota;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;
public class Main extends Application {

    @FXML
    private TextField jugador1TextField;
    @FXML
    private TextField jugador2TextField;

    GameController gameController = new GameController();
    Parent root;
    Scene scene;
    FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("nombres.fxml"));
        root = loader.load();
        primaryStage.setTitle("Hundir la flota");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void empezarJuegoButtonAction(ActionEvent actionEvent) throws Exception {
        loader = new FXMLLoader(getClass().getResource("tablero.fxml"));
        loader.setController(gameController);
        root = loader.load();
        scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
        jugador1TextField.getText();
        gameController.initialize(jugador1TextField.getText(), jugador2TextField.getText());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
