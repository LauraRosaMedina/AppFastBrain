package com.example.fastbrain;

import android.content.Intent;
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

import com.example.fastbrain.client.cliente;


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


        Button botoncrearsala = findViewById(R.id.botoncrearsala);
        Button boton_unirSala = findViewById(R.id.boton_unirSala);

        botoncrearsala.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this , ajustespartida.class);
            startActivity(intent);

            finish();
        });


        boton_unirSala.setOnClickListener(v -> {
            // Iniciar el cliente en un hilo separado
            new Thread(() -> {
                cliente client = new cliente();
                client.conectarServidor();
            }).start();

            // Cambiar a la actividad de juego
            Intent intent = new Intent(crearSala_unirSala.this, activity_jugar.class);
            startActivity(intent);
            finish();
        });

    }
}

