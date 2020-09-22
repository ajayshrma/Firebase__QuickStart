package com.ajayshrma.upload_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
EditText name,address,phoneNo;
Button Browse,upload;
ImageView image;
    Uri filepath;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Browse=(Button)findViewById(R.id.browse);
        upload=(Button)findViewById(R.id.submit);


        Browse.setOnClickListener(new View.OnClickListener() {
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
      uploadtofirebase();
    }
});
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK)
        {
            filepath = data.getData();
            Toast.makeText(getApplicationContext(), "img receve", Toast.LENGTH_SHORT).show();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            }

             catch (Exception ex) {
             }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void uploadtofirebase()
    {


        name=(EditText)findViewById(R.id.usr_name);
        address=(EditText)findViewById(R.id.usr_address);
        phoneNo=(EditText)findViewById(R.id.Usr_no);
        image=(ImageView)findViewById(R.id.img);

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader = storage.getReference("image1"+new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                dialog.dismiss();
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference();

                                dataholder obj = new dataholder(name.getText().toString(),address.getText().toString(),uri.toString());

                                root.child(phoneNo.getText().toString()).setValue(obj);
                                name.setText("");
                                address.setText("");
                                phoneNo.setText("");
                                image.setImageResource(R.drawable.ic_launcher_background);
                                Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                                startActivity(intent);


                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                dialog.setMessage("uploaded :"+(int)percent+"%");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

}