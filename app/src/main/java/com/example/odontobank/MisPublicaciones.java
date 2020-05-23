package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MisPublicaciones extends AppCompatActivity implements RecyclerViewAdapter.OnPublicacionListener {

    private List<Publicacion> mis_publicaciones;
    RecyclerView recyclerView;
    private Context context;
    private ImageView button;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser fuser;
    private DocumentReference usuRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        button = findViewById(R.id.misBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_publicaciones);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        agregarDatos();
    }

    private void agregarDatos() {
        final String id = fuser.getUid();

        db.collection("publicaciones")
                .orderBy("uid", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            mis_publicaciones = new ArrayList<>();

                            for(QueryDocumentSnapshot doc: task.getResult()) {
                                if(doc.get("uid").equals(id)) {
                                    // poner dato por dato a mis publicaciones
                                    // PENSAR COMO HACER EL SORT
                                    double lat = Double.parseDouble(doc.get("latitud").toString());
                                    double lon = Double.parseDouble(doc.get("longitud").toString());
                                    Geocoder geocoder = new Geocoder(MisPublicaciones.this);
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

                                    Publicacion publicacion = doc.toObject(Publicacion.class);
                                    publicacion.setCiudad(city);
                                    mis_publicaciones.add(publicacion);
                                }
                            }

                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(
                                    mis_publicaciones,
                                    MisPublicaciones.this
                            );

                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(MisPublicaciones.this, "Se presentó un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onPublicacionClick(int position, Publicacion publicacion) {
        mis_publicaciones.get(position);

        Intent viewMisPublicaciones = new Intent(this, Informacion.class);
        viewMisPublicaciones.putExtra("publicacion", publicacion);
        startActivity(viewMisPublicaciones);
    }


}
