package com.example.fastbrain.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Dirección del servidor
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Leer mensaje de bienvenida del servidor
            System.out.println("Conectado al servidor.");
            String serverMessage = in.readLine();
            System.out.println(serverMessage);

            // Enviar mensajes al servidor
            new Thread(() -> {
                while (true) {
                    System.out.print("Tú: ");
                    String message = scanner.nextLine();
                    out.println(message); // Enviar mensaje al servidor
                }
            }).start();

            // Escuchar mensajes del servidor
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Servidor: " + response);
            }

        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor: " + e.getMessage());
        }
    }
}

