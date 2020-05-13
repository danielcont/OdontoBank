package com.example.odontobank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.odontobank.Model.Publicacion;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        final Publicacion informacion_atencion = getIntent().getParcelableExtra("publicacion");

    }
}
