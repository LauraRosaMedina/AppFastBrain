package com.example.fastbrain.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class cliente {
    private static final String SERVER_ADDRESS = "10.192.117.26"; // IP del servidor
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean esMiTurno = false; // Estado del turno

    public cliente() {
        // Constructor vacío, ya no recibe TextView
    }

    public void conectarServidor() {
        if (socket != null && !socket.isClosed()) {
            System.out.println("Ya estás conectado al servidor.");
            return;
        }

        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                System.out.println("Conectado al servidor: " + SERVER_ADDRESS + " en el puerto " + SERVER_PORT);

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("UNIR_SALA");
                out.flush();

                String response = in.readUTF();
                if ("TURNO_ACTIVO".equals(response)) {
                    esMiTurno = true;
                    System.out.println("¡Es tu turno!");
                }

                escucharServidor();
            } catch (IOException e) {
                System.out.println("Error al conectar con el servidor: " + e.getMessage());
            }
        }).start();
    }

    private void escucharServidor() {
        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = in.readUTF();
                    manejarMensajeServidor(serverMessage);
                }
            } catch (IOException e) {
                System.out.println("Conexión perdida con el servidor.");
            }
        }).start();
    }

    private void manejarMensajeServidor(String serverMessage) {
        System.out.println("Servidor: " + serverMessage);

        switch (serverMessage) {
            case "TURNO_ACTIVO":
                esMiTurno = true;
                System.out.println("¡Es tu turno!");
                break;
            case "ESPERA_TURNO":
                esMiTurno = false;
                System.out.println("Espera tu turno...");
                break;
            default:
                System.out.println("Mensaje desconocido: " + serverMessage);
                break;
        }
    }

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
