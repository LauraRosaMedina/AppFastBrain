package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_iniciarsesion extends AppCompatActivity {
    private ConexionBBDD conexionBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciarsesion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Iniciamos la conexión a la base de datos
        conexionBBDD = new ConexionBBDD(this);

        // Hacemos referencia a los elementos xml
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.roundButton2);
        ImageButton button_reset = findViewById(R.id.button_reset);

        button_reset.setOnClickListener(v -> {
            Intent intent = new Intent(activity_iniciarsesion.this, inicio.class);
            startActivity(intent);
            finish();
        });

        // Configuramos el botón de inicio de sesión
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            Log.d("IniciarSesion", "Email obtenido: " + email);
            String password = passwordField.getText().toString().trim();

            // Validamos si los datos son correctos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Usamos la conexión a la BBDD para iniciar sesión
            conexionBBDD.iniciarSesion(email, password, new ConexionBBDD.OnLoginResultCallBack() {
                @Override
                public void onSuccess() {
                    // Nos movemos a crear sala una vez se loguee exitosamente
                    Intent intent = new Intent(activity_iniciarsesion.this, crearSala_unirSala.class);
                    intent.putExtra("email", email);  // Pasar el email como extra
                    Log.d("IniciarSesion", "Email enviado a crearSala_unirSala: " + email);  // Verificar que se pasa el email
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure() {
                    // Mostramos mensaje de error si el inicio de sesión falla
                    Toast.makeText(activity_iniciarsesion.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
