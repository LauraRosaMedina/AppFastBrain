package com.example.fastbrain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ajustespartida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustespartida);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btn_baja = findViewById(R.id.btn_baja);
        Button btn_media = findViewById(R.id.btn_media);
        Button btn_alta = findViewById(R.id.btn_alta);


        btn_baja.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad baja", Toast.LENGTH_SHORT).show();

        });


        btn_media.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad media", Toast.LENGTH_SHORT).show();
        });

        btn_alta.setOnClickListener(v ->{
            Toast.makeText(this, "Dificultad alta", Toast.LENGTH_SHORT).show();
        });
    }





}