package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EditShoe extends AppCompatActivity {

    private Shoe shoe;

    private final Integer METERS_IN_A_MILE = 1609;    //constant value for meters to mile conversion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shoe);

        //keep the screen in portrait view at all times. This prevents the activity from resetting.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        //gets the object that was placed in the intent from the main activity
        shoe = (Shoe) intent.getSerializableExtra("Shoe");

        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();
        //Since Java is pass by value, compares the toStrings to find the Shoe object in the
        //ArrayList that has the same value as the one passed in
        for(Shoe shoe : myList)
        {
            //once found, assigns the object from the ArrayList to the private class member variable
            //and then breaks the for loop to prevent an error
            if(this.shoe.toString().equals(shoe.toString()))
            {
                this.shoe = shoe;
                break;
            }
        }
        TextView editShoeNameField = (TextView) findViewById(R.id.edit_shoe_name_text_field);
        TextView editDesiredDistanceField = (TextView) findViewById(R.id.edit_desired_distance_text_field);
        TextView manuallyEnterMilesField = (TextView) findViewById(R.id.manually_enter_miles_text_field);

        //listener method to detect when the user clicks outsid of the text field. When they do,
        //exits the keyboard
        editShoeNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });

        //listener method to detect when the user clicks outsid of the text field. When they do,
        //exits the keyboard
        editDesiredDistanceField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });

        //listener method to detect when the user clicks outsid of the text field. When they do,
        //exits the keyboard
        manuallyEnterMilesField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });
    }

    /**
     * Method used to save the changes the user makes to the given Shoe
     */
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

        //Executed when all three text fields are not empty
        if((!editShoeNameField.getText().toString().equals("")) && (!editDesiredDistanceField.getText().toString().equals(""))
                && (!manuallyEnterMilesField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);

            if(shoe.hasReachedGoal())
            {
                if (shoe.getRunsSinceGoalReached() == 0) {
                    shoe.changeShoeName("👟" + shoe.getName());
                    shoe.incrementRunsSinceGoalReached();
                }
            }
        }

        //Executed when ShoeName and DesiredDistance text fields are not empty
        else if((!editShoeNameField.getText().toString().equals("")) &&
                (!editDesiredDistanceField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
        }

        //Executed when ShoeName and ManuallyEnterMiles fields are not empty
        else if(!editShoeNameField.getText().toString().equals("") && (!manuallyEnterMilesField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);

            if(shoe.hasReachedGoal())
            {
                if (shoe.getRunsSinceGoalReached() == 0) {
                    shoe.changeShoeName("👟" + shoe.getName());
                    shoe.incrementRunsSinceGoalReached();
                }
            }
        }

        //Executed when only ShoeName is not empty
        else if((!editShoeNameField.getText().toString().equals("")))
        {
            shoe.changeShoeName(editShoeNameString);
        }

        //Executed when DesiredDistance and ManuallyEnterMiles fields are not empty
        else if((!editDesiredDistanceField.getText().toString().equals("")
                && (!manuallyEnterMilesField.getText().toString().equals(""))))
        {
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);

            if(shoe.hasReachedGoal())
            {
                if (shoe.getRunsSinceGoalReached() == 0) {
                    shoe.changeShoeName("👟" + shoe.getName());
                    shoe.incrementRunsSinceGoalReached();
                }
            }
        }

        //Executed when only ManuallyEnterMiles text field is not empty
        else if((!manuallyEnterMilesField.getText().toString().equals("")))
        {
            manuallyEnterMilesDouble = Double.valueOf(manuallyEnterMilesString);
            shoe.setMileCount(manuallyEnterMilesDouble);
            shoe.setMeterCount(manuallyEnterMilesDouble * METERS_IN_A_MILE);

            if(shoe.hasReachedGoal())
            {
                if (shoe.getRunsSinceGoalReached() == 0) {
                    shoe.changeShoeName("👟" + shoe.getName());
                    shoe.incrementRunsSinceGoalReached();
                }
            }
        }

        //Executed when only DesiredDistance field is not empty
        else if(!editDesiredDistanceField.getText().toString().equals(""))
        {
            desiredDistanceDouble = Double.valueOf(desiredDistanceFieldString);
            shoe.changeDesiredDistanceInMiles(desiredDistanceDouble);
        }

        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Method used to hide the keyboard when the user clicks outside of the text field
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
