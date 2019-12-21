package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
    }

    public void trackRun() {
        Switch trackRunSwitch = findViewById(R.id.track_run_switch);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String providerName = locationManager.getBestProvider(criteria, true);

        Location startLocation = new Location(providerName);
        Double latitude = startLocation.getLatitude();
        startLocation.setLatitude(latitude);

        Double longitude = startLocation.getLongitude();
        startLocation.setLongitude(longitude);
        startLocation.set(startLocation);


    }
}
