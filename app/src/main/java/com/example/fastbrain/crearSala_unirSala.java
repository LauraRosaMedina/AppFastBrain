package com.example.fastbrain;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.fastbrain.cliente.Client;

public class crearSala_unirSala extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_sala_unir_sala);
        ConstraintLayout layout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(500); // Tiempo de aparición
        animationDrawable.setExitFadeDuration(500); // Tiempo de desaparición
        animationDrawable.start();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Obtener el botón de crear sala
        Button botonCrearSala = findViewById(R.id.botoncrearsala);

        // Configurar el listener del botón para conectar al servidor
        botonCrearSala.setOnClickListener(v -> {
            // Mostrar mensaje indicando que se está conectando
            Toast.makeText(this, "Conectando al servidor...", Toast.LENGTH_SHORT).show();

            // Conectar al servidor y recibir respuesta
            Client.connectToServer(response -> runOnUiThread(() -> {
                // Mostrar la respuesta del servidor en un Toast
                Toast.makeText(this, "Servidor: " + response, Toast.LENGTH_LONG).show();

                // Si la respuesta es "PERMITIDO", cambiar a la pantalla de ajustes de partida
                if (response.equals("PERMITIDO")) {
                    // Aquí puedes iniciar la actividad de ajustes de partida, por ejemplo:
                    // Intent intent = new Intent(crearSala_unirSala.this, sala_espera.class);
                    // startActivity(intent);
                }
            }));
        });
    }
}

