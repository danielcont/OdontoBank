package com.example.odontobank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();

        // Se pide la ubicación
        ActivityCompat.requestPermissions(Inicio.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        MyLocation g = new MyLocation(getApplicationContext());
        Location l = g.getLocation();
        if(l != null) {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            Toast.makeText(getApplicationContext(), "LAT: " + lat + "\n LON: " + lon, Toast.LENGTH_LONG).show();
        }
    }

}
