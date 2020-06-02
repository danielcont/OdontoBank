package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Informacion extends AppCompatActivity {

    DocumentReference ref;

    private TextView nombre, atencionMed, ciudadInfo, descripcionInfo, celularInfo, editarInfo, fech;
    private ImageView backButton, imgEdit;
    private Switch activateButton;
    private ImageView imgAtencionInf, imgCallInf, imgEmailInfo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String sCelular;
    String sCorreo = "";
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        final Publicacion informacion_atencion = getIntent().getParcelableExtra("publicacion");

        String sNombre = informacion_atencion.getNombre();
        nombre = findViewById(R.id.nombreInf);
        nombre.setText(sNombre);

        backButton = findViewById(R.id.BackButtonInf);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        activateButton = findViewById(R.id.switch1);
        editarInfo = findViewById(R.id.editarInf);
        sCelular = informacion_atencion.getContacto();
        fech = findViewById(R.id.fechaPublic);
        imgCallInf = findViewById(R.id.imgCall);
        imgEmailInfo = findViewById(R.id.imgEmail);
        imgEdit = findViewById(R.id.imageEditInfo);
        // Esconder objetos dependiendo quién lo ve
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(informacion_atencion.getUid())) {
            imgCallInf.setVisibility(View.INVISIBLE);
            imgEmailInfo.setVisibility(View.INVISIBLE);
            final String sActivado = informacion_atencion.getActivado();
            boolean bActivado;
            if(sActivado.equals("1")) {
                bActivado = true;
                activateButton.setText("Deslice para desactivar");
            } else {
                bActivado = false;
                activateButton.setText("Deslice para activar");
            }
            activateButton.setChecked(bActivado);

            activateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ref = db.collection("publicaciones").document(informacion_atencion.getpUid());

                    String sAct = informacion_atencion.getActivado();
                    if(sAct.equals("1")) {
                        sAct = "0";
                    } else {
                        sAct = "1";
                    }

                    final String sA = sAct;
                    ref.update("activado", sA).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            informacion_atencion.setActivado(sA);
                            finish();
                            startActivity(getIntent());
                        }
                    });

                }
            });

            editarInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent editarMisPublicaciones = new Intent(Informacion.this, EditarPublicacion.class);
                    editarMisPublicaciones.putExtra("publicacion_", informacion_atencion);
                    startActivity(editarMisPublicaciones);
                }
            });

        } else {
            activateButton.setVisibility(View.INVISIBLE);
            editarInfo.setVisibility(View.INVISIBLE);
            imgEdit.setVisibility(View.INVISIBLE);

            if(informacion_atencion.getContacto().equals("")) {
                imgCallInf.setVisibility(View.INVISIBLE);
            }

            // AGREGAR FUNCIONALIDAD A CADA BOTON
            imgCallInf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makePhoneCall(sCelular);
                }
            });

            db.document("estudiantes/" + informacion_atencion.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                sCorreo = sCorreo + documentSnapshot.getString("correo");
                            }
                        }
                    });
            imgEmailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Correo", sCorreo);
                    clipboardManager.setPrimaryClip(clip);

                    Toast.makeText(Informacion.this, "Correo copiado al portapapeles", Toast.LENGTH_SHORT).show();
                }
            });

        }

        imgAtencionInf = findViewById(R.id.imgAtencionInf);
        atencionMed = findViewById(R.id.atencionMedicaInf);
        ciudadInfo = findViewById(R.id.ciudadInf);
        descripcionInfo = findViewById(R.id.descripcionInf);
        celularInfo = findViewById(R.id.celularInf);

        datosInfo(informacion_atencion);

    }

    @Override
    public void onResume() {
        super.onResume();
        Publicacion informacion_atencion = getIntent().getParcelableExtra("publicacion");
        datosInfo(informacion_atencion);
    }

    private void datosInfo(Publicacion informacion_atencion) {

        if(informacion_atencion.getAtencion_medica().equals("Endodoncia")) {
            imgAtencionInf.setImageResource(R.drawable.endodoncia);
        } else if(informacion_atencion.getAtencion_medica().equals("Cirugía Oral")) {
            imgAtencionInf.setImageResource(R.drawable.cirugia_oral);
        } else if(informacion_atencion.getAtencion_medica().equals("Odontología Preventiva")) {
            imgAtencionInf.setImageResource(R.drawable.odontologia_preventiva);
        } else if(informacion_atencion.getAtencion_medica().equals("Periodoncia")) {
            imgAtencionInf.setImageResource(R.drawable.periodoncia);
        } else if(informacion_atencion.getAtencion_medica().equals("Terapéutica Dental")) {
            imgAtencionInf.setImageResource(R.drawable.terapeutica_dental);
        } else if(informacion_atencion.getAtencion_medica().equals("Prótesis Dental")) {
            imgAtencionInf.setImageResource(R.drawable.protesis_dental);
        } else if(informacion_atencion.getAtencion_medica().equals("Odontología Infantil")) {
            imgAtencionInf.setImageResource(R.drawable.odontologia_infantil);
        } else if(informacion_atencion.getAtencion_medica().equals("Dolor Orofacial")) {
            imgAtencionInf.setImageResource(R.drawable.dolor_orofacial);
        } else if(informacion_atencion.getAtencion_medica().equals("Ortodoncia")) {
            imgAtencionInf.setImageResource(R.drawable.ortodoncia);
        } else if(informacion_atencion.getAtencion_medica().equals("Cariología")) {
            imgAtencionInf.setImageResource(R.drawable.cariologia);
        } else if(informacion_atencion.getAtencion_medica().equals("Implatología")) {
            imgAtencionInf.setImageResource(R.drawable.implantologia);
        }

        atencionMed.setText(informacion_atencion.getAtencion_medica());
        ciudadInfo.setText(informacion_atencion.getCiudad());
        fech.setText(informacion_atencion.getFecha());

        if(informacion_atencion.getContacto().equals("") && informacion_atencion.getDescripcion().equals("")) {
            celularInfo.setText("No disponible");
            descripcionInfo.setText("No disponible");
        } else {
            celularInfo.setText(informacion_atencion.getContacto());
            descripcionInfo.setText(informacion_atencion.getDescripcion());
        }

    }

    private void makePhoneCall(final String sCelular) {

        if(ContextCompat.checkSelfPermission(Informacion.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Informacion.this,
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + sCelular;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(sCelular);
            }

        }

    }
}
