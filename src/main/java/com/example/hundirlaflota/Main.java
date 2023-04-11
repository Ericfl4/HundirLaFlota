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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ResourceBundle;
public class Main extends Application {

    @FXML
    private TextField jugador1TextField;
    @FXML
    private TextField jugador2TextField;

    GameController gameController;
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
        File file = new File("jugadores");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(jugador1TextField.getText());
        bufferedWriter.newLine();
        bufferedWriter.write(jugador2TextField.getText());
        bufferedWriter.close();
        loader = new FXMLLoader(getClass().getResource("tablero.fxml"));
        root = loader.load();
        gameController=loader.getController();
        scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
