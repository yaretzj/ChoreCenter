package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This is the activity for users to choose their account type.
 */
public class ChooseAccountType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_type);
    }

    /** When {@code Parent} button is clicked, start UserLogin activity with EXTRA_MESSAGE = "parents" */
    public void directToParentLogin(View view) {
        Intent intent = new Intent(this, UserLogin.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, "parents");
        startActivity(intent);
    }

    /** When {@code Kid} button is clicked, start UserLogin activity with EXTRA_MESSAGE = "children" */
    public void directToKidLogin(View view) {
        Intent intent = new Intent(this, UserLogin.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, "children");
        startActivity(intent);
    }
}