package com.example.hundirlaflota.sockets;

import com.example.hundirlaflota.GameController;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpSocketClient extends Thread{

    private Scanner sc = new Scanner(System.in);
    private boolean end = false;
    private GameController gameController;
    private boolean inicioJuego = true;
    boolean heAcertado;
    public TcpSocketClient(GameController gameController) {
        this.gameController = gameController;
    }

    //Metodo principal
    public void connection(String address, int port) {
        String serverData = "";
        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            while(!end){
                if (inicioJuego) {
                    iniciarJuego(in, out);
                } else {
                    if (sendJugada(out)){
                        close(socket);
                    }
                    if (getJugada(in)){
                        close(socket);
                    }
                }
            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.printf("Error de connexió. No existeix el host, %s", ex);
        } catch (IOException ex) {
            System.out.printf("Error de connexió indefinit, %s", ex);
        }
    }

    private void iniciarJuego(BufferedReader in, PrintStream out) throws IOException {
        String serverData;
        System.out.println("Empezando juego");
        serverData = in.readLine();
        gameController.declararBarcosComoInvitado(serverData);
        serverData = in.readLine();
        File file = new File("jugador2");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String nombre = bufferedReader.readLine();
        gameController.actualizarNombres(serverData, nombre);
        out.println(serverData);
        out.flush();
        out.println(nombre);
        out.flush();
        inicioJuego = false;
        System.out.println("Juego preparado - Invitado");
        gameController.desactivarTurnos();
    }

    private boolean getJugada(BufferedReader in) throws IOException {
        String serverData;
        heAcertado=true;
        while (heAcertado && !inicioJuego) {
        serverData = in.readLine();
        System.out.println("Me ha enviado una jugada el server");
        if (!serverData.equals("adios")){
                System.out.println("Y voy a leerla");
                String[] jugadaArray = serverData.split(",");
                int x = Integer.parseInt(jugadaArray[0]);
                int y = Integer.parseInt(jugadaArray[1]);
                gameController.turnoJugador(1);
                gameController.clicEnBoton(2,x,y);
                gameController.desactivarTurnos();
                System.out.println("Su jugada a sido: "+x+","+y+" y la he enviado al gridPane1");
                heAcertado= gameController.heAcertado();
        }  else {
            System.out.println("El host no quiere seguir jugando");
            return true;
        }
        }
        return false;
    }

    private boolean sendJugada(PrintStream out) {
        heAcertado=true;
        while (heAcertado && !inicioJuego) {
            System.out.println("Me toca enviar jugada al server (Ejemplo: 1,2 donde 1 sería la columna y 2 la fila)");
            String jugada = sc.next();
            if (!jugada.equals("adios")) {
                String[] jugadaMia = jugada.split(",");
                int x = Integer.parseInt(jugadaMia[0]);
                int y = Integer.parseInt(jugadaMia[1]);
                gameController.turnoJugador(2);
                gameController.clicEnBoton(1, x, y);
                gameController.desactivarTurnos();
                out.println(jugada);
                out.flush();
                System.out.println("Le he enviado esta: " + jugada);
                heAcertado= gameController.heAcertado();
            } else {
                System.out.println("Has cortado conexión con el host");
                out.println(jugada);
                out.flush();
                return true;
            }
        }
        return false;
    }

    //Metodo para gestionar el cierre de conexión
    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(TcpSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    //Metodo para iniciar el Thread del cliente
    public void run(){
        String address;
        String port;
        File file = new File("puertoCliente");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            address=bufferedReader.readLine();
            port=bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection(address, Integer.parseInt(port));
    }

}