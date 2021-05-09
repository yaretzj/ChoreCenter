package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.cse403chorecenter.chorecenterapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Get Start button */
    public void directToChooseAccountType(View view) {
        Intent intent = new Intent(this, ChooseAccountType.class);
        startActivity(intent);
    }
}