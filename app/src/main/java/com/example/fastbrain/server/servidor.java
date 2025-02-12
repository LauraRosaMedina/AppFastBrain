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

    public void ejecutarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + PORT + "...");

            while (true) {
                // Aceptar conexión de un cliente
                Socket clientSocket = serverSocket.accept();

                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Crear un nuevo hilo para manejar al cliente
                    ClientHandler player = new ClientHandler(clientSocket, this);

                    players.add(player);
                    new Thread(player).start();

                    // Si hay al menos 2 jugadores, iniciar los turnos
                    if (players.size() == 2) {
                        iniciarTurnos();
                    }
                } else {
                    // Si el servidor ha alcanzado el límite de jugadores, rechazar la conexión
                    System.out.println("Se ha alcanzado el límite de jugadores.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarTurnos() {
        // El primer jugador en conectarse obtiene el primer turno
        turnoActual = 0;

        for (ClientHandler player : players) {
            player.enviarMensaje("TURNO_ACTIVO");
        }
    }

    public synchronized void avanzarTurno() {
        // Avanzar al siguiente jugador
        turnoActual = (turnoActual + 1) % players.size();
        System.out.println("Es el turno del jugador " + (turnoActual + 1));

        // Informar a todos los jugadores sobre el cambio de turno
        for (int i = 0; i < players.size(); i++) {
            ClientHandler player = players.get(i);
            if (i == turnoActual) {
                player.enviarMensaje("TURNO_ACTIVO");
            } else {
                player.enviarMensaje("ESPERA_TURNO");
            }
        }
    }

    // Clase interna para manejar la comunicación con los clientes
    private class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private servidor server;

        public ClientHandler(Socket socket, servidor server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // Recibir mensaje de unión a la sala
                String mensaje = in.readUTF();
                if (mensaje.equals("UNIR_SALA")) {
                    // Esperar el turno
                    out.writeUTF("ESPERA_TURNO");
                    out.flush();
                }

                while (true) {
                    // Leer mensajes de los jugadores y responder
                    String input = in.readUTF();

                    if (input.equals("TURNO_FINALIZADO")) {
                        server.avanzarTurno();  // Cambiar el turno
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cerrar la conexión con el cliente
                try {
                    in.close();
                    out.close();
                    socket.close();
                    semaphore.release();
                    players.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Método para enviar un mensaje al cliente
        public void enviarMensaje(String mensaje) {
            try {
                out.writeUTF(mensaje);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        servidor server = new servidor();
        server.ejecutarServidor();
    }
}
