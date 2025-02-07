package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fastbrain.client.cliente;

import java.util.ArrayList;
import java.util.Collections;

public class activity_preguntas extends AppCompatActivity {

    private TextView preguntaTextView, timerTextView, puntajeTextView;
    private Button opcion1Button, opcion2Button, opcion3Button, opcion4Button;
    private ArrayList<Integer> puntajesJugadores;
    private int turnoActual = 0;  // El turno actual del jugador (0 es el primer jugador)
    private String categoriaPregunta;
    private String[] opciones;
    private String respuestaCorrecta;
    private ArrayList<Pregunta> preguntas;
    private CountDownTimer temporizador;
    private int tiempoRestante = 10000;  // 10 segundos para responder
    private cliente miCliente;  // Instancia de la clase cliente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        // Inicializar vistas
        preguntaTextView = findViewById(R.id.preguntaTextView);
        opcion1Button = findViewById(R.id.opcion1Button);
        opcion2Button = findViewById(R.id.opcion2Button);
        opcion3Button = findViewById(R.id.opcion3Button);
        opcion4Button = findViewById(R.id.opcion4Button);
        timerTextView = findViewById(R.id.timerTextView);
        puntajeTextView = findViewById(R.id.puntajeTextView); // Para mostrar puntaje actual

        // Recibir la categoría seleccionada desde la actividad anterior
        categoriaPregunta = getIntent().getStringExtra("categoria");

        // Mostrar la categoría en el TextView (opcional)
        TextView categoriaTextView = findViewById(R.id.categoriaTextView);
        categoriaTextView.setText("Categoría: " + categoriaPregunta);

        // Inicializar lista de puntajes para varios jugadores
        puntajesJugadores = new ArrayList<>();
        puntajesJugadores.add(0);  // Empezamos con el puntaje del primer jugador en 0

        // Generar las preguntas aleatorias para la categoría seleccionada
        cargarPreguntas(categoriaPregunta);

        // Mostrar la primera pregunta
        mostrarPregunta();

        // Iniciar el temporizador
        iniciarTemporizador();

        // Configurar los botones de respuesta
        opcion1Button.setOnClickListener(v -> verificarRespuesta(opcion1Button.getText().toString()));
        opcion2Button.setOnClickListener(v -> verificarRespuesta(opcion2Button.getText().toString()));
        opcion3Button.setOnClickListener(v -> verificarRespuesta(opcion3Button.getText().toString()));
        opcion4Button.setOnClickListener(v -> verificarRespuesta(opcion4Button.getText().toString()));
    }

    private void cargarPreguntas(String categoria) {
        preguntas = new ArrayList<>();

        // Se agregan preguntas de cada categoría de la misma forma que ya lo tenías
        // Por ejemplo:
        if (categoria.equals("Sobrenatural")) {
            preguntas.add(new Pregunta("¿Quién es el conocido 'Rey de los Vampiros' en la cultura popular?", "Nosferatu", "Vlad Tepes", "Drácula", "Lestat de Lioncourt", 2));
            preguntas.add(new Pregunta("¿Cómo se llama el ser mitológico que convierte a las personas en piedra con su mirada?", "Medusa", "Fénix", "Sirena", "Minotauro", 0));
            preguntas.add(new Pregunta("¿Qué significa la palabra 'fantasma'?", "Aliento de viento", "Espíritu de un muerto", "Ser de otro planeta", "Animal mitológico", 1));
            preguntas.add(new Pregunta("¿Qué criatura mitológica tiene la cabeza de un león, el cuerpo de una cabra y la cola de una serpiente?", "Hidra", "Quimera", "Minotauro", "Pegaso", 1));
            preguntas.add(new Pregunta("¿En qué cultura se originó la leyenda de los 'hombres lobo'?", "China", "Grecia", "Europa", "Egipto", 2));
        }

        // Mezclar las preguntas para que no sean predecibles
        Collections.shuffle(preguntas);
    }

    private void mostrarPregunta() {
        if (!preguntas.isEmpty()) {
            Pregunta pregunta = preguntas.get(0);  // Tomamos la primera pregunta de la lista
            preguntaTextView.setText(pregunta.getPregunta());
            opciones = new String[]{pregunta.getOpcion1(), pregunta.getOpcion2(), pregunta.getOpcion3(), pregunta.getOpcion4()};
            respuestaCorrecta = opciones[pregunta.getRespuestaCorrecta()];  // La opción correcta

            // Asignar las opciones a los botones
            opcion1Button.setText(opciones[0]);
            opcion2Button.setText(opciones[1]);
            opcion3Button.setText(opciones[2]);
            opcion4Button.setText(opciones[3]);
        } else {
            // No hay más preguntas, finalizar el juego
            Toast.makeText(this, "¡No hay más preguntas! Fin del juego.", Toast.LENGTH_SHORT).show();
            finalizarJuego();
        }
    }

    private void iniciarTemporizador() {
        temporizador = new CountDownTimer(tiempoRestante, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) millisUntilFinished;
                timerTextView.setText("Tiempo restante: " + tiempoRestante / 1000);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("¡Tiempo agotado!");
                verificarRespuesta("");  // Si el tiempo se acaba, el jugador no puede responder
            }
        };

        temporizador.start();
    }

    private void verificarRespuesta(String respuestaSeleccionada) {
        boolean respuestaCorrectaFlag = respuestaSeleccionada.equals(respuestaCorrecta);

        if (respuestaCorrectaFlag) {
            puntajesJugadores.set(turnoActual, puntajesJugadores.get(turnoActual) + 50);
            Toast.makeText(this, "¡Correcto! +50 puntos", Toast.LENGTH_SHORT).show();
        } else {
            puntajesJugadores.set(turnoActual, puntajesJugadores.get(turnoActual) - 20);
            Toast.makeText(this, "¡Incorrecto! -20 puntos", Toast.LENGTH_SHORT).show();
            enviarFinTurno(); // 🔥 Finaliza el turno automáticamente si falla
        }

        puntajeTextView.setText("Puntaje Jugador " + (turnoActual + 1) + ": " + puntajesJugadores.get(turnoActual));

        // Si aún quedan preguntas, mostrar la siguiente
        if (turnoActual < puntajesJugadores.size() - 1) {
            turnoActual++;
            mostrarPregunta();
            tiempoRestante = 10000;
            iniciarTemporizador();
        } else {
            Toast.makeText(this, "Todos han jugado, fin del turno.", Toast.LENGTH_SHORT).show();
            mostrarPregunta();
        }
    }

    private void enviarFinTurno() {
        if (miCliente != null) {
            miCliente.enviarFinTurno(); // Aquí llamas al método que ya está en la clase cliente
        }
    }

    private void finalizarJuego() {
        // Aquí mostramos los puntajes finales y pasamos a otra actividad o reiniciamos
        String resultado = "Puntajes finales:\n";
        for (int i = 0; i < puntajesJugadores.size(); i++) {
            resultado += "Jugador " + (i + 1) + ": " + puntajesJugadores.get(i) + " puntos\n";
        }

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        // Pasar a la actividad final o reiniciar el juego
        Intent intent = new Intent(activity_preguntas.this, activity_jugar.class);
        intent.putExtra("puntajesJugadores", puntajesJugadores);  // Pasamos los puntajes a la actividad siguiente
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (temporizador != null) {
            temporizador.cancel();  // Cancelar temporizador cuando la actividad se detiene
        }
    }
}
