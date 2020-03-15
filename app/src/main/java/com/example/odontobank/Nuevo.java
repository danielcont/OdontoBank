package com.example.odontobank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Nuevo extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText editTextCorreo, editTextPassword, editTextNombre, editTextApellido, editTextEscuela;

    private ImageView back_button;
    private Button registrar_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        editTextCorreo = findViewById(R.id.correo);
        editTextPassword = findViewById(R.id.password);
        editTextNombre = findViewById(R.id.nombre);
        editTextApellido = findViewById(R.id.apellido);
        editTextEscuela = findViewById(R.id.escuela);

        back_button = findViewById(R.id.backButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registrar_button = findViewById(R.id.registro);
        registrar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();

            }
        });

    }

    private void validar() {
        mAuth = FirebaseAuth.getInstance();

        boolean correo_validado = false, password_validado = false, nombre_validado = false, apellido_validado = false, escuela_validado = false;

        TextView correo_text = (TextView)findViewById(R.id.correo_text);
        TextView password_text = (TextView)findViewById(R.id.password_text);
        TextView nombre_text = (TextView)findViewById(R.id.nombre_text);
        TextView apellido_text = (TextView)findViewById(R.id.apellido_text);
        TextView escuela_text = (TextView)findViewById(R.id.escuela_text);

        String email = editTextCorreo.getText().toString();
        String patron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String password = editTextPassword.getText().toString();

        String nombre = editTextNombre.getText().toString();

        String apellido = editTextApellido.getText().toString();

        String escuela = editTextEscuela.getText().toString();

        if(email.matches(patron) && email.length() > 0) { correo_validado = true; correo_text.setVisibility(View.INVISIBLE); } else { correo_text.setVisibility(View.VISIBLE); }

        if(password.length() > 6) { password_validado = true; password_text.setVisibility(View.INVISIBLE); } else { password_text.setVisibility(View.VISIBLE); }

        if(nombre.length() != 0) { nombre_validado = true; nombre_text.setVisibility(View.INVISIBLE); } else { nombre_text.setVisibility(View.VISIBLE); }

        if(apellido.length() != 0) { apellido_validado = true; apellido_text.setVisibility(View.INVISIBLE); } else { apellido_text.setVisibility(View.VISIBLE); }

        if(escuela.length() != 0) { escuela_validado = true; escuela_text.setVisibility(View.INVISIBLE); } else { escuela_text.setVisibility(View.VISIBLE); }

        if((correo_validado && password_validado && nombre_validado && apellido_validado && escuela_validado) == true) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("apellido", apellido);
            datos.put("correo", email);
            datos.put("escuela", escuela);
            datos.put("nombre", nombre);

            createAccount(email, password,datos);
        }
    }

    private void createAccount(String email, String password, final Map<String, Object> datos) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            createUserData(user, datos);
                        } else {
                            Toast.makeText(Nuevo.this, "Ese correo ya existe en la base de datos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserData(final FirebaseUser user, Map<String, Object> datos) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();


        db.collection("estudiantes")
                .document(user.getUid())
                .set(datos)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Nuevo.this, "Favor de confirmar correo electrónico",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            progressDialog.dismiss();
                            user.delete();
                            Toast.makeText(Nuevo.this, "Algo no salió bien",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
