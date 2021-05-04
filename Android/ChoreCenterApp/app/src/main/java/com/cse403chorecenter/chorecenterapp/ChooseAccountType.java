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
        // Do something in response to button
        Intent intent = new Intent(this, UserLogin.class);
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        String chosenType = "parents";
        intent.putExtra(MainActivity.EXTRA_MESSAGE, chosenType);
        startActivity(intent);
    }

    /** Called when the user taps the Kid button */
    public void directToKidLogin(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, UserLogin.class);
        String chosenType = "children";
        intent.putExtra(MainActivity.EXTRA_MESSAGE, chosenType);
        startActivity(intent);
    }
}