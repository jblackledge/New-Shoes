package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {
    //variable used to track whether a shoe was successfully restored from deletion
    boolean isRestored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //keep the screen in portrait view at all times. This prevents the activity from resetting.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button deleteShoeButton = findViewById(R.id.delete_shoe_button);
        //Here we create a long click listener, upon a long click, we restore a previously deleted
        //shoe and inform the user via Toast, that the she was restored successfully
        deleteShoeButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(!AddShoeActivity.getDeletedShoeStack().empty()) {
                        AddShoeActivity.addBackToList();

                        isRestored = true;
                        //calls method to save shoe list again
                        saveSharedPrefAfterShoeReturnedFromDeletion();

                        Context context = getApplicationContext();
                        CharSequence toastText = "Shoe restored from deletion";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, toastText, duration);
                        toast.show();
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            });

        showShoeList();
    }

    /**
     * Method that saves the shoe list after user long clicks delete to undo deletion
     */
    public void saveSharedPrefAfterShoeReturnedFromDeletion() {
        if(isRestored) {
            AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
        }
        isRestored = false;
    }

    /**
     * When called, starts the AddShoeActivity
     */
    public void newShoe(View view) {
        Intent intent = new Intent(this, AddShoeActivity.class);
        startActivity(intent);
    }

    /**
     * Alphabetically displays the list of shoes saved in shared preferences
     */
    public void showShoeList() {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        //loads the list of Shoe's from shared preferences and assigns it to an ArrayList of Shoes
        ArrayList<Shoe> shoeList = AddShoeActivity.loadSharedPreferencesShoeList(this);

        //displays the list in the spinner
        ArrayAdapter<Shoe> spinnerArrayListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shoeList);

        spinnerArrayListAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayListAdapter);
    }

    /**
     * When called, moves to the StartRunActivity as long as there is a Shoe object in the spinner
     */
    public void startRun(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();

        Intent intent = new Intent(this, StartRun.class);
        //passes the value of the Shoe object chosen from the Spinner list to the next activity
        intent.putExtra("Shoe", chosenShoe);
        if(chosenShoe != null) {
            //only start the activity if the shoe object isn't null, this check prevents us from
            //throwing a NullPointerException
            startActivity(intent);
        }
        //If the Shoe object is null, we inform the user via a toast, to create a Shoe to track
        //before trying to move to the StartRunActivity
        else{
            Toast toast = Toast.makeText(this, "Create a shoe before starting run", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * When called, deletes the Shoe object chosen in the spinner, if there are any Shoe objects
     * in the Spinner
     */
    public void deleteShoe(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();
        //Here we perform a null check to prevent a NullPointerException when attempting to delete
        //a Shoe object. If null, we inform the user that there is no Shoe object in the list to
        //delete
        if(chosenShoe == null)
        {
            //informs the user, via a toast, that there is no shoe object in the list to delete
            Toast toast = Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT);
            toast.show();
        }
        //If there is a shoe, we delete it, update our shared preferences list, and then update the
        //spinner
        AddShoeActivity.deleteShoe(chosenShoe);
        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
        showShoeList();

        Context context = getApplicationContext();
        CharSequence toastText = "Shoe deleted. Mistake? Hold Delete button to undo";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();
    }

    /**
     * When called, if there are Shoe's in the spinner, loads the EditShoeActivity
     */
    public void editShoe(View view) {
        Spinner spinner = findViewById(R.id.choose_shoe_spinner);
        Shoe chosenShoe = (Shoe)spinner.getSelectedItem();
        Intent intent = new Intent(this, EditShoe.class);
        //passes the value of the Shoe object chosen from the Spinner list to the next activity
        intent.putExtra("Shoe", chosenShoe);
        //null check to prevent NullPointerException
        if(chosenShoe != null)
        {
            startActivity(intent);
        }
        //informs user, via a toas, that there is no Shoe object in the list to edit
        else
        {
            Toast toast = Toast.makeText(this, "Nothing to edit", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}