package com.example.newshoes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

//    private final double LOCATION_CHANGED_LIMITATION = .00725;
    private final double LOCATION_CHANGED_LIMITATION = .009;

    private final Integer METERS_IN_A_MILE = 1609;

    private Location startLocation;

    private Location currentLocation;

    private Double totalMilesTraveled;

    private TextView startLocationText;

    private TextView currentLocationText;

    private TextView trackedMiles;

    private Shoe shoe;

    private Integer toastTally;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);

        toastTally = 0;

        Intent intent = getIntent();
        shoe = (Shoe) intent.getSerializableExtra("Shoe");

        TextView testingShoe = findViewById(R.id.test_passed_shoe);
        testingShoe.setText(shoe.toString());

        progressBar = findViewById(R.id.run_progress_bar);
//        progressBar.setMax(shoe.getDesiredDistanceInMiles().intValue());
//        progressBar.setProgress(shoe.getMileCount().intValue());
        progressBar.setMax(shoe.getDesiredDistanceInMiles().intValue() * METERS_IN_A_MILE);     //Changed progress bar to meters instead of miles
        progressBar.setProgress(shoe.getMeterCount().intValue());             //Changed progress bar to meters instead of miles
//        progressBar.setMax(100);
//        progressBar.setProgress(50);
        TextView progressBarDistanceText = findViewById(R.id.progress_bar_end_text);
        progressBarDistanceText.setText(shoe.getDesiredDistanceInMiles().toString());

        checkLocationPermission();
    }

    public void getStartLocation(View view) {
        Button pauseRun = findViewById(R.id.pause_run_button);
        Button stopRun = findViewById(R.id.stop_run_button);
        Switch startRun = findViewById(R.id.track_run_switch);
        pauseRun.setVisibility(View.VISIBLE);
        stopRun.setVisibility(View.VISIBLE);
        startRun.setVisibility(View.INVISIBLE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);

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
        startLocation = locationManager.getLastKnownLocation(provider);

//        startLocationText = findViewById(R.id.start_location_test);
//        startLocationText.setText(startLocation.toString());
        trackRun(view);
    }

    public void trackRun(View view) {
        totalMilesTraveled = 0.0;
        trackedMiles = findViewById(R.id.mile_count_text);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

//        currentLocationText = findViewById(R.id.current_location_test);

        currentLocation = locationManager.getLastKnownLocation(provider);
//        currentLocationText.setText(currentLocation.toString());

        trackedMiles.setText(String.format("%.2f", totalMilesTraveled));

        locationManager.requestLocationUpdates(provider, 100, 2, this);
    }

    public void addMilesToShoe(View view) {
        ArrayList<Shoe> myList = AddShoeActivity.getShoeList();

        for(Shoe shoe : myList)
        {
            if(this.shoe.toString().equals(shoe.toString()))
            {
                int indexOfShoeInList = myList.indexOf(shoe);
                myList.remove(shoe);
                this.shoe.setMileCount(totalMilesTraveled);
                myList.add(indexOfShoeInList, this.shoe);
                break;      //without break, throws invokation target exception
            }
        }
        AddShoeActivity.saveSharedPreferencesShoeList(this, myList);
//        shoe.setMileCount(totalMilesTraveled);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

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
                        .setMessage("Can we have permission?")
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
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        final Double VALUE_OF_MILE_IN_METERS = 0.000621371;
        trackedMiles = findViewById(R.id.mile_count_text);
//        currentLocationText = findViewById(R.id.current_location_test);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);


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
        currentLocation = locationManager.getLastKnownLocation(provider);
//        currentLocationText.setText(currentLocation.toString());

        Float metersBetween = currentLocation.distanceTo(startLocation);
        Double metersBetweenDouble = metersBetween.doubleValue();
        Double milesBetween = metersBetweenDouble * VALUE_OF_MILE_IN_METERS;

//        TextView distanceTo = findViewById(R.id.distance_to_test);
//        distanceTo.setText(String.format("Distance to: %.6f", milesBetween));

        if(milesBetween < LOCATION_CHANGED_LIMITATION)
        {
            return;
        }

        totalMilesTraveled += milesBetween;

        trackedMiles.setText(String.format("%.2f", totalMilesTraveled));
//        updateProgressBar(totalMilesTraveled.intValue());
        shoe.setMeterCount(metersBetweenDouble);
        updateProgressBar(shoe.getMeterCount().intValue());        //Changed progress bar to meters instead of miles
        startLocation = currentLocation;

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

    public void updateProgressBar(int progress) {
        progressBar = findViewById(R.id.run_progress_bar);
        progressBar.setProgress(progress);
    }

    public void pauseRun(View view) {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.removeUpdates(this);

        Context context = getApplicationContext();
        CharSequence toastText = "Run paused";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();
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