package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private TextView editTextCorreo, editTextPassword;

    private ImageView back_button;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextCorreo = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        // Botón para registrar
        TextView nuevo = findViewById(R.id.registrar);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Nuevo.class);
                startActivity(intent);
            }

        });

        // Botón de regresar
        back_button = findViewById(R.id.logBackButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_correo();
            }
        });



    }

    public void validar_correo() {
        boolean correo_validado = false, password_validado = false;

        TextView correo_text = (TextView)findViewById(R.id.correo_text);
        TextView password_text = (TextView)findViewById(R.id.password_text);

        String email = editTextCorreo.getText().toString();
        String patron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String password = editTextPassword.getText().toString();

        if(email.matches(patron) && email.length() > 0) { correo_validado = true; correo_text.setVisibility(View.INVISIBLE); } else { correo_text.setVisibility(View.VISIBLE); }

        if(password.length() > 6) { password_validado = true; password_text.setVisibility(View.INVISIBLE); } else { password_text.setVisibility(View.VISIBLE); }

        if((password_validado && correo_validado) == true) { login_bd(email, password); }

    }

    public void login_bd(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    inicio();
                } else {
                    Toast.makeText(LoginActivity.this, "Los datos ingresados son incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void inicio() {
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
    }

}
