package com.example.newshoes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //keep the screen in portrait view at all times. This prevents the activity from resetting.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //if we dont't have permission from the user, we're done here
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        showShoeList();
    }

    /**
     * Method that saves the shoe list after user long clicks delete to undo deletion
     */
    public void saveSharedPrefAfterShoeReturnedFromDeletion() {
        AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
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
        else {
            AddShoeActivity.deleteShoe(chosenShoe);
            AddShoeActivity.saveSharedPreferencesShoeList(this, AddShoeActivity.getShoeList());
            showShoeList();

            Context context = MainActivity.this;
            CharSequence toastText = "Shoe deleted. Mistake? Hold Delete button to undo";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        }

        //Code for undoing a undoing an accidentally deleted Shoe
        Button deleteShoeButton = findViewById(R.id.delete_shoe_button);
        //Here we create a long click listener, upon a long click, we restore a previously deleted
        //shoe and inform the user via Toast, that the she was restored successfully
        deleteShoeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!AddShoeActivity.getDeletedShoeStack().empty()) {
                    AddShoeActivity.addBackToList();

                    //calls method to save shoe list again
                    saveSharedPrefAfterShoeReturnedFromDeletion();
                    //updates list of Shoes
                    showShoeList();

                    Context context = MainActivity.this;
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

    /**
     * Method to check if we have permission from the user to access their location. If not we
     * prompt them with a dialogue box and request permission from them
     */
    public boolean checkLocationPermission() {
        final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("New Shoes needs permission to access your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}