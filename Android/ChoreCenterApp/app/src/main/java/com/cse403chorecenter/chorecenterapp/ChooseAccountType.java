package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAccountType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_type);
    }

    /** Called when the user taps the Parent button */
    public void directToParentLogin(View view) {
        Intent intent = new Intent(this, UserLogin.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, "parents");
        startActivity(intent);
    }

    /** Called when the user taps the Kid button */
    public void directToKidLogin(View view) {
        Intent intent = new Intent(this, UserLogin.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, "children");
//        Intent intent = new Intent(this, KidMain.class);
//        String chosenType = "kid";
//        intent.putExtra(MainActivity.EXTRA_MESSAGE, chosenType);
        startActivity(intent);
    }
}