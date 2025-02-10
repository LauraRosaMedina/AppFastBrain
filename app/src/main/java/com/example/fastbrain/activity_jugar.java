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
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private Button btnSalir;
    private ImageButton botonPerfil; // Botón de perfil
    private LinearLayout cuadroJugar;
    private TextView turnoTextView; // Referencia al TextView de turno
    private boolean esMiTurno = false; // Variable para controlar el turno
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static final String SERVER_ADDRESS = "10.192.117.26"; // IP del servidor
    private static final int SERVER_PORT = 12345; // Puerto del servidor

    private int[] colores = {
            Color.parseColor("#FF5733"), // Naranja
            Color.parseColor("#33FF57"), // Verde
            Color.parseColor("#3357FF"), // Azul
            Color.parseColor("#FF33A6"), // Rosa
            Color.parseColor("#FFD433")  // Amarillo
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
        botonPerfil = findViewById(R.id.boton_perfil); // Botón de perfil correctamente inicializado
        turnoTextView = findViewById(R.id.turnoTextView); // Inicializar el TextView de turno

        // Recibir datos desde otra actividad
        int codigoSala = getIntent().getIntExtra("codigo_sala", -1);
        String emailUsuario = getIntent().getStringExtra("email");
        esMiTurno = getIntent().getBooleanExtra("es_mi_turno", false); // Obtener el estado del turno

        // Mostrar código de sala y email del usuario
        emailUsuarioTextView.setText(codigoSala != -1 ? "Código de sala: " + codigoSala : "Código de sala no disponible.");
        usuarioTextView.setText(emailUsuario != null && !emailUsuario.isEmpty() ? "Email del usuario: " + emailUsuario : "Email no disponible.");

        // Actualizar el TextView en base a si es tu turno o no
        actualizarTurno();

        // Conectar al servidor
        new Thread(this::conectarServidor).start();

        // Configurar el botón para salir
        btnSalir.setOnClickListener(v -> {
            desconectarServidor();
            Intent intent = new Intent(activity_jugar.this, crearSala_unirSala.class);
            startActivity(intent);
            finish();
        });

        // Configurar el botón de perfil
        botonPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(activity_jugar.this, activity_perfil.class);
            startActivity(intent);
        });

        // Llamada para configurar animación de colores
        configurarAnimacionColor();
    }

    private void conectarServidor() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Escuchar mensajes del servidor en un hilo aparte
            new Thread(this::escucharServidor).start();

        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(activity_jugar.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show());
        }
    }

    private void escucharServidor() {
        try {
            while (true) {
                String mensaje = in.readUTF();
                Log.d("Servidor", "Mensaje recibido: " + mensaje);

                if ("TURNO_ACTIVO".equals(mensaje)) {
                    runOnUiThread(() -> {
                        esMiTurno = true;
                        actualizarTurno();
                        Log.d("ACTUALIZACION_UI", "Es tu turno!");
                        Toast.makeText(activity_jugar.this, "¡Es tu turno!", Toast.LENGTH_SHORT).show();
                    });
                } else if ("ESPERA_TURNO".equals(mensaje)) {
                    runOnUiThread(() -> {
                        esMiTurno = false;
                        actualizarTurno();
                        Log.d("ACTUALIZACION_UI", "Espera tu turno...");
                        Toast.makeText(activity_jugar.this, "Espera tu turno...", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar el estado del turno en la UI
    private void actualizarTurno() {
        Log.d("ACTUALIZACION_UI", "Actualizando turno. esMiTurno: " + esMiTurno);
        if (esMiTurno) {
            turnoTextView.setText("¡Es tu turno!");
        } else {
            turnoTextView.setText("Espera tu turno...");
        }
    }

    private void configurarAnimacionColor() {
        cuadroJugar.setOnClickListener(v -> {
            Log.d("Turno", "Es mi turno: " + esMiTurno);  // Asegurarnos de que es el turno correcto
            if (esMiTurno) {
                // Crear animación de cambio de colores
                ValueAnimator colorAnim = ValueAnimator.ofArgb(
                        Color.parseColor("#D50032"),
                        Color.parseColor("#00FF00"),
                        Color.parseColor("#4B5320"),
                        Color.parseColor("#FFEB3B"),
                        Color.parseColor("#1E90FF")
                );
                colorAnim.setDuration(2000); // Duración de la animación
                colorAnim.setRepeatCount(0); // No repetir la animación
                colorAnim.setInterpolator(new android.view.animation.LinearInterpolator());

                // Hacer la animación en el hilo principal para que la UI se actualice correctamente
                colorAnim.addUpdateListener(animator -> cuadroJugar.setBackgroundColor((int) animator.getAnimatedValue()));

                colorAnim.start();

                // Después de que se termine la animación, seleccionamos un color aleatorio
                colorAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        // Detener la animación y seleccionar un color aleatorio
                        int colorSeleccionado = colores[new Random().nextInt(colores.length)];

                        // Actualizar la UI con el color seleccionado
                        cuadroJugar.setBackgroundColor(colorSeleccionado);

                        // Asociar el color con la categoría
                        String categoriaSeleccionada = obtenerCategoriaPorColor(colorSeleccionado);

                        // Iniciar la actividad de preguntas con la categoría seleccionada
                        Intent intent = new Intent(activity_jugar.this, activity_preguntas.class);
                        intent.putExtra("categoria", categoriaSeleccionada); // Pasamos la categoría como extra
                        startActivity(intent);
                    }
                });
            } else {
                // Si no es el turno correcto
                runOnUiThread(() -> Toast.makeText(activity_jugar.this, "No es tu turno, espera...", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String obtenerCategoriaPorColor(int color) {
        // Aquí asociamos el color con la categoría correspondiente
        if (color == Color.parseColor("#00FF00")) {
            return "Mundo Sobrenatural";
        } else if (color == Color.parseColor("#D50032")) {
            return "Cine";
        } else if (color == Color.parseColor("#4B5320")) {
            return "Sabores del Mundo";
        } else if (color == Color.parseColor("#FFEB3B")) {
            return "Risas y Memes";
        } else if (color == Color.parseColor("#1E90FF")) {
            return "Gamer";
        }
        return "Cine"; // Si no es ninguno de estos colores, por defecto asignamos "Random"
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
                e.printStackTrace();
            }
        }).start();
    }

    private void desconectarServidor() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        desconectarServidor();
    }
}
