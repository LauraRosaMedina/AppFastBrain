package com.example.fastbrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private Button btnSalir;
    private ImageButton botonPerfil;
    private LinearLayout cuadroJugar;
    private TextView turnoTextView;
    private boolean esMiTurno = true;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static final String SERVER_ADDRESS = "192.168.1.132";
    private static final int SERVER_PORT = 12345;

    private final int[] colores = {
            Color.parseColor("#FF5733"),
            Color.parseColor("#33FF57"),
            Color.parseColor("#3357FF"),
            Color.parseColor("#FF33A6"),
            Color.parseColor("#FFD433")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        // Inicializar vistas
        TextView emailUsuarioTextView = findViewById(R.id.emailUsuarioTextView);
        TextView usuarioTextView = findViewById(R.id.usuarioTextView);
        cuadroJugar = findViewById(R.id.cuadrojugar);
        btnSalir = findViewById(R.id.btnSalir);
        botonPerfil = findViewById(R.id.boton_perfil);
        turnoTextView = findViewById(R.id.turnoTextView);

        // Recibir datos desde la otra actividad
        int codigoSala = getIntent().getIntExtra("codigo_sala", -1);
        String emailUsuario = getIntent().getStringExtra("email");
        esMiTurno = getIntent().getBooleanExtra("es_mi_turno", true);

        // Mostrar código de sala y email del usuario
        emailUsuarioTextView.setText(codigoSala != -1 ? "Código de sala: " + codigoSala : "Código de sala no disponible.");
        usuarioTextView.setText(emailUsuario != null && !emailUsuario.isEmpty() ? "Email del usuario: " + emailUsuario : "Email no disponible.");

        // Actualizar el estado del turno
        actualizarTurno();

        // Conectar al servidor
        new Thread(this::conectarServidor).start();

        // Configurar botón para salir
        btnSalir.setOnClickListener(v -> {
            desconectarServidor();
            startActivity(new Intent(activity_jugar.this, crearSala_unirSala.class));
            finish();
        });

        // Configurar botón de perfil
        botonPerfil.setOnClickListener(v -> startActivity(new Intent(activity_jugar.this, activity_perfil.class)));

        // Configurar animación de colores
        configurarAnimacionColor();
    }

    private void conectarServidor() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Hilo para escuchar mensajes del servidor
            new Thread(this::escucharServidor).start();

        } catch (IOException e) {
            Log.e("Servidor", "Error al conectar con el servidor", e);
            runOnUiThread(() -> Toast.makeText(activity_jugar.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
        }
    }

    private void escucharServidor() {
        try {
            while (true) {
                String mensaje = in.readUTF();
                Log.d("Servidor", "Mensaje recibido: " + mensaje);

                runOnUiThread(() -> {
                    if ("TURNO_ACTIVO".equals(mensaje)) {
                        esMiTurno = true;
                        Toast.makeText(activity_jugar.this, "¡Es tu turno!", Toast.LENGTH_SHORT).show();
                    } else if ("ESPERA_TURNO".equals(mensaje)) {
                        esMiTurno = false;
                        Toast.makeText(activity_jugar.this, "Espera tu turno...", Toast.LENGTH_SHORT).show();
                    }
                    actualizarTurno();
                });
            }
        } catch (IOException e) {
            Log.e("Servidor", "Error en la comunicación con el servidor", e);
        }
    }

    private void actualizarTurno() {
        turnoTextView.setText(esMiTurno ? "¡Es tu turno!" : "Espera tu turno...");
    }

    private void configurarAnimacionColor() {
        cuadroJugar.setOnClickListener(v -> {
            if (!esMiTurno) {
                Toast.makeText(activity_jugar.this, "No es tu turno, espera...", Toast.LENGTH_SHORT).show();
                return;
            }

            ValueAnimator colorAnim = ValueAnimator.ofArgb(
                    Color.parseColor("#D50032"),
                    Color.parseColor("#00FF00"),
                    Color.parseColor("#4B5320"),
                    Color.parseColor("#FFEB3B"),
                    Color.parseColor("#1E90FF")
            );
            colorAnim.setDuration(2000);
            colorAnim.setInterpolator(new android.view.animation.LinearInterpolator());
            colorAnim.addUpdateListener(animator -> cuadroJugar.setBackgroundColor((int) animator.getAnimatedValue()));
            colorAnim.start();

            colorAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    int colorSeleccionado = colores[new Random().nextInt(colores.length)];
                    cuadroJugar.setBackgroundColor(colorSeleccionado);
                    String categoriaSeleccionada = obtenerCategoriaPorColor(colorSeleccionado);
                    Intent intent = new Intent(activity_jugar.this, activity_preguntas.class);
                    intent.putExtra("categoria", categoriaSeleccionada);
                    startActivity(intent);
                }
            });
        });
    }

    private String obtenerCategoriaPorColor(int color) {
        switch (color) {
            case 0x00FF00:
                return "Mundo Sobrenatural";
            case 0xD50032:
                return "Cine";
            case 0x4B5320:
                return "Sabores del Mundo";
            case 0xFFEB3B:
                return "Risas y Memes";
            case 0x1E90FF:
                return "Gamer";
            default:
                return "Cine";
        }
    }

    private void finalizarTurno() {
        new Thread(() -> {
            try {
                if (out != null) {
                    out.writeUTF("TURNO_FINALIZADO");
                    out.flush();
                }
                runOnUiThread(() -> {
                    esMiTurno = false;
                    actualizarTurno();
                    Toast.makeText(activity_jugar.this, "Turno finalizado, espera tu turno...", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                Log.e("Servidor", "Error al enviar turno finalizado", e);
            }
        }).start();
    }

    private void desconectarServidor() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            Log.e("Servidor", "Error al desconectar", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        desconectarServidor();
    }
}
