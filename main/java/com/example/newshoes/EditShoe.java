package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        CharSequence editShoeNameCharSeq = editShoeNameField.getText();
        String editShoeNameString = editShoeNameCharSeq.toString();

        TextView editDesiredDistanceField = (TextView)findViewById(R.id.edit_desired_distance_text_field);
        CharSequence desiredDistanceFieldCharSeq = editDesiredDistanceField.getText();
        String desiredDistanceFieldString = desiredDistanceFieldCharSeq.toString();
        Double desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);

        EditText manuallyEnterMilesField = findViewById(R.id.manually_enter_miles_text_field);
        CharSequence manuallyEnterMilesFieldCharSeq = manuallyEnterMilesField.getText();
        String manuallyEnterMilesString = manuallyEnterMilesFieldCharSeq.toString();
        Double manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);

        if((editShoeNameField.getText() != null) && (editDesiredDistanceField.getText() != null)
                && (manuallyEnterMilesField != null))
        {
            shoe.changeShoeName(editShoeNameString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            shoe.setMileCount(manuallyEnterMilesDouble);
        }
        else if((editShoeNameField.getText() != null) && (editDesiredDistanceField.getText() != null))
        {
            shoe.changeShoeName(editShoeNameString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
        }
        else if((editShoeNameField.getText() != null) && (manuallyEnterMilesField != null))
        {
            shoe.changeShoeName(editShoeNameString);
            shoe.setMileCount(manuallyEnterMilesDouble);
        }
        else if((editShoeNameField.getText() != null))
        {
            shoe.changeShoeName(editShoeNameString);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
