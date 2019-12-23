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
import android.widget.Switch;
import android.widget.TextView;

public class StartRun extends AppCompatActivity implements LocationListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);

        Intent intent = getIntent();
        Shoe shoe = (Shoe) intent.getSerializableExtra("Shoe");

        TextView testingShoe = findViewById(R.id.test_passed_shoe);
        testingShoe.setText(shoe.toString());

        TextView trackedMiles = findViewById(R.id.mile_count_text);
        trackedMiles.setText("0.00");
        checkLocationPermission();
    }

    public void trackRun(View view) {
        final Double VALUE_OF_MILE_IN_METERS = 0.000621371;
        //Switch trackRunSwitch = findViewById(R.id.track_run_switch);
        Button trackRunSwitch = findViewById(R.id.track_run_switch);    //TEST ONLY DELEETE LATER
        Double totalMilesTraveled = 0.0;
        TextView trackedMiles = findViewById(R.id.mile_count_text);

//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);

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
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                2000, 1, this);

          String provider = locationManager.getBestProvider(new Criteria(), true);

//        Location startLocation = new Location(providerName);
//        Double startLatitude = startLocation.getLatitude();
//        startLocation.setLatitude(startLatitude);
//
//        Double startLongitude = startLocation.getLongitude();
//        startLocation.setLongitude(startLongitude);
//        startLocation.set(startLocation);

        Location startLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //TEST REMOVE LATER
        TextView startLocationText = findViewById(R.id.start_location_test);
        Float accFloat = startLocation.getAccuracy();
        Double testLongitude = startLocation.getLongitude();
        Double testLatitude = startLocation.getLatitude();
        startLocationText.setText("Longitude: " + testLongitude.toString() + "\n"
                    + " Latitdude: " + testLatitude.toString());
        //END OF TEST CODE REMOVE LATER

//        while(trackRunSwitch.isPressed())
//        {
//            Location currentLocation = new Location(providerName);
//            Double currentLatitude = currentLocation.getLatitude();
//            currentLocation.setLatitude(currentLatitude);
//
//            Double currentLongitude = currentLocation.getLongitude();
//            currentLocation.setLongitude(currentLongitude);
//            currentLocation.set(currentLocation);
//
//            Float metersBetween = currentLocation.distanceTo(startLocation);
//            Double metersBetweenDouble = metersBetween.doubleValue();
//            Double milesBetween = metersBetweenDouble * VALUE_OF_MILE_IN_METERS;
//            totalMilesTraveled += milesBetween;
//
//            trackedMiles.setText(totalMilesTraveled.toString());
//            trackRunSwitch.isChecked();
//        }
        //Button stopRunButton = findViewById(R.id.test_stop_run_button);

//        while(!stopRunButton.isPressed())
//        {
//            Location currentLocation = new Location(providerName);
//            Double currentLatitude = currentLocation.getLatitude();
//            currentLocation.setLatitude(currentLatitude);
//
//            Double currentLongitude = currentLocation.getLongitude();
//            currentLocation.setLongitude(currentLongitude);
//            currentLocation.set(currentLocation);
//
//            Float metersBetween = currentLocation.distanceTo(startLocation);
//            Double metersBetweenDouble = metersBetween.doubleValue();
//            Double milesBetween = metersBetweenDouble * VALUE_OF_MILE_IN_METERS;
//            totalMilesTraveled += milesBetween;
//
//            trackedMiles.setText(totalMilesTraveled.toString());
//            stopRunButton.isPressed();
//        }
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
