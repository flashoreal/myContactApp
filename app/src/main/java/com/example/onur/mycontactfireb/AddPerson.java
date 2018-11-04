package com.example.onur.mycontactfireb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddPerson extends AppCompatActivity {

    ImageView selectImageView;
    EditText personNameEditText;
    Button button;
    Uri selectedImageFB;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        button  =findViewById(R.id.addPerson);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);

        selectImageView = findViewById(R.id.selectImageView);
        personNameEditText = findViewById(R.id.personNameEditText);


    }


    public void selectImage (View view){

        //if ile izin olup olmadığını sorguladım, izin yoksa izin aldım varsa galeriye eriştim.
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }


    }

    //izin alındıktan sonra olacakları söyledim.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==1 &&grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //Galeriden resim seçildikten sonra olacakları söyledim. ""
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            selectedImageFB = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageFB);
                selectImageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addPerson (View view){

        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);

        UUID uuid = UUID.randomUUID();
        final String imageName = "images/"+uuid+".jpg";

        final StorageReference storageReference = mStorageRef.child(imageName);
        storageReference.putFile(selectedImageFB).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      String personName =  personNameEditText.getText().toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userMail= user.getEmail();
                        String imageUrl = uri.toString();
                        UUID uuid1 = UUID.randomUUID();
                        String uuidString = uuid1.toString();

                        myRef.child("Persons").child(uuidString).child("usermail").setValue(userMail);
                        myRef.child("Persons").child(uuidString).child("imageurl").setValue(imageUrl);
                        myRef.child("Persons").child(uuidString).child("personname").setValue(personName);

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Person upload is succesful !",Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(),ContactList.class);
                        startActivity(intent);



                    }
                });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}
