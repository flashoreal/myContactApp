package com.example.onur.mycontactfireb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ListView listView;
    ArrayList<String> personNameArray;
    ArrayList<String> personImageArray;
    ArrayList<String> itemIdArray;
    ContactViewAdapter contactViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);


        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();


        listView = findViewById(R.id.listView);
        listView.setLongClickable(true);

        personImageArray = new ArrayList<>();
        personNameArray = new ArrayList<>();
        itemIdArray = new ArrayList<>();



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               String itemId = itemIdArray.get(position);

                DatabaseReference deleteRef = firebaseDatabase.getReference().child("Persons").child(itemId);
                deleteRef.removeValue();
                Intent intent = getIntent();
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Delete is Succesful !",Toast.LENGTH_LONG).show();



                return false;
            }


        });
        contactViewAdapter = new ContactViewAdapter(personNameArray,personImageArray,this);
        listView.setAdapter(contactViewAdapter);

        getDataFromFirebase();

    }


    public void getDataFromFirebase (){

        DatabaseReference databaseReference = firebaseDatabase.getReference("Persons");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser userLogIn = mAuth.getCurrentUser();
                String usermail = userLogIn.getEmail();

                for (DataSnapshot ds :dataSnapshot.getChildren()){




                    HashMap<String,String> reciveData = (HashMap<String, String>) ds.getValue();

                    if (reciveData.get("usermail").equalsIgnoreCase(usermail)) {
                        personNameArray.add(reciveData.get("personname"));
                        personImageArray.add(reciveData.get("imageurl"));
                        itemIdArray.add(ds.getKey());
                        contactViewAdapter.notifyDataSetChanged();


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_person,menu);

        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_person){
            Intent intent = new Intent(getApplicationContext(),AddPerson.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.log_out){
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Sign Out Succesful !",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);


    }
    @Override
    public void onBackPressed() {


    }



}
