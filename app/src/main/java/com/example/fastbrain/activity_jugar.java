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
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class activity_jugar extends AppCompatActivity {

    private ImageView diceImageView;
    private Button btnSalir;
    private ImageButton dadoButton;
    private ImageButton botonPerfil; // Botón de perfil
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

        // Inicializar vistas
        TextView emailUsuarioTextView = findViewById(R.id.emailUsuarioTextView);
        TextView usuarioTextView = findViewById(R.id.usuarioTextView);
        cuadroJugar = findViewById(R.id.cuadrojugar);
        diceImageView = findViewById(R.id.dado);
        dadoButton = findViewById(R.id.dado);
        btnSalir = findViewById(R.id.btnSalir);
        botonPerfil = findViewById(R.id.boton_perfil); // Botón de perfil correctamente inicializado

        // Detectar clic en el LinearLayout para animación de color
        cuadroJugar.setOnClickListener(v -> iniciarAnimacionColor());

        // Recibir datos desde otra actividad
        int codigoSala = getIntent().getIntExtra("codigo_sala", -1);
        String emailUsuario = getIntent().getStringExtra("email");

        // Mostrar código de sala y email del usuario
        emailUsuarioTextView.setText(codigoSala != -1 ? "Código de sala: " + codigoSala : "Código de sala no disponible.");
        usuarioTextView.setText(emailUsuario != null && !emailUsuario.isEmpty() ? "Email del usuario: " + emailUsuario : "Email no disponible.");

        // Establecer la imagen inicial del dado
        diceImageView.setImageResource(R.drawable.dado_1);

        // Configurar el botón para lanzar el dado
        dadoButton.setOnClickListener(v -> rollDice(diceImageView));

        // Configurar el botón para salir
        btnSalir.setOnClickListener(v -> {
            Intent intent = new Intent(activity_jugar.this, crearSala_unirSala.class);
            startActivity(intent);
            finish();
        });

        // Configurar el botón de perfil
        botonPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(activity_jugar.this, activity_perfil.class);
            startActivity(intent);
        });
    }

    private void rollDice(final ImageView imageDado) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageDado, "rotation", 0f, 360f);
        rotate.setDuration(700);
        rotate.start();
        rotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setRandomDiceImage(imageDado);
            }
        });
    }

    private void setRandomDiceImage(ImageView imageDado) {
        int randomNumber = new Random().nextInt(6) + 1;
        int drawableId = getResources().getIdentifier("dado_" + randomNumber, "drawable", getPackageName());
        imageDado.setImageResource(drawableId);
    }

    private void iniciarAnimacionColor() {
        final ValueAnimator colorAnim = ValueAnimator.ofArgb(colores[0], colores[1], colores[2], colores[3], colores[4]);
        colorAnim.setDuration(3000);
        colorAnim.setRepeatCount(0);
        colorAnim.addUpdateListener(animator -> cuadroJugar.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int colorAleatorio = colores[new Random().nextInt(colores.length)];
                cuadroJugar.setBackgroundColor(colorAleatorio);
            }
        });
        colorAnim.start();
    }
}
