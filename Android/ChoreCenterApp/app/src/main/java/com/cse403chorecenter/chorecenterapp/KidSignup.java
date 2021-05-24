package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Prompts the user to sign up for a Chore Center kid account with the current signed-in
 * Google account.
 */
public class KidSignup extends AppCompatActivity {
    private static final String TAG = "KidSignup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_signup);
    }

    /** When {@code Get Sign up} button is clicked, invoke {@code accountSignup}
     * on current signed-in Google account with parent code from input text box.
     * Then, start the ChooseAccountType activity. */
    public void onClickSignUp(View view) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // kid account creation
        if (account != null) {
            if (!accountSignup(account, ((EditText) findViewById(R.id.kidSignupEditText1)).getText().toString())) {
                new AlertDialog.Builder(KidSignup.this)
                        .setTitle("Sign up failure")
                        .setMessage("Sign up failed due to wrong parent code, network connection or server issue.")
                        .setCancelable(false)
                        .setNeutralButton("Back to Choose Account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(KidSignup.this, ChooseAccountType.class);
                                startActivity(intent);
                            }
                        })
                        .create().show();
            }
        }
    }

    /**
     * Queries the backend server to create a Chore Center kid account using the
     * current {@code account} and given {@code parentCode}.
     * @param account the currently signed-in Google account
     * @param parentCode the parent code inputted by the user
     * @return true on success and false otherwise
     */
    public boolean accountSignup(GoogleSignInAccount account, String parentCode) {
        try {
            // Instantiate a service handler and populate the argument appropriately
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/new";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("GoogleTokenId", UserLogin.ACCOUNT_ID_TOKEN);
            jsonObj.put("ParentCode", parentCode);
            jsonObj.put("Name", account.getDisplayName());
            jsonObj.put("Email", account.getEmail());
            params[1] = jsonObj.toString();

            // Execute service handler async task
            sh = (ServiceHandler) sh.execute(params);

            // Handle the response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
                    if (!response.equals("404") && !response.equals("500") && !response.equals("400")) {
                        new AlertDialog.Builder(KidSignup.this)
                                .setTitle("Sign up success")
                                .setCancelable(false)
                                .setPositiveButton("Navigation", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(KidSignup.this, KidNavigation.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Choose Account", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(KidSignup.this, ChooseAccountType.class);
                                        startActivity(intent);
                                    }
                                }).create().show();
                        return true;
                    }
                } else
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