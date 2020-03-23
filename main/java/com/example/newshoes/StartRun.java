package com.example.newshoes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartRun extends AppCompatActivity implements LocationListener {

    //this is a constant value that is used to set a limit on when we can update the location
    //if the distance between the new location and the current location is greater than this value
    //we increment the mile counter, and update the location. If it's less than this value, we do
    //nothing and keep listening. This allows us to prevent the mile count from "floating" and
    //incrementing upon a false GPS change when the location updates
    private final double LOCATION_CHANGED_LIMITATION = .009;

    private final Integer METERS_IN_A_MILE = 1609;

    //the original location that we start at. This location is updated each time we increment the
    //mile count
    private Location startLocation;

    //the location that we get when we get new input from the location listener. This variable
    //allows us to find the distance between currentLocation and startLocation to update the mile
    //count
    private Location currentLocation;

    //Number of miles traveled during the current run
    private Double totalMilesTraveled;

    private TextView startLocationText;

    private TextView currentLocationText;

    //TextView variable that updates the number of miles ran, with the totalMilesTraveled value
    private TextView trackedMiles;

    private Shoe shoe;

    //Used to keep track of whether a toast has been made. Honestly, bool would be more efficient in
    //this case since we only intend to display the toast once per run
    private Integer toastTally;

    //Green progress bar that allows the user to visually see how far they've traveled so far in
    //the selected pair of shoes
    private ProgressBar progressBar;

    //used to track when run is paused to allow us to change the attributes and features of the
    //Pause Run button
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);
        //keep the screen in portrait view at all times. This prevents the activity from resetting.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        totalMilesTraveled = 0.0;
        toastTally = 0;

        Intent intent = getIntent();
        //gets the Shoe object placed in the intent
        shoe = (Shoe) intent.getSerializableExtra("Shoe");

        //displays the Shoe object on the top of the acitvity using its toString value
        TextView testingShoe = findViewById(R.id.test_passed_shoe);
        testingShoe.setText(shoe.toString());

        progressBar = findViewById(R.id.run_progress_bar);

        //Sets the max value of the progress bar to that of the desired distance of the current Shoe
        //object. Here we convert from miles to meters
        progressBar.setMax(shoe.getDesiredDistanceInMiles().intValue() * METERS_IN_A_MILE);     //Changed progress bar to meters instead of miles
        //Here we import the progress from previous runs and set that as the value level of the
        //progress bar
        progressBar.setProgress(shoe.getMeterCount().intValue());             //Changed progress bar to meters instead of miles

        //This TextVies displays the desired distance in miles at the far right end of the progress
        //bar
        TextView progressBarDistanceText = findViewById(R.id.progress_bar_end_text);
        progressBarDistanceText.setText(shoe.getDesiredDistanceInMiles().toString());

        //checks if the user has already give the app permission to access location
        checkLocationPermission();
    }

    /**
     * Method to get the location we are beginning our run at
     */
    public void getStartLocation(View view) {
        Button pauseRun = findViewById(R.id.pause_run_button);
        Button stopRun = findViewById(R.id.stop_run_button);
        Button startRun = findViewById(R.id.track_run_switch);
        pauseRun.setVisibility(View.VISIBLE);
        stopRun.setVisibility(View.VISIBLE);
        startRun.setVisibility(View.INVISIBLE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);

        //if we dont't have permission from the user, we're done here
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        //assigns the start location with the current GPS location
        startLocation = locationManager.getLastKnownLocation(provider);

        trackRun(view);
    }

    /**
     * Method that sets the currentLocation, and creates a lcationListener to check if the location
     * has changes
     */
    public void trackRun(View view) {
        totalMilesTraveled = 0.0;
        trackedMiles = findViewById(R.id.mile_count_text);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //if we dont't have permission from the user, we're done here
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            trackedMiles.setText("Lol nope");
            return;
        }

        String provider = locationManager.getBestProvider(criteria, true);

        currentLocation = locationManager.getLastKnownLocation(provider);

        trackedMiles.setText(String.format("%.2f", totalMilesTraveled));

        //creates a location listener to update onLocationChanged method upon meeting the specified
        //time and/or distance criteria
        locationManager.requestLocationUpdates(provider, 1000, 2, this);
    }

    //Method to add miles to the current Shoe object whenever the End Run button is pressed
    public void addMilesToShoe(View view) {
        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();

        //Since Java is pass by value, we have to loop through the ArrayList of Shoes and find
        //the shoe with the same toString value as the Shoe that was passed in to the activity
        for(Shoe shoe : myList)
        {
            //Once we find the shoe, we remove it, update the mile count of the Shoe from this
            //activity, and then insert it into the place of the shoe we just removed. Once this is
            //accomplished, we break out of the loop to avoid errors
            if(this.shoe.toString().equals(shoe.toString()))
            {
                int indexOfShoeInList = myList.indexOf(shoe);
                myList.remove(shoe);
                this.shoe.setMileCount(totalMilesTraveled);

                //Places a shoe emoji next to the shoe name to indicate to the user that this Shoe
                //has reached the goal set by the user
                if(this.shoe.hasReachedGoal()) {
                    if (this.shoe.getRunsSinceGoalReached() == 0) {
                        this.shoe.changeShoeName("👟" + this.shoe.getName());
                        this.shoe.incrementRunsSinceGoalReached();
                    }
                    Toast toast = Toast.makeText(this,
                            "Wow, you're fast! Time for a new pair of shoes!",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                myList.add(indexOfShoeInList, this.shoe);
                break;      //without break, throws invokation target exception
            }
        }
        //save the list of Shoes to shared preferences
        AddShoeActivity.saveSharedPreferencesShoeList(this, myList);
        //go back to the main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                                ActivityCompat.requestPermissions(StartRun.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 1000, 2, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    /**
     * Method called when the locationListener sees that we've changed location
     */
    @Override
    public void onLocationChanged(Location location) {
        final Double VALUE_OF_MILE_IN_METERS = 0.000621371;
        trackedMiles = findViewById(R.id.mile_count_text);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);


        //if we dont't have permission from the user, we're done here
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        //update currentLocation
        currentLocation = locationManager.getLastKnownLocation(provider);

        //Here we place a try/catch block to prevent a NullPointerException that was causing the app
        //to crash
        try{
            //calculate the meters between our currentLocation and our startLocation
            Float metersBetween = currentLocation.distanceTo(startLocation);
            //convert Float value to a Double value
            Double metersBetweenDouble = metersBetween.doubleValue();
            //convert meters to miles
            Double milesBetween = metersBetweenDouble * VALUE_OF_MILE_IN_METERS;

            //if the milesBetween our current and start locations is greater than the value set by the
            //LOCATION_CHANGED_LIMITATION constant, we continue on. Else we return and wait for another
            //call to the method. This prevents us from falsely incrementing our totalMilesTraveled,
            //when the GPS is "floating"
            if(milesBetween < LOCATION_CHANGED_LIMITATION)
            {
                return;
            }

            totalMilesTraveled += milesBetween;
            trackedMiles.setText(String.format("%.2f", totalMilesTraveled));
            shoe.setMeterCount(metersBetweenDouble);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }

        updateProgressBar(shoe.getMeterCount().intValue());        //Changed progress bar to meters instead of miles
        //set the value of startLocation to currentLocation. This allows us to only count recent
        //changes, otherwise, if startLocation stays the same as the beginning, we are adding the
        //incorrect amount of miles, because we are always checking the distance between
        //start and current location
        startLocation = currentLocation;

        //this checks if we've reached our mile goal set by the user for this pair of Shoes. If we
        //have, we display a Toast that alerts the user that it's time for a new pair of shoes.
        //toastTally only allows us to make the Toast once
        //I can implement this better though
        if( (totalMilesTraveled >= shoe.getDesiredDistanceInMiles()) && toastTally < 1)
        {
            Context context = getApplicationContext();
            CharSequence toastText = "Time for a new pair of shoes!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
            ++toastTally;
        }
    }

    /**
     * Method to update the progress bar that is called after every successful call to the
     * onLocationChanged method
     */
    public void updateProgressBar(int progress) {
        progressBar = findViewById(R.id.run_progress_bar);
        progressBar.setProgress(progress);
    }

    /**
     * Method called when the Pause Run button is pressed. This method turns off the
     * locationListener by calling its removeUpdates method, then alerts the user that the run has
     * been paused, via a Toast
     */
    public void pauseRun(View view) {
        if(isPaused)
        {
            continueRun();
        }
        else {
            LocationManager locationManager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.removeUpdates(this);

            Context context = getApplicationContext();
            CharSequence toastText = "Run paused";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();

            setRunPausedStatus(true);
            Button pauseRunButton = findViewById(R.id.pause_run_button);
            pauseRunButton.setText("Continue Run");
        }
    }

    public void continueRun() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);

        //if we dont't have permission from the user, we're done here
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            trackedMiles.setText("Lol nope");
            return;
        }

        //creates a location listener to update onLocationChanged method upon meeting the specified
        //time and/or distance criteria
        locationManager.requestLocationUpdates(provider, 1000, 2, this);

        Context context = getApplicationContext();
        CharSequence toastText = "Continuing run";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();

        setRunPausedStatus(false);
        Button pauseRunButton = findViewById(R.id.pause_run_button);
        pauseRunButton.setText("Pause Run");
    }

    public void setRunPausedStatus(boolean isPaused){
        this.isPaused = isPaused;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}