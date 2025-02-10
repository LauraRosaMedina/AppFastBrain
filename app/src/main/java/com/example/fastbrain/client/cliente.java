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
    private boolean esMiTurno = true; // Para saber si el jugador puede jugar
    private TextView turnoTextView; // Para actualizar el estado del turno en la UI

    // Constructor para recibir el TextView desde la actividad
    public cliente(TextView turnoTextView) {
        this.turnoTextView = turnoTextView;
    }

    public void conectarServidor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Intentamos conectar con el servidor
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    System.out.println("Conectado al servidor: " + SERVER_ADDRESS + " en el puerto " + SERVER_PORT);

                    // Establecer los flujos de entrada y salida
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());

                    // Enviar solicitud de unión a la sala
                    out.writeUTF("UNIR_SALA");
                    out.flush();  // Asegurarse de que el mensaje se envíe inmediatamente

                    // Esperar la respuesta del servidor
                    String response = in.readUTF();
                    if (response.equals("SALA_OK")) {
                        System.out.println("Unido correctamente a la sala.");
                        // Enviar un mensaje para preguntar el estado del juego
                        out.writeUTF("ESTADO_JUEGO");
                        out.flush();
                        // Iniciar escucha de mensajes del servidor
                        escucharServidor();
                    } else if (response.equals("SALA_LLENA")) {
                        System.out.println("La sala está llena.");
                        // Mostrar mensaje de error o redirigir a otra actividad
                        actualizarUI("Sala llena, intenta más tarde.");
                    } else {
                        System.out.println("No se pudo unir a la sala: " + response);
                    }

                } catch (IOException e) {
                    System.out.println("No se pudo conectar al servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Método para escuchar mensajes del servidor (manejo de turnos y estado del juego)
    private void escucharServidor() {
        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = in.readUTF();
                    System.out.println("Servidor: " + serverMessage);

                    if ("TURNO_ACTIVO".equals(serverMessage)) {
                        esMiTurno = true;
                        actualizarUI("¡Es tu turno!");
                    } else if ("ESPERA_TURNO".equals(serverMessage)) {
                        esMiTurno = false;
                        actualizarUI("Espera tu turno...");
                    } else if ("INICIO_JUEGO".equals(serverMessage)) {
                        // El juego está listo para comenzar
                        actualizarUI("¡El juego ha comenzado! Espera tu turno.");
                    } else if ("SALA_LISTA".equals(serverMessage)) {
                        // La sala está lista para comenzar
                        actualizarUI("Sala lista, esperando turno.");
                    } else if ("SALA_NO_LISTA".equals(serverMessage)) {
                        // La sala no está lista para empezar (puede ser un mensaje de espera)
                        actualizarUI("Esperando que la sala esté lista.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Desconectado del servidor.");
            }
        }).start();
    }

    // Método para enviar al servidor que el turno ha finalizado
    public void enviarFinTurno() {
        if (esMiTurno) { // Solo puede enviarlo si es su turno
            try {
                out.writeUTF("TURNO_FINALIZADO");
                out.flush();
                esMiTurno = false; // Deshabilitar hasta recibir el siguiente turno
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
