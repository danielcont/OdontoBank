package com.example.odontobank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Nuevo extends AppCompatActivity {

    private ImageView back_button;
    private Button registrar_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

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
                boolean validado = validar();
                if(validado == true) {
                    finish();
                }
            }
        });

    }

    private boolean validar() {
        boolean validado = false;
        boolean correo_validado = false, password_validado = false, nombre_validado = false, apellido_validado = false, escuela_validado = false;

        TextView correo_text = (TextView)findViewById(R.id.correo_text);
        EditText ValidateEmail = (EditText)findViewById(R.id.correo);
        String email = ValidateEmail.getText().toString().trim();
        String patron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        TextView password_text = (TextView)findViewById(R.id.password_text);
        EditText validate_password =  (EditText)findViewById(R.id.password);
        String password = validate_password.getText().toString();

        TextView nombre_text = (TextView)findViewById(R.id.nombre_text);
        EditText validate_nombre = (EditText)findViewById(R.id.nombre);
        String nombre = validate_nombre.getText().toString();

        TextView apellido_text = (TextView)findViewById(R.id.apellido_text);
        EditText validate_apellido = (EditText)findViewById(R.id.apellido);
        String apellido = validate_apellido.getText().toString();

        TextView escuela_text = (TextView)findViewById(R.id.escuela_text);
        EditText validate_escuela = (EditText)findViewById(R.id.escuela);
        String escuela = validate_escuela.getText().toString();

        if(email.matches(patron) && email.length() > 0) { correo_validado = true; correo_text.setVisibility(View.INVISIBLE); } else { correo_text.setVisibility(View.VISIBLE); }

        if(password.length() > 6) { password_validado = true; password_text.setVisibility(View.INVISIBLE); } else { password_text.setVisibility(View.VISIBLE); }

        if(nombre.length() != 0) { nombre_validado = true; nombre_text.setVisibility(View.INVISIBLE); } else { nombre_text.setVisibility(View.VISIBLE); }

        if(apellido.length() != 0) { apellido_validado = true; apellido_text.setVisibility(View.INVISIBLE); } else { apellido_text.setVisibility(View.VISIBLE); }

        if(escuela.length() != 0) { escuela_validado = true; escuela_text.setVisibility(View.INVISIBLE); } else { escuela_text.setVisibility(View.VISIBLE); }

        if((correo_validado && password_validado && nombre_validado && apellido_validado && escuela_validado) == true) {
            validado = true;
        }

        return validado;
    }

    private void registro_bd() {

    }

}
