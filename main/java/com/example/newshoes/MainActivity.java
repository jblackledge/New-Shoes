package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showShoeList();
    }

    public void newShoe(View view) {
        Intent intent = new Intent(this, AddShoeActivity.class);
        startActivity(intent);
    }

    public void showShoeList() {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
//        ArrayList<Shoe> shoeList = AddShoeActivity.getShoeList();
//
//        if(shoeList == null) {
//            shoeList = AddShoeActivity.loadSharedPreferencesShoeList(this);
//        }

        ArrayList<Shoe> shoeList = AddShoeActivity.loadSharedPreferencesShoeList(this);

        ArrayAdapter<Shoe> spinnerArrayListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shoeList);

        spinnerArrayListAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayListAdapter);
    }

    public void startRun(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();
//        chosenShoe = (Shoe)chosenShoe;

        //Shoe shoe = new Shoe("Nike", 500.0);
        Intent intent = new Intent(this, StartRun.class);
        intent.putExtra("Shoe", chosenShoe);
        startActivity(intent);
    }

}
