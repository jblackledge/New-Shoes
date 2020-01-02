package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EditShoe extends AppCompatActivity {

    private Shoe shoe;

    private final Integer METERS_IN_A_MILE = 1609;

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
        TextView editShoeNameField = (TextView) findViewById(R.id.edit_shoe_name_text_field);
        TextView editDesiredDistanceField = (TextView) findViewById(R.id.edit_desired_distance_text_field);
        TextView manuallyEnterMilesField = (TextView) findViewById(R.id.manually_enter_miles_text_field);

        editShoeNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });

        editDesiredDistanceField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });

        manuallyEnterMilesField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });
//        TextView editDesiredDistanceField = findViewById(R.id.edit_desired_distance_text_field);
//        System.out.println(editDesiredDistanceField.getText());
    }

    public void saveChanges(View view) {
        TextView editShoeNameField = findViewById(R.id.edit_shoe_name_text_field);
        CharSequence editShoeNameCharSeq = editShoeNameField.getText();
        String editShoeNameString = editShoeNameCharSeq.toString();

        TextView editDesiredDistanceField = (TextView)findViewById(R.id.edit_desired_distance_text_field);
        CharSequence desiredDistanceFieldCharSeq = editDesiredDistanceField.getText();
        String desiredDistanceFieldString = desiredDistanceFieldCharSeq.toString();
        Double desiredDistanceDouble = 0.0;


        TextView manuallyEnterMilesField = findViewById(R.id.manually_enter_miles_text_field);
        CharSequence manuallyEnterMilesFieldCharSeq = manuallyEnterMilesField.getText();
        String manuallyEnterMilesString = manuallyEnterMilesFieldCharSeq.toString();
        Double manuallyEnterMilesDouble = 0.0;

        if((!editShoeNameField.getText().toString().equals("")) && (!editDesiredDistanceField.getText().toString().equals(""))
                && (!manuallyEnterMilesField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);
        }
        else if((!editShoeNameField.getText().toString().equals("")) &&
                (!editDesiredDistanceField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
        }
        else if(!editShoeNameField.getText().toString().equals("") && (!manuallyEnterMilesField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);
        }
        else if((!editShoeNameField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
        }
        else if((!editDesiredDistanceField.getText().toString().equals("")
                && (!manuallyEnterMilesField.getText().toString().equals(""))))
        {
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);
        }
        else if((!manuallyEnterMilesField.getText().toString().equals("")))
        {
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);
        }

//        if((editShoeNameString != null) && (desiredDistanceDouble != null)
//                && (manuallyEnterMilesDouble != null))
//        {
//            shoe.changeShoeName(editShoeNameString);
//            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
//            shoe.setMileCount(manuallyEnterMilesDouble);
//        }
//        else if((editShoeNameString != null) && (desiredDistanceDouble != null))
//        {
//            shoe.changeShoeName(editShoeNameString);
//            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
//        }
//        else if((editShoeNameString != null) && (manuallyEnterMilesDouble != null))
//        {
//            shoe.changeShoeName(editShoeNameString);
//            shoe.setMileCount(manuallyEnterMilesDouble);
//        }
//        else if((editShoeNameString != null))
//        {
//            shoe.changeShoeName(editShoeNameString);
//        }
//        else if((desiredDistanceDouble != null)
//                && (manuallyEnterMilesField != null))
//        {
//            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
//            shoe.setMileCount(manuallyEnterMilesDouble);
//        }
//        else if((manuallyEnterMilesField != null))
//        {
//            shoe.setMileCount(manuallyEnterMilesDouble);
//        }

        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
