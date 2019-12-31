package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class EditShoe extends AppCompatActivity {

    private Shoe shoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shoe);

        Intent intent = getIntent();
        shoe = (Shoe) intent.getSerializableExtra("Shoe");

        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();
        for(Shoe shoe : myList)
        {
            if(this.shoe.toString().equals(shoe.toString()))
            {
                this.shoe = shoe;
                break;
            }
        }
    }

    public void saveChanges(View view) {
        EditText editShoeNameField = findViewById(R.id.edit_shoe_name_text_field);
        EditText editDesiredDistanceField = findViewById(R.id.edit_desired_distance_text_field);
        Double desiredDistanceDouble = Double.valueOf(editDesiredDistanceField.toString());

        EditText manuallyEnterMilesField = findViewById(R.id.manually_enter_miles_text_field);
        Double manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesField.toString());

        if((editShoeNameField.getText() != null) && (editDesiredDistanceField.getText() != null)
                && (manuallyEnterMilesField != null))
        {
            shoe.changeShoeName(editShoeNameField.toString());
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            shoe.setMileCount(manuallyEnterMilesDouble);
        }
        else if((editShoeNameField.getText() != null) && (editDesiredDistanceField.getText() != null))
        {
            shoe.changeShoeName(editShoeNameField.toString());
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
        }
        else if((editShoeNameField.getText() != null) && (manuallyEnterMilesField != null))
        {
            shoe.changeShoeName(editShoeNameField.toString());
            shoe.setMileCount(manuallyEnterMilesDouble);
        }
        else if((editShoeNameField.getText() != null))
        {
            shoe.changeShoeName(editShoeNameField.toString());
        }
        else if((editDesiredDistanceField.getText() != null)
                && (manuallyEnterMilesField != null))
        {
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            shoe.setMileCount(manuallyEnterMilesDouble);
        }
        else if((manuallyEnterMilesField != null))
        {
            shoe.setMileCount(manuallyEnterMilesDouble);
        }

        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
    }
}
