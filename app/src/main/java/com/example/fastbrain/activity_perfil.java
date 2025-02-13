package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Referencias a los botones
        Button btnCambiarContrasena = findViewById(R.id.roundButton2);
        Button btnCerrarSesion = findViewById(R.id.roundButton3);
        ImageButton btnVolver = findViewById(R.id.button_reset);


        // Botón "Cambiar Contraseña" -> Redirigir a activity_cambiarcontrasena
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_perfil.this, activity_cambiarcontrasena.class);
                startActivity(intent);
            }
        });

        // Botón "Cerrar Sesión" -> Cerrar sesión y redirigir a activity_iniciarsesion
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity_perfil.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_perfil.this, activity_iniciarsesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Botón "Volver" -> Regresa a la actividad anterior
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y vuelve atrás
            }
        });
    }
}
