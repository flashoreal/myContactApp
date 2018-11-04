package com.example.onur.mycontactfireb;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactViewAdapter extends ArrayAdapter<String> {

    private  final ArrayList<String> personNameArray;
    private  final ArrayList<String> personImageArray;
    private final Activity context;

    public ContactViewAdapter(ArrayList<String> personNameArray, ArrayList<String> personImageArray, Activity context) {

        super(context,R.layout.contact_view,personNameArray);

        this.personNameArray = personNameArray;
        this.personImageArray = personImageArray;
        this.context = context;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.contact_view,null,true );

        TextView personNameEditText = customView.findViewById(R.id.personNameTextContactText);
        ImageView personImageView = customView.findViewById(R.id.personImageViewContactView);

        personNameEditText.setText(personNameArray.get(position));
        Picasso.get().load(personImageArray.get(position)).into(personImageView);

        return customView;
    }
}
