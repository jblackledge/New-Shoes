package com.example.newshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
}
