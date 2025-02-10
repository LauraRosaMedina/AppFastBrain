package com.example.fastbrain.client;

import java.io.*;
import java.net.*;
import android.widget.TextView;

public class cliente {
    private static final String SERVER_ADDRESS = "10.192.117.26"; // IP del servidor
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean esMiTurno = false; // Para saber si el jugador puede jugar
    private TextView turnoTextView; // Para actualizar el estado del turno en la UI
    private int codigoSala; // Código de sala

    // Constructor para recibir el TextView desde la actividad
    public cliente(TextView turnoTextView) {
        this.turnoTextView = turnoTextView;
    }

    public void conectarServidor() {
        if (socket != null && !socket.isClosed()) {
            System.out.println("Ya estás conectado al servidor.");
            return;  // No intenta reconectarse si la conexión sigue activa
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    System.out.println("Conectado al servidor: " + SERVER_ADDRESS + " en el puerto " + SERVER_PORT);

                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());

                    out.writeUTF("UNIR_SALA");
                    out.flush();

                    String response = in.readUTF();
                    if (response.startsWith("Código de sala:")) {
                        codigoSala = Integer.parseInt(response.split(":")[1].trim());
                        out.writeUTF("UNIR_SALA" + codigoSala);
                        out.flush();

                        String salaResponse = in.readUTF();
                        if (salaResponse.equals("SALA_OK")) {
                            System.out.println("Unido correctamente a la sala.");
                            out.writeUTF("ESTADO_JUEGO");
                            out.flush();
                            escucharServidor();
                        } else if (salaResponse.equals("SALA_LLENA")) {
                            System.out.println("La sala está llena.");
                            actualizarUI("Sala llena, intenta más tarde.");
                            socket.close();
                        } else {
                            System.out.println("No se pudo unir a la sala: " + salaResponse);
                            socket.close();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("No se pudo conectar al servidor: " + e.getMessage());
                    actualizarUI("Error al conectar con el servidor.");
                }
            }
        }).start();
    }

    // Método mejorado para escuchar mensajes del servidor
    private void escucharServidor() {
        new Thread(() -> {
            try {
                while (true) {
                    if (socket == null || socket.isClosed()) {
                        actualizarUI("Desconectado del servidor.");
                        break;
                    }

                    String serverMessage = in.readUTF();
                    manejarMensajeServidor(serverMessage);
                }
            } catch (IOException e) {
                actualizarUI("Conexión perdida con el servidor.");
            }
        }).start();
    }

    // Método para manejar los mensajes del servidor
    private void manejarMensajeServidor(String serverMessage) {
        System.out.println("Servidor: " + serverMessage);

        switch (serverMessage) {
            case "TURNO_ACTIVO":
                esMiTurno = true;
                actualizarUI("¡Es tu turno!");
                break;
            case "ESPERA_TURNO":
                esMiTurno = false;
                actualizarUI("Espera tu turno...");
                break;
            case "INICIO_JUEGO":
                actualizarUI("¡El juego ha comenzado! Espera tu turno.");
                break;
            case "SALA_LISTA":
                actualizarUI("Sala lista, esperando turno.");
                break;
            case "SALA_NO_LISTA":
                actualizarUI("Esperando que la sala esté lista.");
                break;
            default:
                actualizarUI("Mensaje desconocido: " + serverMessage);
                break;
        }
    }

    // Método para enviar al servidor que el turno ha finalizado
    public void enviarFinTurno() {
        if (esMiTurno) {
            try {
                out.writeUTF("TURNO_FINALIZADO");
                out.flush();
                esMiTurno = false;
                actualizarUI("Turno finalizado, esperando...");
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje de fin de turno.");
            }
        }
    }

    // Método para actualizar la UI con mensajes de turno
    private void actualizarUI(String mensaje) {
        if (turnoTextView != null) {
            turnoTextView.post(() -> turnoTextView.setText(mensaje));
        }
    }

    // Método para desconectar del servidor
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
