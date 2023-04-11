package com.example.hundirlaflota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    //Cargamos todos los elementos del FXML
    @FXML
    private GridPane gridPane1;
    @FXML
    private GridPane gridPane2;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private AnchorPane anchorPane2;
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text textFinal;
    @FXML
    private Line line1;
    @FXML
    private Line line2;
    @FXML
    private Button reiniciarButton;


    //Creamos las listas de barcos
    ArrayList<Barco> barcos1 = new ArrayList<>();
    ArrayList<Barco> barcos2 = new ArrayList<>();
    int totalBarcos1 = 0;
    int totalBarcos2 = 0;

    String jugador1Nombre, jugador2Nombre;

    /*
    @FXML
    public void initialize(String jugador1TextField, String jugador2TextField) throws InterruptedException {
    }
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File file = new File("jugadores");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            jugador1Nombre = bufferedReader.readLine();
            jugador2Nombre = bufferedReader.readLine();


        } catch (IOException e) {
                throw new RuntimeException(e);
            }

            gridPane1.setDisable(false);
            gridPane2.setDisable(false);
            anchorPane1.setDisable(false);
            anchorPane2.setDisable(false);
            text1.setDisable(false);
            text2.setDisable(false);
            line1.setDisable(false);
            line2.setDisable(false);

            text1.setText(text1.getText() + jugador1Nombre);
            text2.setText(text2.getText() + jugador2Nombre);

            //Cargamos los barcos en la lista de barcos 1
            System.out.println("Barcos del gridPane1:");
            while (totalBarcos1 < 10) {
                boolean repetido1 = false;
                Barco barco = new Barco((int) (Math.random() * 10), (int) (Math.random() * 10));
                for (Barco b : barcos1) {
                    if ((barco.getX() == b.getX() || barco.getX() == b.getX() - 1 || barco.getX() == b.getX() + 1) && (barco.getY() == b.getY() || barco.getY() == b.getY() - 1 || barco.getY() == b.getY() + 1)) {
                        repetido1 = true;
                    }
                }
                if (!repetido1) {
                    barcos1.add(barco);
                    totalBarcos1++;
                    System.out.print(totalBarcos1 + ": " + barco.toString());
                }
            }

            //Cargamos los barcos en la lista de barcos 2
            System.out.println("\nBarcos del gridPane2:");
            while (totalBarcos2 < 10) {
                boolean repetido2 = false;
                Barco barco = new Barco((int) (Math.random() * 10), (int) (Math.random() * 10));
                for (Barco b : barcos2) {
                    if ((barco.getX() == b.getX() || barco.getX() == b.getX() - 1 || barco.getX() == b.getX() + 1) && (barco.getY() == b.getY() || barco.getY() == b.getY() - 1 || barco.getY() == b.getY() + 1)) {
                        repetido2 = true;
                    }
                }
                if (!repetido2) {
                    barcos2.add(barco);
                    totalBarcos2++;
                    System.out.print(totalBarcos2 + ": " + barco.toString());

                }
            }

            //Creamos los botones para el gridpane 1 y les decimos que hacer al ser clicados
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    Button button = new Button();
                    button.setBorder(null);
                    button.setMinSize(44, 44);
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        if (button.getStyle() != "-fx-background-color: #f89292;") {
                            if (button.getStyle() != "-fx-background-color: #9292f8;") {
                                for (Barco b : barcos1) {
                                    if (finalI == b.getX() && finalJ == b.getY()) {
                                        button.setStyle("-fx-background-color: #f89292;");
                                        button.setText("X");
                                        totalBarcos1--;
                                        int fila = GridPane.getRowIndex(button);
                                        int columna = GridPane.getColumnIndex(button);
                                        int[] filasAdyacentes = {fila - 1, fila, fila + 1};
                                        int[] columnasAdyacentes = {columna - 1, columna, columna + 1};
                                        for (int c : filasAdyacentes) {
                                            for (int d : columnasAdyacentes) {
                                                if (c >= 0 && c < 10 && d >= 0 && d < 10) {
                                                    Button botonAdyacente = (Button) gridPane1.getChildren().get(c * 10 + d);
                                                    botonAdyacente.setStyle("-fx-background-color: #9292f8;");
                                                }
                                            }
                                        }
                                        button.setStyle("-fx-background-color: #f89292;");
                                    }
                                }
                                if (button.getStyle() != "-fx-background-color: #f89292;") {
                                    button.setStyle("-fx-background-color: #9292f8;");
                                    turnoJugador1();
                                }
                            }
                        }
                        //System.out.println("Botón clicado en la fila " + finalI + " y la columna " + finalJ);
                        comprobarVictoria();
                    });
                    gridPane1.add(button, i, j);
                    GridPane.setRowIndex(button, i);
                    GridPane.setColumnIndex(button, j);
                }
            }

            //Creamos los botones para el gridpane 2 y les decimos que hacer al ser clicados
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    Button button = new Button();
                    button.setBorder(null);
                    button.setMinSize(44, 44);
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        if (button.getStyle() != "-fx-background-color: #f89292;") {
                            if (button.getStyle() != "-fx-background-color: #9292f8;") {
                                for (Barco b : barcos2) {
                                    if (finalI == b.getX() && finalJ == b.getY()) {
                                        button.setStyle("-fx-background-color: #f89292;");
                                        button.setText("X");
                                        totalBarcos2--;
                                        int fila = GridPane.getRowIndex(button);
                                        int columna = GridPane.getColumnIndex(button);
                                        int[] filasAdyacentes = {fila - 1, fila, fila + 1};
                                        int[] columnasAdyacentes = {columna - 1, columna, columna + 1};
                                        for (int c : filasAdyacentes) {
                                            for (int d : columnasAdyacentes) {
                                                if (c >= 0 && c < 10 && d >= 0 && d < 10) {
                                                    Button botonAdyacente = (Button) gridPane2.getChildren().get(c * 10 + d);
                                                    botonAdyacente.setStyle("-fx-background-color: #9292f8;");

                                                }
                                            }
                                        }
                                        button.setStyle("-fx-background-color: #f89292;");
                                    }
                                }
                                if (button.getStyle() != "-fx-background-color: #f89292;") {
                                    button.setStyle("-fx-background-color: #9292f8;");
                                    turnoJugador2();
                                }
                            }
                        }
                        //System.out.println("Botón clicado en la fila " + finalI + " y la columna " + finalJ);
                        comprobarVictoria();
                    });
                    gridPane2.add(button, i, j);
                    GridPane.setRowIndex(button, i);
                    GridPane.setColumnIndex(button, j);
                }
            }

            //Para que empiece a jugar el jugador uno ejecutamos el metodo turno2()
            turnoJugador1();
        }


        public void turnoJugador1(){ //Desactivamos el panel 1 y activamos el 2 para que juege el jugador 1
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
        for (Node node : gridPane2.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }
        gridPane1.setCursor(Cursor.DEFAULT);
        gridPane2.setCursor(Cursor.CROSSHAIR);
    }

    public void turnoJugador2(){ //Desactivamos el panel 2 y activamos el 1 para que juege el jugador 2
        for (Node node : gridPane2.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }
        gridPane2.setCursor(Cursor.DEFAULT);
        gridPane1.setCursor(Cursor.CROSSHAIR);
    }

    public void comprobarVictoria(){

        /*
        * Se comprueba que se hayan hundido todos los barcos de algun jugador y si es asi
        * ponemos en invisible todos los elementos excepto el textoFinal y mostramos en este
        * que jugador a ganado la partida
        */

        if (totalBarcos1==0){
            gridPane1.setVisible(false);
            gridPane2.setVisible(false);
            anchorPane1.setVisible(false);
            anchorPane2.setVisible(false);
            text1.setVisible(false);
            text2.setVisible(false);
            line1.setVisible(false);
            line2.setVisible(false);
            textFinal.setDisable(false);
            textFinal.setText(jugador2Nombre+" ha hundido la flota de "+jugador1Nombre);
            reiniciarButton.setDisable(false);
            reiniciarButton.setVisible(true);
        } else if (totalBarcos2==0){
            gridPane1.setVisible(false);
            gridPane2.setVisible(false);
            anchorPane1.setVisible(false);
            anchorPane2.setVisible(false);
            text1.setVisible(false);
            text2.setVisible(false);
            line1.setVisible(false);
            line2.setVisible(false);
            textFinal.setDisable(false);
            textFinal.setText(jugador1Nombre+" ha hundido la flota de "+jugador2Nombre);
            reiniciarButton.setDisable(false);
            reiniciarButton.setVisible(true);
        }
    }

    @FXML
    public void reiniciarJuego(){
        //saaa
    }
}