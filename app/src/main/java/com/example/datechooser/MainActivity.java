package com.example.datechooser;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FechaSelector fechaSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        fechaSelector = findViewById(R.id.fechaSelector);

        // Listener para cambios en la fecha
        fechaSelector.setOnFechaChangeListener((mes, anio) -> {
            //String mensaje = "Fecha seleccionada: " + (mes + 1) + "/" + anio;
            //Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
        });
    }
}