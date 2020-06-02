package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarPublicacion extends AppCompatActivity {

    private TextView nombreEditpu, correoEditpu;
    private EditText celularEdit, descripcionEdit;
    private Button editPublicacion, backButtonEdit;
    private Spinner spinnerEdit;

    DocumentReference ref;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacion);

        final Publicacion informacion_ = getIntent().getParcelableExtra("publicacion_");

        backButtonEdit = findViewById(R.id.editPublisherBackButton);
        backButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editPublicacion = findViewById(R.id.editPublishButton);
        editPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatos(informacion_);
            }
        });

        spinnerEdit = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdit.setAdapter(adapter);
        //spinnerEdit.setOnItemSelectedListener(this);

        int spinnerPosition = adapter.getPosition(informacion_.getAtencion_medica());
        spinnerEdit.setSelection(spinnerPosition);

        nombreEditpu = findViewById(R.id.nombrePublicadorEdit);
        nombreEditpu.setText(informacion_.getNombre());

        correoEditpu = findViewById(R.id.editPublisherCorreo);
        correoEditpu.setText(informacion_.getCiudad());


        descripcionEdit = findViewById(R.id.editPublisherDescripcion);
        descripcionEdit.setText(informacion_.getDescripcion());

        celularEdit = findViewById(R.id.editPublisherCelular);
        celularEdit.setText(informacion_.getContacto());

    }

    private void updateDatos(Publicacion info) {
        db = FirebaseFirestore.getInstance();
        ref = db.collection("publicaciones").document(info.getpUid());

        ref.update(
                "contacto", celularEdit.getText().toString(),
                "descripcion", descripcionEdit.getText().toString(),
                "atencion_medica", spinnerEdit.getSelectedItem().toString()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });

    }
}
