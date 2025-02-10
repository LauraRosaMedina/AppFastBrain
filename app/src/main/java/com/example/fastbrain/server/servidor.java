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
    private static int turnoActual = -1; // Índice del jugador en turno
    private int codigoSala;  // Código generado para la sala

    public void ejecutarServidor() {
        // Generar código aleatorio para la sala
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

                    // Si hay al menos 2 jugadores, iniciar los turnos
                    if (players.size() >= 2) {
                        iniciarTurnos();
                    }
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

    // Método para iniciar los turnos cuando hay al menos 2 jugadores
    private static void iniciarTurnos() {
        if (players.size() >= 2 && turnoActual == -1) { // Solo asignar turno inicial si hay al menos 2 jugadores y el turno no ha sido asignado aún
            turnoActual = new Random().nextInt(players.size()); // Asigna un turno aleatorio a uno de los jugadores
            System.out.println("Turno inicial para el jugador: " + turnoActual);
            actualizarTurnos();  // Llama al método para notificar a los jugadores el turno
        }
    }


    // Método para actualizar turno
    private static void actualizarTurnos() {
        for (int i = 0; i < players.size(); i++) {
            ClientHandler player = players.get(i);
            if (i == turnoActual) {
                player.enviarMensaje("TURNO_ACTIVO"); // Notifica al jugador que es su turno
            } else {
                player.enviarMensaje("ESPERA_TURNO"); // Notifica a los demás que esperen
            }
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
        private servidor servidorInstance;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.servidorInstance = servidorInstance;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                out.writeUTF("¡Bienvenido al servidor! Esperando a otros jugadores...");
                out.writeUTF("OK"); // Confirmación para el cliente

                // Notificar a los jugadores sobre el código de la sala
                out.writeUTF("Código de sala: " + servidorInstance.getCodigoSala());

                while (true) {
                    String message = in.readUTF(); // Escuchar mensajes del cliente
                    System.out.println("Mensaje del cliente: " + message);

                    if ("TURNO_FINALIZADO".equals(message)) {
                        avanzarTurno();
                    }
                }
            } catch (IOException e) {
                System.out.println("Jugador desconectado.");
                players.remove(this);
                semaphore.release();
                if (!players.isEmpty()) {
                    ajustarTurnosTrasDesconexion();
                }
            }
        }

        // Método para enviar mensajes a los clientes
        public void enviarMensaje(String mensaje) {
            try {
                out.writeUTF(mensaje);
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje al cliente.");
            }
        }
    }

    // Método para pasar el turno al siguiente jugador
    private static void avanzarTurno() {
        if (!players.isEmpty()) {
            turnoActual = (turnoActual + 1) % players.size();
            System.out.println("Turno cambiado al jugador: " + turnoActual);
            actualizarTurnos();
        }
    }

    // Método para reajustar los turnos cuando un jugador se desconecta
    private static void ajustarTurnosTrasDesconexion() {
        if (players.isEmpty()) {
            turnoActual = -1; // No hay jugadores, no hay turnos
            return;
        }
        if (turnoActual >= players.size()) {
            turnoActual = 0; // Resetear al primer jugador si el actual se ha ido
        }
        actualizarTurnos();
    }
}
