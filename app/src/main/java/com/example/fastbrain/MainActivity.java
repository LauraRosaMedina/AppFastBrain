
package com.example.fastbrain;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Usar un Handler para cambiar de actividad después de 3 segundos (3000 ms)
        new Handler().postDelayed(() -> {
            // Inicia la actividad de inicio de sesión
            Intent intent = new Intent(MainActivity.this, inicio.class);
            startActivity(intent);
            // Finaliza la actividad actual para que no regrese al presionar "atrás"
            finish();
        }, 3000);
    }


}