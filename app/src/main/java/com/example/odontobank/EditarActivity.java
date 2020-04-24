package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    private ImageView back_button;
    private EditText correo, nombre, apellido, escuela;
    private Button modificiar_boton;

    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;

    String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        back_button = findViewById(R.id.eBackButton);
        modificiar_boton = findViewById(R.id.eRegistro);
        correo = findViewById(R.id.eCorreo);
        nombre = findViewById(R.id.eNombre);
        apellido = findViewById(R.id.eApellido);
        escuela = findViewById(R.id.eEscuela);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfil();
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = db.document("estudiantes/" + uid);

        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            String sCorreo = documentSnapshot.getString("correo");
                            correo.setText(sCorreo);
                            correo.setEnabled(false);

                            String sNombre = documentSnapshot.getString("nombre");
                            nombre.setText(sNombre, TextView.BufferType.EDITABLE);

                            String sApellido = documentSnapshot.getString("apellido");
                            apellido.setText(sApellido, TextView.BufferType.EDITABLE);

                            String sEscuela = documentSnapshot.getString("escuela");
                            escuela.setText(sEscuela, TextView.BufferType.EDITABLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        modificiar_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
    }

    private void validar() {
        mAuth = FirebaseAuth.getInstance();

        boolean nombre_validado = false, apellido_validado = false, escuela_validado = false;

        TextView nombre_text = (TextView)findViewById(R.id.eNombre_text);
        TextView apellido_text = (TextView)findViewById(R.id.eApellido_text);
        TextView escuela_text = (TextView)findViewById(R.id.eEscuela_text);

        String sCorreo = correo.getText().toString();

        String sNombre = nombre.getText().toString();

        String sApellido = apellido.getText().toString();

        String sEscuela = escuela.getText().toString();

        if(sNombre.length() != 0) { nombre_validado = true; nombre_text.setVisibility(View.INVISIBLE); } else { nombre_text.setVisibility(View.VISIBLE); }

        if(sApellido.length() != 0) { apellido_validado = true; apellido_text.setVisibility(View.INVISIBLE); } else { apellido_text.setVisibility(View.VISIBLE); }

        if(sEscuela.length() != 0) { escuela_validado = true; escuela_text.setVisibility(View.INVISIBLE); } else { escuela_text.setVisibility(View.VISIBLE); }

        if((nombre_validado && apellido_validado && escuela_validado) == true) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("apellido", sApellido);
            datos.put("escuela", sEscuela);
            datos.put("nombre", sNombre);
            datos.put("correo", sCorreo);


            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref = db.document("estudiantes/" + uid);

            ref.set(datos)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    perfil();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });

        }
    }

    private void perfil() {
        finish();
        Intent intent = new Intent(EditarActivity.this, Perfil.class);
        startActivity(intent);
    }

}
