package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class StartRun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);

        Intent intent = getIntent();
        Shoe shoe = (Shoe)intent.getSerializableExtra("Shoe");

        TextView testingShoe = findViewById(R.id.test_passed_shoe);
        testingShoe.setText(shoe.toString());

        TextView trackedMiles = findViewById(R.id.mile_count_text);
        trackedMiles.setText("0.00");
    }

    public void trackRun(View view) {
        final Double VALUE_OF_MILE_IN_METERS = 0.000621371;
        Switch trackRunSwitch = findViewById(R.id.track_run_switch);
        Double totalMilesTraveled = 0.0;
        TextView trackedMiles = findViewById(R.id.mile_count_text);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String providerName = locationManager.getBestProvider(criteria, true);

        Location startLocation = new Location(providerName);
        Double startLatitude = startLocation.getLatitude();
        startLocation.setLatitude(startLatitude);

        Double startLongitude = startLocation.getLongitude();
        startLocation.setLongitude(startLongitude);
        startLocation.set(startLocation);

        while(trackRunSwitch.isChecked())
        {
            Location currentLocation = new Location(providerName);
            Double currentLatitude = currentLocation.getLatitude();
            currentLocation.setLatitude(currentLatitude);

            Double currentLongitude = currentLocation.getLongitude();
            currentLocation.setLongitude(currentLongitude);
            currentLocation.set(currentLocation);

            Float metersBetween = currentLocation.distanceTo(startLocation);
            Double metersBetweenDouble = metersBetween.doubleValue();
            Double milesBetween = metersBetweenDouble * VALUE_OF_MILE_IN_METERS;
            totalMilesTraveled += milesBetween;

            trackedMiles.setText(totalMilesTraveled.toString());
        }


    }
}
