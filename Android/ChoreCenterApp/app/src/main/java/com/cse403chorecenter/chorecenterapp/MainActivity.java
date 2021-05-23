package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This is the entry-point of the application.
 */
public class MainActivity extends AppCompatActivity {
    // Extra keys used throughout the app
    public static final String EXTRA_MESSAGE = "com.cse403chorecenter.chorecenterapp.MESSAGE";

    // Domain of server to send network requests
    public static final String DNS = "http://chorecenter.westus2.cloudapp.azure.com/";
//    public static final String DNS = "http://10.0.2.2:80/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** When {@code Get Started} button is clicked, start ChooseAccountType activity. */
    public void directToChooseAccountType(View view) {
        Intent intent = new Intent(this, ChooseAccountType.class);
        startActivity(intent);
    }

    // TODO: Provide slideshow to quickly introduce the functionalities of the app to users.
}
