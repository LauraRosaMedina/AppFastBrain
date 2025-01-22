package com.example.fastbrain.cliente;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "10.0.3.2"; // Dirección IP del servidor (host)
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    private static TextView textoJugadores; // Para actualizar la lista de jugadores conectados

    // Constructor
    public Client(TextView textoJugadores) {
        Client.textoJugadores = textoJugadores; // Asignar el TextView estático
    }

    public interface ClientCallback {
        void onResponse(String response);
    }

    // Hacer este método estático
    public static void connectToServer(ClientCallback callback) {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // Enviar una solicitud al servidor para pedir permiso
                out.println("SOLICITAR_PERMISO");

                // Leer la respuesta del servidor
                String response = in.readLine();
                if (callback != null) {
                    callback.onResponse(response); // Devolver la respuesta al hilo principal
                }

                // Si el servidor permite la conexión (responde con PERMITIDO)
                if ("PERMITIDO".equals(response)) {
                    // Pedir al jugador que ingrese su nombre (suponemos que "Jugador1")
                    String nombreJugador = "Jugador1";  // Aquí puedes pedir el nombre en la interfaz de usuario (UI)

                    // Enviar el nombre del jugador al servidor
                    out.println(nombreJugador);

                    // Leer la lista de jugadores conectados después de unirse
                    String jugadoresConectados = in.readLine();

                    // Actualizar la interfaz para mostrar la lista de jugadores conectados
                    actualizarJugadores(jugadoresConectados);
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onResponse("ERROR: " + e.getMessage());
                }
            }
        }).start();
    }

    // Método estático para actualizar la lista de jugadores conectados en la interfaz
    private static void actualizarJugadores(String jugadores) {
        // Actualizar el TextView con la lista de jugadores conectados
        textoJugadores.setText("Jugadores conectados:\n" + jugadores);
    }
}
