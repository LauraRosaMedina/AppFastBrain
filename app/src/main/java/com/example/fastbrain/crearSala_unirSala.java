package com.example.fastbrain;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class crearSala_unirSala extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_sala_unir_sala);

        EditText edittextsala = findViewById(R.id.edittextsala);
        String email = getIntent().getStringExtra("email");
        Log.d("EmailDebug", "Email recibido en crearSala_unirSala: " + email);

        int codigoSala = getIntent().getIntExtra("codigo_sala", -1);
        if (codigoSala != -1) {
            edittextsala.setText(String.valueOf(codigoSala));
        }

        ConstraintLayout layout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(500);
        animationDrawable.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button botonCrearSala = findViewById(R.id.botoncrearsala);
        botonCrearSala.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this, ajustespartida.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });

        Button boton_unirSala = findViewById(R.id.boton_unirSala);
        boton_unirSala.setOnClickListener(v -> {
            String codigoSalaString = edittextsala.getText().toString();
            try {
                int codigoJuego = Integer.parseInt(codigoSalaString);

                // NO conectar al servidor aquí, solo pasar los datos a la siguiente actividad
                Intent intent = new Intent(crearSala_unirSala.this, activity_jugar.class);
                intent.putExtra("codigo_sala", codigoJuego);
                intent.putExtra("email", email);
                intent.putExtra("es_mi_turno", true);
                startActivity(intent);
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(crearSala_unirSala.this, "Código de sala inválido", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView iconoPerfil = findViewById(R.id.boton_perfil);
        iconoPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(crearSala_unirSala.this, activity_perfil.class);
            startActivity(intent);
        });
    }
}
