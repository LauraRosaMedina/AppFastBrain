package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

        //Iniciamos la conexión a la base de datos
        conexionBBDD = new ConexionBBDD(this);

        //Hacemos referencia a los elementos xml
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.roundButton2);


        //Configuramos el botón de inicio de sesión

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            //Vamos a validar si los datos son correctos

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campo" , Toast.LENGTH_SHORT).show();
                return;
            }
            //Usamos la conexión a la BBDD para inciar sesión
            conexionBBDD.iniciarSesion(email, password, new ConexionBBDD.OnLoginResultCallBack(){
                @Override
                public void onSuccess (){
                    //Nos movemos a crear sala si la conexión es correcta

                    Intent intent = new Intent(activity_iniciarsesion.this, crearSala_unirSala.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure (){
                    //Mostramos mensaje de error

                    Toast.makeText(activity_iniciarsesion.this, "Usuario o contraseña incorrectos" , Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}