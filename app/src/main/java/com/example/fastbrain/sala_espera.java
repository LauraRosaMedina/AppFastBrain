package com.example.fastbrain;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastbrain.cliente.Client;

public class sala_espera extends AppCompatActivity {

    private TextView textoJugadores;  // Referencia al TextView que muestra los jugadores conectados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sala_espera);

        // Inicializar el TextView
        textoJugadores = findViewById(R.id.texto_jugadores);

        // Configurar la animación de la ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Llamar a connectToServer para conectarse al servidor
        Client.connectToServer(new Client.ClientCallback() {
            @Override
            public void onResponse(String response) {
                // Aquí recibimos la respuesta del servidor (por ejemplo, "PERMITIDO")
                if ("PERMITIDO".equals(response)) {
                    // Si la respuesta es PERMITIDO, recibimos la lista de jugadores
                    actualizarListaJugadores();
                } else {
                    // Si la respuesta es "RECHAZADO" o algún error, lo manejamos aquí
                    textoJugadores.setText("Error: No se pudo conectar.");
                }
            }
        });
    }

    // Este método simula la actualización de la lista de jugadores conectados
    private void actualizarListaJugadores() {
        // Aquí debes recibir la lista de jugadores conectados desde el servidor
        // Este es solo un ejemplo, la lista real debe venir del servidor a través de Client
        String listaJugadores = "Jugadores conectados: Jugador1, Jugador2, Jugador3";

        // Actualizar el TextView con la lista de jugadores
        textoJugadores.setText(listaJugadores);
    }
}
