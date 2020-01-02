package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static java.util.Collections.*;

public class AddShoeActivity extends AppCompatActivity {

    private Shoe myShoe;

    private static ArrayList<Shoe> shoeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shoe);
//        doStuff(this);

        TextView shoeNameText = (TextView) (findViewById(R.id.shoe_name_text_field));
        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));

        shoeDistanceText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    hideKeyboard(v);
                }
            }
        });


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

    public void addShoe(View view) {
        //addToShoeList();
        TextView shoeNameText = (TextView)(findViewById(R.id.shoe_name_text_field));
        CharSequence shoeNameSequence = shoeNameText.getText();
        String shoeName = shoeNameSequence.toString();

        TextView shoeDistanceText = (TextView)(findViewById(R.id.desired_distance_text_field));
        CharSequence shoeDistanceSequence = shoeDistanceText.getText();
        String shoeDistanceString = shoeDistanceSequence.toString();
        Double shoeDistance = 0.0;

        try{
            shoeDistance = Double.valueOf(shoeDistanceString);
            myShoe = new Shoe(shoeName, shoeDistance);
            shoeList.add(myShoe);
            Collections.sort(shoeList);

            saveSharedPreferencesShoeList(this, shoeList);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_LONG);
            toast.show();
        }



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

    public static ArrayList<Shoe> deleteShoe(Shoe shoe) {

        for(Shoe myShoe : shoeList) {
            if(myShoe.toString().equals(shoe.toString())) {
                shoeList.remove(myShoe);
                break;
            }
        }
        return shoeList;
    }

//    public void exitKeyboard(View view) {
//
//        ViewParent viewParent = findViewById(R.id.parent);
//        EditText editTextName = findViewById(R.id.edit_shoe_name_text_field);
//        editTextName.setText("Yup");
//        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if(inputMethodManager.isAcceptingText())
//        {
//            hideKeyboard(view);
//        }
//    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public void doStuff(Activity activity) {
//        TextView txtEdit = findViewById(R.id.edit_shoe_name_text_field);
//
//        txtEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    // code to execute when EditText loses focus
//                    hideKeyboard(v);
//                }
//            }
//        });
//    }
//    public void doThing(View view) {
//        hideKeyboard(this);
//    }
//
//    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        //Find the currently focused view, so we can grab the correct window token from it.
//        View view = activity.getCurrentFocus();
//        //If no view currently has focus, create a new one, just so we can grab a window token from it
//        if (view == null) {
//            view = new View(activity);
//        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
}