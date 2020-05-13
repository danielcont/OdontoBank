package com.example.odontobank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

 public class Perfil extends AppCompatActivity {

     CircleImageView image_profile;
     TextView username, correo, nombre, apellido, carrera, editar;
     private ImageView back_button;

     FirebaseAuth mAuth;
     FirebaseUser fuser;

     StorageReference mStorageReference;
     FirebaseFirestore  db = FirebaseFirestore.getInstance();
     DocumentReference ref;

     private static final int TAKE_IMAGE_CODE = 10001;

     String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        image_profile = findViewById(R.id.foto);
        username = (TextView) findViewById(R.id.profile_id);
        correo = (TextView) findViewById(R.id.pCorreo);
        nombre = (TextView) findViewById(R.id.pNombre);
        apellido = (TextView) findViewById(R.id.pApellido);
        carrera = (TextView) findViewById(R.id.pCarrera);
        editar = (TextView) findViewById(R.id.pEditar);

        fuser = mAuth.getCurrentUser();

        // Botón de regresar
        back_button = findViewById(R.id.pBackButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        });

        // Botón de editar
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Perfil.this, EditarActivity.class);
                startActivity(intent);
            }
        });

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
                            username.setText(sCorreo);

                            correo.append(sCorreo);
                            String sNombre = documentSnapshot.getString("nombre");
                            nombre.append(sNombre);
                            String sApellido = documentSnapshot.getString("apellido");
                            apellido.append(sApellido);
                            String sCarrera = documentSnapshot.getString("escuela");
                            carrera.append(sCarrera);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

     public void handleImageClick(View view) {
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         if(intent.resolveActivity(getPackageManager()) != null) {
             startActivityForResult(intent, TAKE_IMAGE_CODE);
         }
     }

     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if(requestCode == TAKE_IMAGE_CODE) {
             switch (resultCode) {
                 case RESULT_OK:
                     Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                     image_profile.setImageBitmap(bitmap);
                     handleUpload(bitmap);
                     break;

             }
         }
     }

     private void handleUpload(Bitmap bitmap) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

         String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
         final StorageReference reference = mStorageReference.child("imagenes/" + uid +  ".jpeg");

         reference.putBytes(baos.toByteArray())
                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         getDownloadUrl(reference);
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.e(TAG, "onFailure: ", e.getCause());
                     }
                 });
     }

     private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
     }

     private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                 .setPhotoUri(uri)
                 .build();

        user.updateProfile(request)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Toast.makeText(Perfil.this, "Actualización exitosa.", Toast.LENGTH_SHORT).show();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(Perfil.this, "Error...", Toast.LENGTH_SHORT).show();
                     }
                 });
     }

 }