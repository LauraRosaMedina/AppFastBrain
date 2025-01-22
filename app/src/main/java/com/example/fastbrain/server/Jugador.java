package com.example.fastbrain.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Jugador {
    private Socket socket;
    private PrintWriter out;
    private String nombre;

    public Jugador(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter y setter para el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para enviar un mensaje al jugador
    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    public Socket getSocket() {
        return socket;
    }
}

