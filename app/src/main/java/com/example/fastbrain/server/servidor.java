package com.example.fastbrain.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class servidor {
    private static final int PORT = 12345; // Puerto donde el servidor escuchará conexiones
    private static final int MAX_PLAYERS = 4; // Número máximo de jugadores permitidos
    private static Semaphore semaphore = new Semaphore(MAX_PLAYERS); // Controla el acceso concurrente
    private static List<ClientHandler> players = Collections.synchronizedList(new ArrayList<>()); // Lista de jugadores

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Crear un nuevo hilo para manejar al cliente
                    ClientHandler player = new ClientHandler(clientSocket);
                    players.add(player);
                    new Thread(player).start();
                } else {
                    // Rechazar al cliente si ya hay 4 jugadores
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Servidor lleno. Inténtelo más tarde.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase interna para manejar la comunicación con cada cliente
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Enviar mensaje de bienvenida al cliente
                out.println("¡Bienvenido al servidor! Esperando a otros jugadores...");
                System.out.println("Esperando mensajes del cliente...");

                // Leer y procesar mensajes del cliente
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Mensaje del cliente: " + message);

                    // Responder al cliente
                    out.println("Servidor: Recibido -> " + message);

                    // Lógica del juego puede ir aquí (p. ej., broadcast a todos los jugadores)
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    semaphore.release(); // Liberar un lugar cuando un cliente se desconecta
                    players.remove(this);
                    System.out.println("Un jugador se ha desconectado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
