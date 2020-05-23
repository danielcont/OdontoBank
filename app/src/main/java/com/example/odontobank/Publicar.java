package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Publicar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView backButton;
    private TextView nombre, correo;
    private EditText descripcion, phone;
    private Button publish_button;
    private CircleImageView image_profile;
    private Spinner spinner;

    FirebaseAuth mAuth;
    FirebaseUser fuser;

    StorageReference mStorageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;

    private static String TAG = "";

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
        phone = (EditText) findViewById(R.id.celular);

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
                if((descripcion.getText().toString().trim().length() == 0) || spinner.getSelectedItem().toString().equals("Seleccionar Opción") )  {

                    if(descripcion.getText().toString().trim().length() > 0) {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(Publicar.this).create();
                        alertDialog2.setMessage("Favor agregar una breve descripción");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                    }

                    if(spinner.getSelectedItem().toString().equals("Seleccionar Opción")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Publicar.this).create();
                        alertDialog.setMessage("Favor de escoger una Atención Médica");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                } else {
                    publicar();
                }
            }
        });

        spinner = findViewById(R.id.spinner);
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
        MyLocation g = new MyLocation(getApplicationContext());
        Location l = g.getLocation();
        double lat, lon;
        String latitud = "", longitud = "";
        if(l != null) {
            lat = l.getLatitude();
            lon = l.getLongitude();

            latitud = Double.toString(lat);
            longitud = Double.toString(lon);
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String contacto = phone.getText().toString();
        String atencion_medica = spinner.getSelectedItem().toString();

        Map<Object, String> datos = new HashMap<>();
        datos.put("uid", uid);
        datos.put("nombre", nombre.getText().toString());
        datos.put("descripcion", descripcion.getText().toString());
        datos.put("contacto", contacto);
        datos.put("atencion_medica", atencion_medica);
        datos.put("longitud", longitud);
        datos.put("latitud", latitud);
        datos.put("activado", "1");

        db.collection("publicaciones").document()
                .set(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(Publicar.this).create();
                        alertDialog2.setMessage("La solicitud médica se ha publicado exitosamente");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog2.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

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
