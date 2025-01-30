package com.example.fastbrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private ImageView diceImageView;
    private Button btnSalir;
    private ImageButton dadoButton; // ImageButton para lanzar el dado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

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
}
