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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    DocumentReference ref;
    private FirebaseUser fuser;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Spinner filterSpin;
    TextView aplicarButton;

    private List<Publicacion> publicaciones;
    private RecyclerView recyclerViewInicio;
    private Context contextInicio;
    private double lat1, lon1, lat2,lon2;
    String cc = "";

    String[] arrayAtencion = {"", "Cirugía Oral", "Odontología Preventiva", "Periodoncia", "Terapéutica Dental",
            "Endodoncia", "Prótesis Dental", "Odontología Infantil", "Dolor Orofacial", "Ortodoncia", "Cariología", "Implantología"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        // Menú
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.menu);
        filterSpin = findViewById(R.id.filterSpinner);
        aplicarButton = findViewById(R.id.aplicar);

        aplicarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosInicio();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filterSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpin.setAdapter(adapter);
        //filterSpin.setOnItemSelectedListener(this);

        int spinnerPosition = adapter.getPosition("Default");
        filterSpin.setSelection(spinnerPosition);

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

        // Se pide la ubicación
        ActivityCompat.requestPermissions(Inicio.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 123);

        MyLocation g = new MyLocation(getApplicationContext());
        Location l = g.getLocation();
        if(l != null) {
            lat1 = l.getLatitude();
            lon1 = l.getLongitude();
        }

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
                                lat2 = Double.parseDouble(doc.get("latitud").toString());
                                lon2 = Double.parseDouble(doc.get("longitud").toString());
                                Geocoder geocoder = new Geocoder(Inicio.this);
                                String city = "";

                                double distancia = distanceBetween(lat1, lon1, lat2, lon2);

                                try {
                                    List<Address>addresses = geocoder.getFromLocation(lat2, lon2,1);
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

                                int[] especialidades = new int[4];
                                cc = "";

                                ref = db.document("estudiantes/" + id_);
                                ref.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.exists()) {
                                                    cc = documentSnapshot.getString("carrera");
                                                }
                                            }
                                        });

                                if (distancia <= 55000 && doc.get("atencion_medica").equals(filterSpin.getSelectedItem().toString())) {
                                    Publicacion publicacionIni = doc.toObject(Publicacion.class);
                                    publicacionIni.setCiudad(city);
                                    publicaciones.add(publicacionIni);
                                } else if(distancia <= 55000 && filterSpin.getSelectedItem().toString().equals("Default")) {
                                    if (cc.equals("Lic. Cirujano Dentista")) {
                                        especialidades = new int[]{1, 2, 8, 0};
                                    } else if(cc.equals("Especialidad Endodoncia")) {
                                        especialidades = new int[]{4, 5, 0, 0};
                                    } else if(cc.equals("Especialidad Cirugía Oral y Maxilofacial")) {
                                        especialidades = new int[]{1, 4, 8, 0};
                                    } else if((cc.equals("Maestría Endodoncia"))) {
                                        especialidades = new int[]{5, 7, 10, 0};
                                    } else if((cc.equals("Maestría Periodoncia"))) {
                                        especialidades = new int[]{3, 6, 11, 0};
                                    } else if((cc.equals("Maestría Odontopediatría"))) {
                                        especialidades = new int[]{7, 0, 0, 0};
                                    } else if((cc.equals("Maestría en Ortodoncia"))) {
                                        especialidades = new int[]{6, 9, 0, 0};
                                    } else if((cc.equals("Maestría en Prostodoncia"))) {
                                        especialidades = new int[]{6, 11, 0, 0};
                                    } else if((cc.equals("Maestría en Odontología Avanzada"))) {
                                        especialidades = new int[]{5, 7, 9, 0};
                                    }

                                    for(int i = 0; i <= especialidades.length; i ++) {
                                        if(doc.get("atencion_medica").equals(arrayAtencion[i])) {
                                            Publicacion publicacionIni = doc.toObject(Publicacion.class);
                                            publicacionIni.setCiudad(city);
                                            publicaciones.add(publicacionIni);
                                        }
                                    }
                                }

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

    private double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double distance;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);

        return distance;
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
