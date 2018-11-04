package com.example.onur.mycontactfireb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText userNameText;
    EditText userPasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameText = findViewById(R.id.userEditText);
        userPasswordText = findViewById(R.id.userPassText);

        mAuth = FirebaseAuth.getInstance();

       if (mAuth.getCurrentUser()!= null){
           Intent intent = new Intent(getApplicationContext(),ContactList.class);
           startActivity(intent);
       }




    }





    public void signIn (View view){

        mAuth.signInWithEmailAndPassword(userNameText.getText().toString(),userPasswordText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(getApplicationContext(),"Welcome Your Contact !",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),ContactList.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    }
                });

    }


    public void signUp (View view){

        mAuth.createUserWithEmailAndPassword(userNameText.getText().toString(),userPasswordText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Welcome to MyContact",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),ContactList.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}
