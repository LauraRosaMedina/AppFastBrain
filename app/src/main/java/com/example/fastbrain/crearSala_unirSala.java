package com.example.fastbrain;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastbrain.server.servidor;

import java.util.Random;

public class crearSala_unirSala extends AppCompatActivity {

    private TextView textViewCodigoSala;
    private Button botonCrearSala;
    private Button botonUnirSala;
    private servidor servidorInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_sala_unir_sala);

        // Inicialización de las vistas
        textViewCodigoSala = findViewById(R.id.textViewCodigoSala);  // Asegúrate de tener este TextView en tu XML
        // Obtener el email que viene del inicio de sesión
        String email = getIntent().getStringExtra("email");
        Log.d("EmailDebug", "Email recibido en crearSala_unirSala: " + email);

        // Fondo animado
        ConstraintLayout layout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(500);
        animationDrawable.start();

        // Ajuste de márgenes para el diseño en pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar el servidor (se quedará esperando conexiones)
        servidorInstance = new servidor();

        // Configuración de botón para crear sala
        botonCrearSala = findViewById(R.id.botoncrearsala);
        botonCrearSala.setOnClickListener(v -> {
            // Generar el código aleatorio
            int codigoSala = generarCodigoSala();

            // Mostrar el código en el TextView
            textViewCodigoSala.setText(String.valueOf(codigoSala));

            // Copiar el código al portapapeles
            copiarAlPortapapeles(codigoSala);

            // Mostrar un Toast de confirmación
            Toast.makeText(crearSala_unirSala.this, "Sala creada. Código: " + codigoSala, Toast.LENGTH_SHORT).show();

            // Iniciar el servidor en otro hilo
            new Thread(() -> {
                servidorInstance.ejecutarServidor();
            }).start();
        });

        // Configuración de botón para unirse a una sala
        botonUnirSala = findViewById(R.id.boton_unirSala);
        botonUnirSala.setOnClickListener(v -> {
            // Obtener el código de sala desde el TextView
            String codigoSalaString = textViewCodigoSala.getText().toString();
            try {
                int codigoJuego = Integer.parseInt(codigoSalaString);

                // Pasar los datos a la actividad "activity_jugar"
                Intent intent = new Intent(crearSala_unirSala.this, activity_jugar.class);
                intent.putExtra("codigo_sala", codigoJuego);  // Código de sala
                intent.putExtra("email", email);  // Email del usuario
                intent.putExtra("es_mi_turno", false);  // El segundo jugador no tiene el turno inicialmente
                startActivity(intent);
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(crearSala_unirSala.this, "Código de sala inválido", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuración del icono de perfil
        ImageView iconoPerfil = findViewById(R.id.boton_perfil);
        iconoPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this, activity_perfil.class);
            startActivity(intent);
        });
    }

    // Método para generar un código aleatorio de 4 dígitos
    private int generarCodigoSala() {
        return new Random().nextInt(9000) + 1000; // Genera un número entre 1000 y 9999
    }

    // Método para copiar al portapapeles
    private void copiarAlPortapapeles(int codigoSala) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Código de sala", String.valueOf(codigoSala));
        clipboard.setPrimaryClip(clip);
        Log.d("CopiarCodigo", "Código copiado al portapapeles: " + codigoSala);
    }
}
