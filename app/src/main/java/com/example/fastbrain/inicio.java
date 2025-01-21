package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtén la referencia del botón
        Button iniciarSesionButton = findViewById(R.id.roundButton);
        Button registrarse = findViewById(R.id.roundButton2);
        // Configura el evento de clic en el botón
        iniciarSesionButton.setOnClickListener(v -> {
            // Crea un Intent para ir a activity_iniciarsesion
            Intent intent = new Intent(inicio.this, activity_iniciarsesion.class);
            startActivity(intent);
        });

        registrarse.setOnClickListener(view -> {
            Intent intent = new Intent(inicio.this, activity_registrarse.class);
            startActivity(intent);
        });

    }
}