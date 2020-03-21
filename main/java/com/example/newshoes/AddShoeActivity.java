package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static java.util.Collections.*;

public class AddShoeActivity extends AppCompatActivity {

    private Shoe myShoe;

    private static Stack<Shoe> deletedShoeStack = new Stack<>();

    private static ArrayList<Shoe> shoeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shoe);

        //keep the screen in portrait view at all times. This prevents the activity from resetting.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView shoeNameText = (TextView) (findViewById(R.id.shoe_name_text_field));
        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));

        //checks if the user clicks any area outside of the text field, it they do, exits the
        //keyboard
        shoeDistanceText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });

        //checks if the user clicks any area outside of the text field, it they do, exits the
        //keyboard
        shoeNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
     * Method to add a new Shoe object to the ArrayList of Shoe's
     */
    public void addShoe(View view) {
        TextView shoeNameText = (TextView)(findViewById(R.id.shoe_name_text_field));
        CharSequence shoeNameSequence = shoeNameText.getText();
        String shoeName = shoeNameSequence.toString();

        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));
        CharSequence shoeDistanceSequence = shoeDistanceText.getText();
        String shoeDistanceString = shoeDistanceSequence.toString();
        Double shoeDistance = 0.0;

        //if the user enters a valid number we add the shoe
        try{
            shoeDistance = Double.valueOf(shoeDistanceString);
            myShoe = new Shoe(shoeName, shoeDistance);
            shoeList.add(myShoe);
            Collections.sort(shoeList);

            saveSharedPreferencesShoeList(this, shoeList);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        //if the user doesn't enter number, we catch the exception thrown, and inform the user via
        // a toast, to enter a valid number
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static ArrayList<Shoe> getShoeList() {
        return shoeList;
    }

    public static Stack<Shoe> getDeletedShoeStack() {
        return deletedShoeStack;
    }

    /**
     * Method to save the ArrayList of Shoe objects to shared preferences. This allows the user to
     * exit the app or even turn off their device and still have all of their shoes saved
     */
    public static void saveSharedPreferencesShoeList(Context context, ArrayList<Shoe> shoeList) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("ShoeList", context.MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        //Here we make our ArrayList object into a Gson object, then turns it into a Json object
        Gson gson = new Gson();
        String json = gson.toJson(shoeList);
        //Saves the Json object in shared preferences
        preferencesEditor.putString("shoe", json);
        preferencesEditor.commit();
    }

    /**
     * Method to load the saved ArrayList object of Shoes from shared preferences
     */
    public static ArrayList<Shoe> loadSharedPreferencesShoeList(Context context) {
        shoeList = new ArrayList<>();
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("ShoeList", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("shoe", "");

        //if there is no json file saved in shared preferences, we just create an empty ArrayList
        if(json.isEmpty())
        {
            shoeList = new ArrayList<>();
        }
        //otherwise, we assign the json file saved in shared prefences, to a gson object, then assign
        //the gson object to the ArrayList
        else
        {
            Type type = new TypeToken<ArrayList<Shoe>>() {}.getType();
            shoeList = gson.fromJson(json, type);
        }
        return shoeList;
    }

    /**
     * Method to delete a Shoe object from the ArrayList
     */
    public static ArrayList<Shoe> deleteShoe(Shoe shoe) {

        //Since Java is a pass by value language, we have to compare whether the toString of the
        //passed in Shoe object is equal to the toString of one of the Shoe objects in the
        // ArrayList. If so, we remove the Shoe object from the ArrayList
        for(Shoe myShoe : shoeList) {
            if(myShoe.toString().equals(shoe.toString())) {
                //push deleted shoe to stack to allow user to undo deletion in case it was deleted
                //by mistake
                deletedShoeStack.push(myShoe);
                shoeList.remove(myShoe);
                break;  //once the correct Shoe object is found, we break out of the loop
            }
        }
        return shoeList;
    }

    /**
     * Method used to exit the keyboard when the user clicks outside of a text field
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ArrayList<Shoe> addBackToList() {
        shoeList.add(deletedShoeStack.pop());
        Collections.sort(shoeList);
        return shoeList;
    }
}