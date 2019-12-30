package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

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
        Collections.sort(shoeList);

        saveSharedPreferencesShoeList(this, shoeList);

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

    public static void saveSharedPreferencesShoeList(Context context, ArrayList<Shoe> shoeList) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("ShoeList", context.MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shoeList);
        preferencesEditor.putString("shoe", json);
        preferencesEditor.commit();
    }

    public static ArrayList<Shoe> loadSharedPreferencesShoeList(Context context) {
        shoeList = new ArrayList<>();
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("ShoeList", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("shoe", "");

        if(json.isEmpty())
        {
            shoeList = new ArrayList<>();
        }
        else
        {
            Type type = new TypeToken<ArrayList<Shoe>>() {}.getType();
            shoeList = gson.fromJson(json, type);
        }
        return shoeList;
    }
}
