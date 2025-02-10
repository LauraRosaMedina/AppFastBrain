package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_registrarse extends AppCompatActivity {

    private boolean registroExitoso = false; // Variable para controlar el estado del registro

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

        // Acción para el botón de reset
        button_reset.setOnClickListener(v -> {
            Intent intent = new Intent(activity_registrarse.this, activity_iniciarsesion.class);
            startActivity(intent);
            finish();
        });

        // Acción para el botón de registro
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(activity_registrarse.this, "Formato de correo inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            // Llamar al método de agregarUsuario de ConexionBBDD, pasando el callback
            conexionBBDD.agregarUsuario(email, password, new ConexionBBDD.RegistroCallback() {
                @Override
                public void onRegistroExitoso() {
                    // Cuando el registro es exitoso, cambiamos el estado y navegamos
                    registroExitoso = true;
                }

                @Override
                public void onRegistroFallido() {
                    // Cuando el registro falla, cambiamos el estado a false
                    registroExitoso = false;
                    Toast.makeText(activity_registrarse.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                }
            });

            // Usamos Handler para esperar 3 segundos antes de hacer algo
            new Handler().postDelayed(() -> {
                // Si el registro fue exitoso, navegar a la actividad de inicio de sesión
                if (registroExitoso) {
                    Intent intent = new Intent(activity_registrarse.this, activity_iniciarsesion.class);
                    startActivity(intent);
                    finish();  // Finalizar la actividad actual
                }
                // Si el registro no fue exitoso, la actividad sigue en su lugar
            }, 3000);  // Esperar 3 segundos antes de cambiar
        });
    }
}