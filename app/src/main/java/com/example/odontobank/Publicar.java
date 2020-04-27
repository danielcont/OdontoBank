package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Publicar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView backButton;
    private TextView nombre, correo;
    private EditText descripcion;
    private Button publish_button;
    private CircleImageView image_profile;

    FirebaseAuth mAuth;
    FirebaseUser fuser;

    StorageReference mStorageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        fuser = mAuth.getCurrentUser();

        nombre = (TextView) findViewById(R.id.nombre_publicador);
        correo = (TextView) findViewById(R.id.puCorreo);
        image_profile = findViewById(R.id.puFoto);
        descripcion = (EditText) findViewById(R.id.descripcion);

        // Botón de regresar
        backButton = findViewById(R.id.puBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        // Botón de publicar
        publish_button = findViewById(R.id.publishButton);
        publish_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                publicar();
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int spinnerPosition = adapter.getPosition("Seleccionar Opción");
        spinner.setSelection(spinnerPosition);

        if(fuser != null) {
            if(fuser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(fuser.getPhotoUrl())
                        .into(image_profile);
            }
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = db.document("estudiantes/" + uid);

        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            String sCorreo = documentSnapshot.getString("correo");
                            correo.setText(sCorreo);

                            String sNombre = documentSnapshot.getString("nombre");
                            String sApellido = documentSnapshot.getString("apellido");
                            nombre.append(sNombre + " " + sApellido);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void publicar() {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
