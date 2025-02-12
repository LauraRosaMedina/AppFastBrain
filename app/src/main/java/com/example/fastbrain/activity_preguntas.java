package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class activity_preguntas extends AppCompatActivity {

    private TextView preguntaTextView, opcion1, opcion2, opcion3, opcion4, timerTextView;
    private String categoriaPregunta;
    private Pregunta preguntaActual;
    private List<Pregunta> preguntasLista = new ArrayList<>();
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        // Obtener la categoría desde el Intent
        categoriaPregunta = getIntent().getStringExtra("categoria");
        Log.d("CategoriaRecibida", "Categoría recibida: " + categoriaPregunta);

        // Inicializar vistas
        preguntaTextView = findViewById(R.id.preguntaTextView);
        opcion1 = findViewById(R.id.opcion1);
        opcion2 = findViewById(R.id.opcion2);
        opcion3 = findViewById(R.id.opcion3);
        opcion4 = findViewById(R.id.opcion4);
        timerTextView = findViewById(R.id.timerTextView);

        // Cargar preguntas desde el JSON
        cargarPreguntasDesdeJSON();

        // Mostrar la primera pregunta y comenzar el temporizador
        mostrarPregunta();
        iniciarTemporizador();
    }

    // Método para leer el JSON y cargar preguntas en la lista
    private void cargarPreguntasDesdeJSON() {
        try {
            // Leer el archivo JSON
            InputStream inputStream = getResources().openRawResource(R.raw.preguntas);
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Convertir JSON a un Map<String, List<Pregunta> >
            Type type = new TypeToken<Map<String, List<Pregunta>>>() {}.getType();
            Map<String, List<Pregunta>> preguntasMap = new Gson().fromJson(reader, type);

            // Obtener preguntas de la categoría seleccionada
            preguntasLista = preguntasMap.getOrDefault(categoriaPregunta, new ArrayList<>());
            Collections.shuffle(preguntasLista); // Mezclar preguntas

        } catch (Exception e) {
            Log.e("ErrorJSON", "Error al cargar preguntas: " + e.getMessage());
        }
    }

    // Método para mostrar una pregunta en pantalla
    private void mostrarPregunta() {
        if (preguntasLista.isEmpty()) {
            Log.e("Error", "No hay preguntas disponibles para la categoría: " + categoriaPregunta);
            return;
        }

        preguntaActual = preguntasLista.remove(0);
        preguntaTextView.setText(preguntaActual.getPregunta());

        // Obtener las opciones y mezclarlas
        List<String> opciones = new ArrayList<>();
        opciones.add(preguntaActual.getRespuestaCorrecta());
        Collections.addAll(opciones, preguntaActual.getOpcionesIncorrectas());
        Collections.shuffle(opciones);

        // Asignar opciones a los TextView
        opcion1.setText(opciones.get(0));
        opcion2.setText(opciones.get(1));
        opcion3.setText(opciones.get(2));
        opcion4.setText(opciones.get(3));

        // Hacer que los TextView sean clickeables
        opcion1.setOnClickListener(v -> verificarRespuesta(opcion1.getText().toString()));
        opcion2.setOnClickListener(v -> verificarRespuesta(opcion2.getText().toString()));
        opcion3.setOnClickListener(v -> verificarRespuesta(opcion3.getText().toString()));
        opcion4.setOnClickListener(v -> verificarRespuesta(opcion4.getText().toString()));
    }

    private void verificarRespuesta(String respuestaSeleccionada) {
        if (respuestaSeleccionada.equals(preguntaActual.getRespuestaCorrecta())) {
            Log.d("Respuesta", "Correcto");
            Toast.makeText(this, "¡Respuesta correcta!", Toast.LENGTH_SHORT).show(); // Toast para respuesta correcta
            if (!preguntasLista.isEmpty()) {
                mostrarPregunta();  // Mostrar la siguiente pregunta si hay más
                iniciarTemporizador();  // Reiniciar el temporizador
            }
        } else {
            Log.d("Respuesta", "Incorrecto");
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show(); // Toast para respuesta incorrecta
            // Si la respuesta es incorrecta o no hay tiempo, ir a la actividad 'activity_jugar' y perder el turno
            Intent intent = new Intent(activity_preguntas.this, activity_jugar.class);
            startActivity(intent);
            finish();  // Terminar la actividad actual para que no se quede en la pila
        }
    }

    // Iniciar el temporizador
    private void iniciarTemporizador() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Actualizar el temporizador cada segundo
                int segundosRestantes = (int) (millisUntilFinished / 1000);
                timerTextView.setText(String.valueOf(segundosRestantes));
            }

            @Override
            public void onFinish() {
                // Cuando el temporizador llega a cero, perder el turno y regresar a 'activity_jugar'
                Toast.makeText(activity_preguntas.this, "¡Tiempo agotado!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_preguntas.this, activity_jugar.class);
                startActivity(intent);
                finish();  // Terminar la actividad actual para que no se quede en la pila
            }
        };
        countDownTimer.start();
    }

    // Clase para mapear el JSON con Gson
    private static class Pregunta {
        private String pregunta;
        private String respuesta_correcta;
        private String[] opciones_incorrectas;

        public String getPregunta() {
            return pregunta;
        }

        public String getRespuestaCorrecta() {
            return respuesta_correcta;
        }

        public String[] getOpcionesIncorrectas() {
            return opciones_incorrectas;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();  // Detener el temporizador si la actividad se destruye
        }
    }
}
