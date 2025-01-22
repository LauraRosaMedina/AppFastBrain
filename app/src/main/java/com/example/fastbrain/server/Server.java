package com.example.fastbrain.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server {
    private static final int PORT = 12345; // Puerto del servidor
    private static final int MAX_PLAYERS = 4; // Máximo de jugadores
    private List<Jugador> jugadoresConectados; // Lista de jugadores conectados
    private static Semaphore semaphore = new Semaphore(MAX_PLAYERS); // Semáforo para controlar los jugadores

    public Server() {
        // Inicializar la lista de jugadores conectados
        jugadoresConectados = new ArrayList<>();
    }

    public static void main(String[] args) {
        // Crear una instancia del servidor
        Server servidor = new Server();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                // Aceptar conexiones entrantes
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Crear un hilo para manejar al cliente y pasarle la instancia del servidor
                new Thread(servidor.new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase interna para manejar cada cliente
    class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Intentar adquirir un permiso del semáforo (máximo 4 jugadores)
                if (semaphore.tryAcquire()) {
                    // Enviar confirmación al cliente
                    out.println("PERMITIDO");

                    // Solicitar el nombre del jugador
                    out.println("Ingrese su nombre:");
                    String nombre = in.readLine(); // Leer el nombre del cliente

                    // Crear un objeto Jugador para este cliente
                    Jugador nuevoJugador = new Jugador(clientSocket);
                    nuevoJugador.setNombre(nombre);  // Asignar el nombre al jugador
                    jugadoresConectados.add(nuevoJugador); // Agregarlo a la lista de jugadores conectados

                    // Enviar la lista de jugadores a todos los clientes
                    enviarListaJugadores();

                    // Esperar hasta que todos los jugadores estén listos
                    if (semaphore.availablePermits() == 0) {
                        System.out.println("Todos los jugadores están conectados. Comenzando el juego...");
                        // Lógica para iniciar el juego
                    }

                } else {
                    // Rechazar al cliente si ya hay 4 jugadores
                    out.println("RECHAZADO");
                    System.out.println("Jugador rechazado: " + clientSocket.getInetAddress());
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Método para enviar la lista de jugadores a todos los clientes
        private void enviarListaJugadores() {
            StringBuilder listaJugadores = new StringBuilder("Jugadores conectados: ");
            for (Jugador jugador : jugadoresConectados) {
                listaJugadores.append(jugador.getNombre()).append(", ");
            }

            // Eliminar la última coma y espacio
            if (listaJugadores.length() > 2) {
                listaJugadores.setLength(listaJugadores.length() - 2);
            }

            // Enviar la lista de jugadores a todos los clientes
            for (Jugador jugador : jugadoresConectados) {
                jugador.enviarMensaje(listaJugadores.toString());
            }
        }
    }
}
