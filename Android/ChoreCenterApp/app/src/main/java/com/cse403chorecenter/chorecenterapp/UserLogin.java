package com.cse403chorecenter.chorecenterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * This activity prompts the user to sign in his or her Google account
 * and automatically authenticates the user's Google Account with the backend server.
 *
 * <p>If the user is successfully authenticated, start the main user navigation page of
 * the selected account type.
 * If the user does not yet have a Chore Center account linked to the signed-in
 * Google account, start the user sign up activity with the appropriate account type
 * using the Signed-in Google account.
 * If the authentication fails, return to the ChooseAccountType activity.
 */
public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserLogin";

    // Used for the request code of getting result from Google SignIn activity
    public static final int RC_SIGN_IN = 403;

    // Options object for the Google SignIn
    public static GoogleSignInOptions GSO;

    // User account info to be populated
    public static String ACCOUNT_TYPE;
    public static String ACCOUNT_DISPLAY_NAME;
    public static String ACCOUNT_POINTS;
    public static String ACCOUNT_ID;
    public static String ACCOUNT_ID_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        findViewById(R.id.button_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login:
                signIn();
            case R.id.button_delete_account:
                revokeAccess();
            default:
                // no action
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the Intent that started current activity and
        // extract the string passed through MainActivity.EXTRA_MESSAGE.
        // This is used to determine the type of account the user tries to sign in.
        Intent intent = getIntent();
        ACCOUNT_TYPE = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Populate Google SignIn Options
        GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("1090737446393-3im1mga2cqjkj3nambmkcm8jf4oohtt5.apps.googleusercontent.com")  // set up Google API console project to use here
                .build();

        // Default values without user signed in
        ACCOUNT_DISPLAY_NAME = "Android";
        ACCOUNT_POINTS = "";
        ACCOUNT_ID = "1";
        ACCOUNT_ID_TOKEN = "exp_token";
    }

    /** When {@code Login} button is clicked, start GoogleSignIn activity. */
    public void signIn() {
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, GSO);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    /** When an activity is finished, handle the result according to {@code requestCode}.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Handles the Google SignIn activity result by querying the backend server to authenticate
     * the user's Google account. If the Google account is not linked to a Chore Center account,
     * start the appropriate user sign up activity. If the {@code completedTask} result is null,
     * the Google SignIn has failed, so start the ChooseAccountType activity.
     * @param completedTask A task with the Google SignIn result
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                // Signed in successfully. Populate user account information.
                ACCOUNT_DISPLAY_NAME = account.getDisplayName();
                ACCOUNT_ID = account.getId();
                ACCOUNT_ID_TOKEN = account.getIdToken();

                // Check if the account exists on server before proceeding
                if (accountExists(account)) {
                    if (ACCOUNT_TYPE.equals("parents")) {
                        startActivity(new Intent(this, ParentNavigation.class));
                    } else {
                        startActivity(new Intent(this, KidNavigation.class));
                    }
                } else {
                    // The account does not exist, direct to user sign up
                    if (ACCOUNT_TYPE.equals("parents")) {
                        startActivity(new Intent(this, ParentSignup.class));
                    } else {
                        startActivity(new Intent(this, KidSignup.class));
                    }
                }
            } else {
                // Sign in failed, retry
                Intent intent = new Intent(this, ChooseAccountType.class);
                startActivity(intent);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Intent intent = new Intent(this, ChooseAccountType.class);
            startActivity(intent);
        }
    }

    /**
     * Queries the backend server whether there is an existing Chore Center account
     * signed up using {@code account}.
     * @param account the user's signed-in Google account
     * @return true if the user account exists and false otherwise.
     */
    public boolean accountExists(GoogleSignInAccount account) {
        try {
            // Instantiate a service handler and populate the argument appropriately
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/" + ACCOUNT_TYPE + "/info";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", ACCOUNT_ID);
            jsonObj.put("GoogleTokenId", ACCOUNT_ID_TOKEN);
            params[1] = jsonObj.toString();

            // Execute service handler async task
            sh = (ServiceHandler) sh.execute(params);

            // Handle the response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
                    if (!response.equals("404") && !response.equals("500") && !response.equals("400")) {
                        if (ACCOUNT_TYPE.equals("children")) {
                            JSONObject jsonObject = new JSONObject(response);
                            ACCOUNT_POINTS = "Points: " + String.valueOf(jsonObject.getInt("Points"));
                        }
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

    /** Revokes Google accounts Signed in on the device in the past. */
    private void revokeAccess() {
        GoogleSignIn.getClient(this, GSO).revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}