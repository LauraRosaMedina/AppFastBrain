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

    private int codigoSala;  // Código generado para la sala

    public void ejecutarServidor() {
        // generar código aleatorio
        codigoSala = new Random().nextInt(9000) + 1000; // Números entre 1000 y 9999
        System.out.println("Código de sala generado: " + codigoSala);

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + PORT + "...");

            while (true) {
                // Aceptar conexión de un cliente
                Socket clientSocket = serverSocket.accept();

                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Crear un nuevo hilo para manejar al cliente
                    ClientHandler player = new ClientHandler(clientSocket);
                    players.add(player);
                    new Thread(player).start();
                } else {
                    // Rechazar al cliente si ya hay 4 jugadores
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    out.writeUTF("Servidor lleno. Inténtelo más tarde.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el código generado
    public int getCodigoSala() {
        return codigoSala;
    }

    // Clase interna para manejar la comunicación con cada cliente
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Crear flujos de entrada y salida
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                // Enviar mensaje de bienvenida al cliente
                out.writeUTF("¡Bienvenido al servidor! Esperando a otros jugadores...");
                System.out.println("Esperando mensajes del cliente...");

                // Leer y procesar la solicitud del cliente
                String message = in.readUTF();
                System.out.println("Mensaje del cliente: " + message);

                if ("Solicitud de unión a la sala".equals(message)) {
                    if (players.size() <= 4) {
                        out.writeUTF("OK");
                        System.out.println("Cliente unido a la sala.");
                    } else {
                        out.writeUTF("Sala llena");
                        clientSocket.close();
                    }
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