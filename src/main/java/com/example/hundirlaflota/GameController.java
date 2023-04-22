package com.example.hundirlaflota;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class GameController implements Initializable {

    public boolean heAcertado() {
        return otra;
    }

    private boolean otra = false;

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

    //Creamos las listas de barcos
    ArrayList<Barco> listaBarcos1 = new ArrayList<>();
    ArrayList<Barco> listaBarcos2 = new ArrayList<>();
    int totalBarcos1 = 0;
    int totalBarcos2 = 0;

    String jugador1Nombre = null, jugador2Nombre = null;

    public GameController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Creamos los botones para los gridpanes y les decimos que hacer al ser clicados
        cargarGridPanes();

        desactivarTurnos();

    }

    public void cargarBarcosComoHost() throws IOException {

        //Limpiamos los que habia antes
        totalBarcos1=0;

        if (!listaBarcos1.isEmpty()) {
            for (int i = 10; i > 0; i--) {
                listaBarcos1.remove(i - 1);
            }
        }

        //Ponemos los nuevos sin repetirse
        while (totalBarcos1 < 10) {
            boolean repetido1 = false;
            Barco barco = new Barco((int) (Math.random() * 10), (int) (Math.random() * 10));
            for (Barco b : listaBarcos1) {
                if ((barco.getX() == b.getX() || barco.getX() == b.getX() - 1 || barco.getX() == b.getX() + 1) && (barco.getY() == b.getY() || barco.getY() == b.getY() - 1 || barco.getY() == b.getY() + 1)) {
                    repetido1 = true;
                }
            }
            if (!repetido1) {
                listaBarcos1.add(barco);
                totalBarcos1++;
            }
        }

        //Limpiamos los que habia antes
        totalBarcos2=0;
        if (!listaBarcos2.isEmpty()) {
            for (int i = 10; i > 0; i--) {
                listaBarcos2.remove(i - 1);
            }
        }
        //Ponemos los nuevos sin repetirse
        while (totalBarcos2 < 10) {
            boolean repetido2 = false;
            Barco barco = new Barco((int) (Math.random() * 10), (int) (Math.random() * 10));
            for (Barco b : listaBarcos2) {
                if ((barco.getX() == b.getX() || barco.getX() == b.getX() - 1 || barco.getX() == b.getX() + 1) && (barco.getY() == b.getY() || barco.getY() == b.getY() - 1 || barco.getY() == b.getY() + 1)) {
                    repetido2 = true;
                }
            }
            if (!repetido2) {
                listaBarcos2.add(barco);
                totalBarcos2++;
            }
        }

        File file = new File("barcos");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Barco barco1 : listaBarcos1) {
            bufferedWriter.write(barco1.toString()+";");
        }
        bufferedWriter.write("/");
        for (Barco barco2 : listaBarcos2) {
            bufferedWriter.write(barco2.toString()+";");
        }
        bufferedWriter.close();
        fileWriter.close();

        //System.out.println(listaBarcos1.toString());
        //System.out.println(listaBarcos2.toString());
    }

    public void declararBarcosComoInvitado(String barcosDelHost) {
        if (!listaBarcos1.isEmpty()){
            for (Barco b : listaBarcos1) {
                listaBarcos1.remove(b);
            }
        }

        if (!listaBarcos2.isEmpty()){
            for (Barco b : listaBarcos2) {
                listaBarcos2.remove(b);
            }
        }

        String[] parts = barcosDelHost.split("/");
        String[] barcos1 = parts[0].split(";");
        String[] barcos2 = parts[1].split(";");

        for (String s : barcos1 ) {
            String[] barco = s.split(",");
            int x = Integer.parseInt(barco[0])-1;
            int y = Integer.parseInt(barco[1])-1;
            Barco b = new Barco(x,y);
            listaBarcos1.add(b);
        }

        totalBarcos1=10;


        for (String s : barcos2 ) {
            String[] barco = s.split(",");
            int x = Integer.parseInt(barco[0])-1;
            int y = Integer.parseInt(barco[1])-1;
            Barco b = new Barco(x,y);
            listaBarcos2.add(b);
        }

        totalBarcos2=10;


    }

    private void cargarGridPanes() {
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

                            for (Barco b : listaBarcos1) {
                                if (finalI == b.getX() && finalJ == b.getY()) {
                                    otra=true;
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
                                turnoJugador(1);
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

                            for (Barco b : listaBarcos2) {
                                if (finalI == b.getX() && finalJ == b.getY()) {
                                    otra=true;
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
                                turnoJugador(2);
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
    }

    public void turnoJugador(int jugador){
        if (jugador==1){
            for (Node node : gridPane2.getChildren()) {
                if (node instanceof Button) {
                    node.setDisable(false);
                }
            }
            gridPane2.setCursor(Cursor.CROSSHAIR);
        } else if (jugador==2){
            for (Node node : gridPane1.getChildren()) {
                if (node instanceof Button) {
                    node.setDisable(false);
                }
            }
            gridPane1.setCursor(Cursor.CROSSHAIR);
        }

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
            textFinal.setVisible(true);
            textFinal.setText(jugador2Nombre+" ha hundido la flota de "+jugador1Nombre);

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
        }
    }

    public void clicEnBoton(int gridPane, int fila, int columna) {

        otra = false;
        System.out.println("click en el panel: "+gridPane+", en la fila: "+fila+" y en la columna: "+columna);

        fila=fila-1;
        columna=columna-1;

        if (gridPane==1){
            Node nodo = null;
            for (Node n : gridPane1.getChildren()) {
                if (GridPane.getColumnIndex(n) == columna && GridPane.getRowIndex(n) == fila) {
                    nodo = n;
                    break;
                }
            }
            if (nodo != null && nodo instanceof Button) {
                ((Button) nodo).fire();
            }
        } else if (gridPane==2) {
            Node nodo = null;
            for (Node n : gridPane2.getChildren()) {
                if (GridPane.getColumnIndex(n) == columna && GridPane.getRowIndex(n) == fila) {
                    nodo = n;
                    break;
                }
            }
            if (nodo != null && nodo instanceof Button) {
                ((Button) nodo).fire();
            }
        }
    }

    public void actualizarNombres(String jugador1String, String jugador2String) {
        text1.setText("Flota de " + jugador1String);
        text2.setText("Flota de " + jugador2String);
        jugador2Nombre = jugador2String;
        jugador1Nombre=jugador1String;
    }

    public void desactivarTurnos(){
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
        for (Node node : gridPane2.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
        gridPane1.setCursor(Cursor.DEFAULT);
        gridPane2.setCursor(Cursor.DEFAULT);
    }
}