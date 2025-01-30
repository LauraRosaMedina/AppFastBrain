package com.example.fastbrain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_cambiarcontrasena extends AppCompatActivity {

    private EditText nuevaContrasena, confirmarContrasena;
    private Button btnGuardar;
    private ImageButton btnVolver;
    private ConexionBBDD conexionBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambiarcontrasena);

        // Ajuste de padding para compatibilidad con gestos y barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        nuevaContrasena = findViewById(R.id.emailField);
        confirmarContrasena = findViewById(R.id.passwordField);
        btnGuardar = findViewById(R.id.registerButton);
        btnVolver = findViewById(R.id.button_reset);
        conexionBBDD = new ConexionBBDD(this);

        // Manejar clic en el botón "Guardar Cambios"
        btnGuardar.setOnClickListener(v -> cambiarContrasena());

        // Manejar clic en el botón "Volver"
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cambiarContrasena() {
        String nuevaPass = nuevaContrasena.getText().toString().trim();
        String confirmarPass = confirmarContrasena.getText().toString().trim();

        if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
            Toast.makeText(this, "Por favor, llena ambos campos", Toast.LENGTH_SHORT).show();
        } else if (!nuevaPass.equals(confirmarPass)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else if (nuevaPass.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            // Llamar al método de ConexionBBDD para cambiar la contraseña
            conexionBBDD.cambiarContrasena(nuevaPass);
        }
    }
}
