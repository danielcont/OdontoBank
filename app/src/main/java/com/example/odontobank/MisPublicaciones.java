package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.odontobank.Model.Publicacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MisPublicaciones extends AppCompatActivity implements RecyclerViewAdapter.OnPublicacionListener {

    private List<Publicacion> mis_publicaciones;
    RecyclerView recyclerView;
    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_publicaciones);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        agregarDatos();
    }

    private void agregarDatos() {
        final String id = fuser.getUid();

        db.collection("publicacion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            mis_publicaciones = new ArrayList<>();

                            for(QueryDocumentSnapshot doc: task.getResult()) {
                                if(doc.get("uid").equals(id)) {
                                    Publicacion publicacion = doc.toObject(Publicacion.class);
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
        //viewMisPublicaciones.putExtra("publicacion", publicacion);
        startActivity(viewMisPublicaciones);
    }

}
