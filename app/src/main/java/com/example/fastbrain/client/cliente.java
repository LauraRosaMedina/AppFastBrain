package com.example.fastbrain.client;

import java.io.*;
import java.net.*;
import android.widget.TextView;

public class cliente {
    private static final String SERVER_ADDRESS = "10.192.116.26"; // IP del servidor
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void conectarServidor() {
        try {
            // Intentamos conectar con el servidor
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor: " + SERVER_ADDRESS + " en el puerto " + SERVER_PORT);

            // Establecer los flujos de entrada y salida
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Leer mensaje de bienvenida del servidor
            String serverMessage = in.readUTF();
            System.out.println("Servidor: " + serverMessage);

            // Enviar una solicitud de conexión al servidor
            out.writeUTF("Solicitud de unión a la sala");
            out.flush();  // Asegurarse de que el mensaje se envíe inmediatamente

            // Esperar la respuesta del servidor
            String response = in.readUTF(); // Usamos readUTF para leer mensajes de texto
            if (response.equals("OK")) {
                System.out.println("Unido correctamente a la sala.");
                // Aquí podríamos activar la activity jugar en Android
                // Por ejemplo, cambiar a una actividad de juego en Android
            } else {
                System.out.println("No se pudo unir a la sala: " + response);
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para desconectar del servidor cuando sea necesario
    public void desconectarServidor() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Desconectado del servidor.");
            }
        } catch (IOException e) {
            System.out.println("Error al desconectar del servidor: " + e.getMessage());
        }
    }
}
