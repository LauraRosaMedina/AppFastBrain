package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Clases
        ConexionBBDD conexionBBDD = new ConexionBBDD(activity_registrarse.this);

        // Elementos de la UI
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button registerButton = findViewById(R.id.registerButton);
        ImageButton button_reset = findViewById(R.id.button_reset);


        button_reset.setOnClickListener( v -> {
            Intent intent = new Intent(activity_registrarse.this, activity_iniciarsesion.class);
            startActivity(intent);

            finish();
        });
        registerButton.setOnClickListener( v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            conexionBBDD.agregarUsuario(email, password);

            new Handler().postDelayed(() -> {
                // Crear un Intent para navegar a la activity_iniciarsesion
                Intent intent = new Intent(activity_registrarse.this, activity_iniciarsesion.class);
                startActivity(intent);

                // Cerrar la actividad actual para que no pueda volver a ella
                finish();
            }, 3000); // 3000 milisegundos = 3 segundos



        });
    }
}