package com.cse403chorecenter.chorecenterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This is the sign out activity for user account that has already signed in.
 */
public class UserSignout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signout);

        // Get the Google Sign In client
        GoogleSignIn.getClient(this, UserLogin.GSO).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });

        // Go back to the choose account type activity
        Intent intent = new Intent(this, ChooseAccountType.class);
        startActivity(intent);
    }
}