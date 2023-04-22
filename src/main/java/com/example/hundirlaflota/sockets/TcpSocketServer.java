package com.example.hundirlaflota.sockets;

import com.example.hundirlaflota.GameController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpSocketServer extends Thread{

    private Scanner sc = new Scanner(System.in);
    private boolean end = false;
    private GameController gameController;
    private boolean inicioJuego = true;
    boolean heAcertado;
    public TcpSocketServer(GameController gameController) {
        this.gameController = gameController;
    }

    //Metodo inicial
    public void listen(int port){
        System.out.println(port);
        ServerSocket serverSocket=null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(!end){
                clientSocket = serverSocket.accept();
                System.out.println("Connexió amb: " + clientSocket.getInetAddress());
                connection(clientSocket);
            }
            if(serverSocket!=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connection(Socket clientSocket){
        String clientMessage="";
        BufferedReader in=null;
        PrintStream out=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out= new PrintStream(clientSocket.getOutputStream());
            do{
                String dataToSend = "";
                if (inicioJuego){
                    iniciarJuego(in, out);

                } else {
                    if (getJugada(in)){
                        closeClient(clientSocket);
                    }
                    if (sendJugada(out)){
                        closeClient(clientSocket);
                    }
                }
            }while((clientMessage)!=null);
        } catch (IOException ex) {
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void iniciarJuego(BufferedReader in, PrintStream out) throws IOException {
        String dataToSend;
        //Se crean los barcos y se envian al tablero del cliente para tener los mismos
        System.out.println("Empezando juego");
        gameController.cargarBarcosComoHost();
        File file = new File("barcos");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        dataToSend = bufferedReader.readLine();
        out.println(dataToSend);
        out.flush();

        //Tambien se pasa el nombre del jugador que actua como host
        File file2 = new File("jugador1");
        FileReader fileReader2 = new FileReader(file2);
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        dataToSend = bufferedReader2.readLine();
        out.println(dataToSend);
        out.flush();

        //Leemos el nombre del jugador invitado que nos envia el cliente
        dataToSend= in.readLine();
        String nombre= in.readLine();

        //Con los dos nombres actualizamos el tablero
        gameController.actualizarNombres(dataToSend, nombre);
        inicioJuego=false;
        System.out.println("Juego preparado - Host");
        gameController.desactivarTurnos();
    }

    private boolean getJugada(BufferedReader in) throws IOException {
        //Con el juego ya empezado se espera la jugada del cliente y se ejecuta la misma en el tablero del host, si ha acertado se espera otra jugada
        heAcertado=true;
        String clientMessage;
        while (heAcertado && !inicioJuego) {
            System.out.println("Espero una jugada del cliente");
            clientMessage = in.readLine();

            if (!clientMessage.equals("adios")){
                System.out.println("Me ha enviado una jugada el cliente");
                String[] jugada = clientMessage.split(",");
                int x = Integer.parseInt(jugada[0]);
                int y = Integer.parseInt(jugada[1]);
                gameController.turnoJugador(2);
                gameController.clicEnBoton(1, x, y);
                gameController.desactivarTurnos();
                System.out.println("Su jugada a sido: " + x + "," + y + " y la he enviado al gridPane2");
                heAcertado=gameController.heAcertado();
            } else {
                System.out.println("El invitado no quiere seguir jugando");
                return true;
            }
        }
        return false;
    }

    private boolean sendJugada(PrintStream out) {
        //Y ahora enviamos la nuestra y si acertamos pues tenemos otro turno para enviar jugada
        heAcertado=true;
        while (heAcertado && !inicioJuego) {
            System.out.println("Me toca enviar jugada al cliente (Ejemplo: 1,2 donde 1 sería la columna y 2 la fila)");
            String jugada = sc.next();
            if (!jugada.equals("adios")) {
                String[] jugadaMia = jugada.split(",");
                int x = Integer.parseInt(jugadaMia[0]);
                int y = Integer.parseInt(jugadaMia[1]);
                gameController.turnoJugador(1);
                gameController.clicEnBoton(2, x, y);
                gameController.desactivarTurnos();
                out.println(jugada);
                out.flush();
                System.out.println("Le he enviado esta: " + jugada);
                heAcertado = gameController.heAcertado();
            } else {
                System.out.println("Has cortado conexión con el invitado");
                out.println(jugada);
                out.flush();
                return true;
            }
        }
        return false;
    }

    //Metodo para cerrar conexión con el invitado
    private void closeClient(Socket clientSocket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(clientSocket!=null && !clientSocket.isClosed()){
                if(!clientSocket.isInputShutdown()){
                    clientSocket.shutdownInput();
                }
                if(!clientSocket.isOutputShutdown()){
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    //Metodo para iniciar el Thread del server
    public void run() {
        File file = new File("puertoHost");
        FileReader fileReader = null;
        int port;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            port = Integer.parseInt(bufferedReader.readLine());
            //bufferedWriter.newLine();
            //bufferedWriter.write(jugador2TextField.getText());
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listen(port);
    }
}