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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showShoeList();
        //updateShoeListView();
    }

    public void newShoe(View view) {
        Intent intent = new Intent(this, AddShoeActivity.class);
        startActivity(intent);
    }

    public void showShoeList() {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        ArrayList<Shoe> shoeList = AddShoeActivity.getShoeList();
        ArrayAdapter<Shoe> spinnerArrayListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shoeList);

        spinnerArrayListAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayListAdapter);
    }

//    public void testShoeList(View view) {
//        ListView shoeListView = findViewById(R.id.shoe_list_box);
//
//        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();
//        String myString = "";
//
//        for(int i = 0; i < myList.size(); ++i)
//        {
//            myString += ("Shoe Name: " + myList.get(i).getName() + " " + "Shoe Distance: " +
//                    myList.get(i).getDesiredDistanceInMiles() + "\n");
//        }
//        CharSequence mySequ = myString;
//        //shoeListView.setText(mySequ);
//    }

//    public void updateShoeListView() {
//        ListView shoeListView = findViewById(R.id.shoe_list_box);
//
//        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();
//        String myString = "";
//
//        for(int i = 0; i < myList.size(); ++i)
//        {
//            myString += ("Shoe Name: " + myList.get(i).getName() + " " + "Shoe Distance: " +
//                    myList.get(i).getDesiredDistanceInMiles() + "\n");
//        }
//        CharSequence mySequ = myString;
//
//        ArrayAdapter<Shoe> adapter = new ArrayAdapter<>(this, R.layout.activity_main, myList);
//        shoeListView.setAdapter(adapter);
//    }

}
