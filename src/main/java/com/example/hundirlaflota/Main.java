package com.example.hundirlaflota;

import com.example.hundirlaflota.sockets.TcpSocketClient;
import com.example.hundirlaflota.sockets.TcpSocketServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main extends Application {

    @FXML
    private Text textoTitulo;

    @FXML
    private TextField nombreJugadorTextField;

    @FXML
    private GridPane nombreGridPane;

    @FXML
    private Button botonHost;

    @FXML
    private Button botonInvitado;

    @FXML
    private Text textoHost;

    @FXML
    private GridPane hostGridPane;

    @FXML
    private Button botonConfirmarHost;

    @FXML
    private Text textoInvitado;

    @FXML
    private GridPane invitadoGridPane;

    @FXML
    private Button botonConfirmarInvitado;

    @FXML
    private TextField ipTextFieldInvitado;

    @FXML
    private TextField puertoTextFieldInvitado;

    @FXML
    private TextField puertoTextFieldHost;

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

        if (!puertoTextFieldHost.getText().equals("")){

        File file = new File("jugador1");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(nombreJugadorTextField.getText());
        //bufferedWriter.newLine();
        //bufferedWriter.write(jugador2TextField.getText());
        bufferedWriter.close();

        File file2 = new File("puertoHost");
        FileWriter fileWriter2 = new FileWriter(file2);
        BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
        bufferedWriter2.write(puertoTextFieldHost.getText());
        //bufferedWriter.newLine();
        //bufferedWriter.write(jugador2TextField.getText());
        bufferedWriter2.close();


        loader = new FXMLLoader(getClass().getResource("tablero.fxml"));
        root = loader.load();
        gameController=loader.getController();
        scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();

        TcpSocketServer tcpSocketServer = new TcpSocketServer(gameController);
        tcpSocketServer.start();

        primaryStage.setTitle("Hundir la flota - Host");
        }
    }

    public void confirmarComoInvitado(ActionEvent actionEvent) throws Exception {

        /*
        textoTitulo.setDisable(false);
        textoTitulo.setVisible(true);
        jugador1TextField.setDisable(false);
        jugador1TextField.setVisible(true);
        nombreGridPane.setDisable(false);
        nombreGridPane.setVisible(true);
        botonHost.setDisable(false);
        botonHost.setVisible(true);
        botonInvitado.setDisable(false);
        botonInvitado.setVisible(true);

        textoInvitado.setDisable(true);
        textoInvitado.setVisible(false);
        invitadoGridPane.setDisable(true);
        invitadoGridPane.setVisible(false);
        botonConfirmarInvitado.setDisable(true);
        botonConfirmarInvitado.setVisible(false);

        */


        if (!puertoTextFieldInvitado.getText().equals("")
                && !ipTextFieldInvitado.getText().equals("")){


            File file1 = new File("jugador2");
            FileWriter fileWriter1 = new FileWriter(file1);
            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
            bufferedWriter1.write(nombreJugadorTextField.getText());
            //bufferedWriter.newLine();
            //bufferedWriter.write(jugador2TextField.getText());
            bufferedWriter1.close();

            File file = new File("puertoCliente");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(ipTextFieldInvitado.getText());
            bufferedWriter.newLine();
            bufferedWriter.write(puertoTextFieldInvitado.getText());
            bufferedWriter.close();

            loader = new FXMLLoader(getClass().getResource("tablero.fxml"));
            root = loader.load();
            gameController=loader.getController();
            scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();

            TcpSocketClient invitado = new TcpSocketClient(gameController);
            invitado.start();

            primaryStage.setTitle("Hundir la flota - Invitado");
        }
    }

    public void conectarseComoHost(){
        if (!nombreJugadorTextField.getText().equals("")){
            textoTitulo.setVisible(false);
            textoTitulo.setDisable(true);
            nombreJugadorTextField.setVisible(false);
            nombreJugadorTextField.setDisable(true);
            nombreGridPane.setVisible(false);
            nombreGridPane.setDisable(true);
            botonHost.setVisible(false);
            botonHost.setDisable(true);
            botonInvitado.setVisible(false);
            botonInvitado.setDisable(true);

            textoHost.setDisable(false);
            textoHost.setVisible(true);
            hostGridPane.setDisable(false);
            hostGridPane.setVisible(true);
            botonConfirmarHost.setDisable(false);
            botonConfirmarHost.setVisible(true);
        }
    }
    public void conectarseComoInvitado(){

        if (!nombreJugadorTextField.getText().equals("")){
            textoTitulo.setVisible(false);
            textoTitulo.setDisable(true);
            nombreJugadorTextField.setVisible(false);
            nombreJugadorTextField.setDisable(true);
            nombreGridPane.setVisible(false);
            nombreGridPane.setDisable(true);
            botonHost.setVisible(false);
            botonHost.setDisable(true);
            botonInvitado.setVisible(false);
            botonInvitado.setDisable(true);

            textoInvitado.setDisable(false);
            textoInvitado.setVisible(true);
            invitadoGridPane.setDisable(false);
            invitadoGridPane.setVisible(true);
            botonConfirmarInvitado.setDisable(false);
            botonConfirmarInvitado.setVisible(true);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}