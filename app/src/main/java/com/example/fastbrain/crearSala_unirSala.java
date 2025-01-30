package com.example.fastbrain;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class crearSala_unirSala extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_sala_unir_sala);

        // Configurar animación de fondo
        ConstraintLayout layout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(500);
        animationDrawable.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón Crear Sala
        Button botonCrearSala = findViewById(R.id.botoncrearsala);
        botonCrearSala.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this, ajustespartida.class);
            startActivity(intent);
            finish();
        });

        // Icono de perfil (arriba a la derecha)
        ImageView iconoPerfil = findViewById(R.id.boton_perfil);
        iconoPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this, activity_perfil.class);
            startActivity(intent);
        });
    }
}
