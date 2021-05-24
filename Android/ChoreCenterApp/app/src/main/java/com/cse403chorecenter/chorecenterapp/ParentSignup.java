package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Prompts the user to sign up for a Chore Center parent account with the current signed-in
 * Google account.
 */
public class ParentSignup extends AppCompatActivity {
    private static final String TAG = "ParentSignup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);
    }

    /** When {@code Get Sign up} button is clicked, invoke {@code accountSignup}
     * on current signed-in Google account.
     * Then, start the ChooseAccountType activity. */
    public void onClickSignUp(View view) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // parent account creation
        if (account != null) {
            if (!accountSignup(account)) {
                new AlertDialog.Builder(ParentSignup.this)
                        .setTitle("Sign up failure")
                        .setMessage("Sign up failed due to network connection or server issue.")
                        .setCancelable(false)
                        .setNeutralButton("Back to Choose Account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(ParentSignup.this, ChooseAccountType.class);
                                startActivity(intent);
                            }
                        })
                        .create().show();
            }
        }
    }

    /**
     * Queries the backend server to create a Chore Center parent account using the
     * current {@code account}.
     * @param account the currently signed-in Google account
     * @return true on success and false otherwise
     */
    public boolean accountSignup(GoogleSignInAccount account) {
        try {
            // Instantiate a service handler and populate the argument appropriately
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/new";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("GoogleTokenId", UserLogin.ACCOUNT_ID_TOKEN);
            jsonObj.put("Name", account.getDisplayName());
            jsonObj.put("Email", account.getEmail());
            params[1] = jsonObj.toString();

            // Execute service handler async task
            sh = (ServiceHandler) sh.execute(params);

            // Handle the response
            try {
                String response = sh.get();

                // Initialize showText
                TextView showText = new TextView(this);
                showText.setHint("Parent Code");
                showText.setTextIsSelectable(true);

                // Check response content
                if(response != null && !response.equals("")) {
                    if (!response.equals("404") && !response.equals("500") && !response.equals("400")) {
                        showText.setText(new JSONObject(response).getString("ParentCode"));
                        new AlertDialog.Builder(ParentSignup.this)
                                .setTitle("Parent Code")
                                .setView(showText)
                                .setCancelable(false)
                                .setPositiveButton("Navigation", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(ParentSignup.this, ParentNavigation.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Choose Account", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(ParentSignup.this, ChooseAccountType.class);
                                        startActivity(intent);
                                    }
                                }).create().show();
                        return true;
                    }
                }
                return false;
            } catch (ExecutionException e) {
                Log.e(TAG, "Async execution error: " + e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG, "Async interrupted error: " + e.getMessage());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return false;
    }
}