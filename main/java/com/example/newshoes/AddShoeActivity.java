package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AddShoeActivity extends AppCompatActivity {

    private Shoe myShoe;

    private static ArrayList<Shoe> shoeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shoe);
    }

    public void addShoe(View view) {
        //addToShoeList();
        TextView shoeNameText = (TextView)(findViewById(R.id.shoe_name_text_field));
        CharSequence shoeNameSequence = shoeNameText.getText();
        String shoeName = shoeNameSequence.toString();

        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));
        CharSequence shoeDistanceSequence = shoeDistanceText.getText();
        String shoeDistanceString = shoeDistanceSequence.toString();
        Double shoeDistance = Double.valueOf(shoeDistanceString);

        myShoe = new Shoe(shoeName, shoeDistance);
        shoeList.add(myShoe);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    public void addToShoeList() {
//        TextView shoeNameText = (TextView)(findViewById(R.id.shoe_name_text_field));
//        String shoeName = (String)shoeNameText.getText();
//
//        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));
//        String shoeDistanceString = (String)shoeDistanceText.getText();
//        Integer shoeDistance = Integer.valueOf(shoeDistanceString);
//
//        myShoe = new Shoe(shoeName, shoeDistance);
//        shoeList.add(myShoe);
//    }

    public static ArrayList<Shoe> getShoeList() {
        return shoeList;
    }

//    public String getShoeName() {
//        return myShoe.getName();
//    }
//
//    public Integer getDistance() {
//        return myShoe.getDesiredDistanceInMiles();
//    }
}
