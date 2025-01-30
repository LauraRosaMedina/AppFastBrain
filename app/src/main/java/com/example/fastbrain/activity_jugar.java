package com.example.fastbrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private ImageView diceImageView;
    private Button registerButton;
    private ImageButton diceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        // Inicialización de los elementos de la UI
        diceImageView = findViewById(R.id.dice); // Este es el ImageView para mostrar el dado
        registerButton = findViewById(R.id.registerButton); // Botón de "Salir"
        diceButton = findViewById(R.id.dice); // Botón de dado (ImageButton)

        // Establecer el dado con una imagen inicial
        diceImageView.setImageResource(R.drawable.dado_1);

        // Configurar el listener para el botón de dado
        diceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice(diceImageView);
            }
        });

        // Configurar el listener para el botón de salir
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSalirButtonClick(v);  // Llamamos al método para salir y redirigir
            }
        });
    }

    // Método que maneja la rotación del dado
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

    // Cambiar la imagen del dado según un número aleatorio
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

    // Método que se ejecuta cuando el botón de "Salir" es presionado
    public void onSalirButtonClick(View view) {
        // Agrega un log para ver si el método es ejecutado
        Log.d("activity_jugar", "Botón 'Salir' presionado");

        // Intent para abrir la actividad crearSala_unirSala
        Intent intent = new Intent(activity_jugar.this, crearSala_unirSala.class);
        startActivity(intent);  // Inicia la nueva actividad
    }
}
