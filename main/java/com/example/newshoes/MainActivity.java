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
import android.widget.Toast;

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

        Intent intent = new Intent(this, StartRun.class);
        intent.putExtra("Shoe", chosenShoe);
        if(chosenShoe != null) {
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(this, "Create a shoe before starting run", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void deleteShoe(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();
        if(chosenShoe == null)
        {
            Toast toast = Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT);
            toast.show();
        }
        AddShoeActivity.deleteShoe(chosenShoe);
        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
        showShoeList();
    }

    public void editShoe(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();
        Intent intent = new Intent(this, EditShoe.class);
        intent.putExtra("Shoe", chosenShoe);
        if(chosenShoe != null)
        {
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(this, "Nothing to edit", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}