package com.selfskyway.firebase_img_upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button browse,upload;
    Uri filepath;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView)findViewById(R.id.imgview);
        browse=(Button)findViewById(R.id.b1);
        upload=(Button)findViewById(R.id.b2);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Please select image"), 1);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimgtofirebase();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK) {


            filepath = data.getData();

            Toast.makeText(getApplicationContext(), "img receve", Toast.LENGTH_SHORT).show();


            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);

            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadimgtofirebase() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference();

        StorageReference imagefolder = reference.child("imagess");

        StorageReference uploader=storage.getReference().child("image1");
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "file upload", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                        startActivity(intent);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                        dialog.setMessage("uploaded :"+(int)percent+"%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();

                    }
                });


    }
}