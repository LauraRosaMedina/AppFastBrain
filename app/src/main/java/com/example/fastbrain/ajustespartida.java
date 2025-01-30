package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastbrain.server.servidor;
import com.example.fastbrain.server.servidorMain;

public class ajustespartida extends AppCompatActivity {

    private int numJugadores = 1; // Valor inicial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustespartida);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los botones y al contador
        Button btnDecrementar = findViewById(R.id.btn_decrementar);
        Button btnIncrementar = findViewById(R.id.btn_incrementar);
        TextView contadorJugadores = findViewById(R.id.contador_jugadores);

        // Inicializar el valor en el TextView
        contadorJugadores.setText(String.valueOf(numJugadores));

        // Botón de incrementar
        btnIncrementar.setOnClickListener(v -> {
            if (numJugadores < 4) { // Límite superior
                numJugadores++;
                contadorJugadores.setText(String.valueOf(numJugadores));
            }
        });

        // Botón de decrementar
        btnDecrementar.setOnClickListener(v -> {
            if (numJugadores > 1) { // Límite inferior
                numJugadores--;
                contadorJugadores.setText(String.valueOf(numJugadores));
            }
        });


        Button btn_baja = findViewById(R.id.btn_baja);
        Button btn_media = findViewById(R.id.btn_media);
        Button btn_alta = findViewById(R.id.btn_alta);
        Button btn_espanol = findViewById(R.id.btn_espanol);
        Button btn_ingles = findViewById(R.id.btn_ingles);
        Button btn_crear_partida = findViewById(R.id.btn_crear_partida);


        btn_baja.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad baja", Toast.LENGTH_SHORT).show();

        });


        btn_media.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad media", Toast.LENGTH_SHORT).show();
        });

        btn_alta.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad alta", Toast.LENGTH_SHORT).show();
        });


        btn_espanol.setOnClickListener(v ->{
            Toast.makeText(this, "Ha elegido español", Toast.LENGTH_SHORT).show();
        });

        btn_ingles.setOnClickListener(v ->{
            Toast.makeText(this, "Ha elegido inglés", Toast.LENGTH_SHORT).show();
        });


        btn_crear_partida.setOnClickListener(v -> {
            // Iniciar el servidor en un hilo separado
            new Thread(() -> {
                servidor server = new servidor();
                server.ejecutarServidor();
            }).start();

            // Pasar a la Activity "Jugar"
            Intent intent = new Intent(ajustespartida.this, activity_jugar.class);
            startActivity(intent);
            finish();
        });

    }





}