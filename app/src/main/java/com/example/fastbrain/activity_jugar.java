package com.example.fastbrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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

import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private ImageView diceImageView;
    private Button btnSalir;
    private ImageButton dadoButton; // ImageButton para lanzar el dado

    // Colores para el LinearLayout
    private LinearLayout cuadroJugar;
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

        TextView emailUsuarioTextView = findViewById(R.id.emailUsuarioTextView);
        TextView usuarioTextView = findViewById(R.id.usuarioTextView);
        cuadroJugar = findViewById(R.id.cuadrojugar);  // LinearLayout donde harás el clic

        // Detectar el clic en el LinearLayout
        cuadroJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la animación
                iniciarAnimacionColor();
            }
        });

        // Recibir el código de la sala y el email enviado desde crearSala_unirSala
        int codigoSala = getIntent().getIntExtra("codigo_sala", -1);
        String emailUsuario = getIntent().getStringExtra("email");

        // Establecer el texto del código de sala en el TextView correspondiente
        if (codigoSala != -1) {
            emailUsuarioTextView.setText("Código de sala: " + codigoSala);  // Añadir código de sala
        } else {
            emailUsuarioTextView.setText("Código de sala no disponible.");
        }

        // Establecer el texto del email en el otro TextView
        if (emailUsuario != null && !emailUsuario.isEmpty()) {
            usuarioTextView.setText("Email del usuario: " + emailUsuario);  // Añadir email
            Log.d("ActivityJugar", "Email: " + emailUsuario);
        } else {
            usuarioTextView.setText("Email no disponible.");
        }

        // Inicialización de los elementos de la UI
        diceImageView = findViewById(R.id.dado); // ImageView del dado
        dadoButton = findViewById(R.id.dado); // Botón para lanzar el dado
        btnSalir = findViewById(R.id.btnSalir); // Botón de "Salir"

        // Establecer la imagen inicial del dado
        diceImageView.setImageResource(R.drawable.dado_1);

        // Configurar el listener para lanzar el dado
        dadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice(diceImageView);
            }
        });

        // Configurar el listener para el botón de salir
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para ir a la actividad crearSala_unirSala
                Intent intent = new Intent(activity_jugar.this, crearSala_unirSala.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para que no se pueda volver atrás
            }
        });
    }

    private void rollDice(final ImageView imageDado) {
        // Animación de rotación
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageDado, "rotation", 0f, 360f);
        rotate.setDuration(700); // Duración de la animación en milisegundos
        rotate.start();

        // Después de la animación, cambiamos la imagen del dado
        rotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setRandomDiceImage(imageDado);
            }
        });
    }

    private void setRandomDiceImage(ImageView imageDado) {
        // Generamos un número aleatorio entre 1 y 6 para simular el dado
        Random random = new Random();
        int randomNumber = random.nextInt(6) + 1; // Número aleatorio entre 1 y 6

        // Cambiar la imagen del dado según el número aleatorio
        switch (randomNumber) {
            case 1:
                imageDado.setImageResource(R.drawable.dado_1);
                break;
            case 2:
                imageDado.setImageResource(R.drawable.dado_2);
                break;
            case 3:
                imageDado.setImageResource(R.drawable.dado_3);
                break;
            case 4:
                imageDado.setImageResource(R.drawable.dado_4);
                break;
            case 5:
                imageDado.setImageResource(R.drawable.dado_5);
                break;
            case 6:
                imageDado.setImageResource(R.drawable.dado_6);
                break;
        }
    }

    private void iniciarAnimacionColor() {
        // Crear un ValueAnimator que simule un cambio de colores rápidos
        final ValueAnimator colorAnim = ValueAnimator.ofArgb(colores[0], colores[1], colores[2], colores[3], colores[4]);
        colorAnim.setDuration(3000);  // Duración de la animación (3 segundos)
        colorAnim.setRepeatCount(0);  // La animación no se repite infinitamente

        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // Cambiar el color del fondo del LinearLayout
                cuadroJugar.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        // Cuando la animación termine, detenerla en un color aleatorio
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Seleccionar un color aleatorio de la lista
                int colorAleatorio = colores[(int) (Math.random() * colores.length)];
                cuadroJugar.setBackgroundColor(colorAleatorio);  // Establecer el color final
                Toast.makeText(activity_jugar.this, "Color final: " + Integer.toHexString(colorAleatorio), Toast.LENGTH_SHORT).show();
            }
        });

        // Comenzar la animación
        colorAnim.start();
    }
}


