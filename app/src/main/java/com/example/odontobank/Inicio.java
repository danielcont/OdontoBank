package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewAdapter.OnPublicacionListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser fuser;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private List<Publicacion> publicaciones;
    private RecyclerView recyclerViewInicio;
    private Context contextInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        // Se pide la ubicación
        ActivityCompat.requestPermissions(Inicio.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 123);

        MyLocation g = new MyLocation(getApplicationContext());
        Location l = g.getLocation();
        if(l != null) {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
        }

        // Menú
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.menu);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.news_feed);

        recyclerViewInicio = (RecyclerView) findViewById(R.id.recyclerView_publicacionesInicio);
        recyclerViewInicio.setHasFixedSize(true);
        LinearLayoutManager layoutManagerInicio = new LinearLayoutManager(contextInicio);
        recyclerViewInicio.setLayoutManager(layoutManagerInicio);

        datosInicio();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.news_feed:

                break;
            case R.id.agregar:
                Intent intentPublicar = new Intent(Inicio.this, Publicar.class);
                startActivity(intentPublicar);
                break;
            case R.id.profile:
                Intent intent = new Intent(Inicio.this, Perfil.class);
                startActivity(intent);
                break;
            case R.id.my_activities:
                Intent intent_myActivities = new Intent(Inicio.this, MisPublicaciones.class);
                startActivity(intent_myActivities);
                break;
            case R.id.logout:
                alerta();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void datosInicio() {
        final String id_ = fuser.getUid();

        db.collection("publicaciones")
                .orderBy("ts", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        publicaciones = new ArrayList<>();

                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            if(!doc.get("uid").equals(id_) && doc.get("activado").equals("1")) {
                                // poner dato por dato a mis publicaciones
                                // PENSAR COMO HACER EL SORT
                                double lat = Double.parseDouble(doc.get("latitud").toString());
                                double lon = Double.parseDouble(doc.get("longitud").toString());
                                Geocoder geocoder = new Geocoder(Inicio.this);
                                String city = "";

                                try {
                                    List<Address>addresses = geocoder.getFromLocation(lat, lon,1);
                                    if (geocoder.isPresent()) {
                                        if (addresses.size()>0) {
                                            Address returnAddress = addresses.get(0);

                                            city = returnAddress.getLocality();

                                        }
                                    } else {

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Publicacion publicacionIni = doc.toObject(Publicacion.class);
                                publicacionIni.setCiudad(city);
                                publicaciones.add(publicacionIni);
                            }
                        }
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(
                                publicaciones,
                                Inicio.this
                        );

                        recyclerViewInicio.setAdapter(adapter);
                    }
                });

    }

    public void alerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Desea salir de esta cuenta?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salir();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    private void salir() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onPublicacionClick(int position, Publicacion publicacion) {
        publicaciones.get(position);

        Intent viewPublicaciones = new Intent(this, Informacion.class);
        viewPublicaciones.putExtra("publicacion", publicacion);
        startActivity(viewPublicaciones);
    }

}
