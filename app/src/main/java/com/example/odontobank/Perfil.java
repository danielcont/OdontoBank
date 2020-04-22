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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

 public class Perfil extends AppCompatActivity {

     CircleImageView image_profile;
     TextView username;

     FirebaseAuth mAuth;
     FirebaseUser fuser;

     StorageReference mStorageReference;

     private static final int TAKE_IMAGE_CODE = 10001;

     String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        image_profile = findViewById(R.id.foto);
        username = findViewById(R.id.profile_id);

        fuser = mAuth.getCurrentUser();

        if(fuser != null) {
            if(fuser.getDisplayName() != null) {
                username.setText(fuser.getEmail());
            }
            if(fuser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(fuser.getPhotoUrl())
                        .into(image_profile);
            }
        }

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