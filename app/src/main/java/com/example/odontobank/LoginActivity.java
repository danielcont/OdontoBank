package com.example.odontobank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ImageView back_button;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        back_button = findViewById(R.id.backButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean input_validado = validar_correo();
                if(input_validado == true) {
                    login_bd();

                }
            }
        });


    }

    public boolean validar_correo() {
        boolean validado = false, correo_validado = false, password_validado = false;

        TextView correo_text = (TextView)findViewById(R.id.correo_text);
        EditText ValidateEmail = (EditText)findViewById(R.id.correo);
        String email = ValidateEmail.getText().toString().trim();
        String patron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        TextView password_text = (TextView)findViewById(R.id.password_text);
        EditText validate_password =  (EditText)findViewById(R.id.password);
        String password = validate_password.getText().toString();


        if(email.matches(patron) && email.length() > 0) { correo_validado = true; correo_text.setVisibility(View.INVISIBLE); } else { correo_text.setVisibility(View.VISIBLE); }

        if(password.length() > 6) { password_validado = true; password_text.setVisibility(View.INVISIBLE); } else { password_text.setVisibility(View.VISIBLE); }

        if((password_validado && correo_validado) == true) { validado = true; }

        return validado;
    }

    private void login_bd() {

    }

}
