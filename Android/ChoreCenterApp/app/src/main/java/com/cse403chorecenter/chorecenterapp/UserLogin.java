package com.cse403chorecenter.chorecenterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

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

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    public static GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("1008730141731-1lj701n3a3upvf71trttd97g6ugsj381.apps.googleusercontent.com")  // set up your own Google API console project to use here
            .build();
    private static final String TAG = "UserLogin";
    public static final int RC_SIGN_IN = 403;
    public static String ACCOUNT_TYPE;
    public static String ACCOUNT_ID;
    public static long ACCOUNT_BALANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        findViewById(R.id.button_login).setOnClickListener(this);

        // Get the Intent that started this activity "ChooseAccountType" and
        // extract the string passed through the intent
        Intent intent = getIntent();
        ACCOUNT_TYPE = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        ACCOUNT_ID = null;
        ACCOUNT_BALANCE = 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login:
                signIn();
                break;
                case R.id.button_delete_account:
                revokeAccess();
                break;

        }

    }

    /** Called when the user taps the Login button */
    public void signIn() {
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, GSO);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            ACCOUNT_ID = account.getId();
            // TODO: check if the account exists on server before proceeding
            // TODO: implement KidNavigation
//            if (accountExists(account)) {
//                if (ACCOUNT_TYPE.equals("parents")) {
                    startActivity(new Intent(this, ParentNavigation.class));
//                }
//                startActivity(new Intent(this, KidNavigation.class));
//            } else {
//                // direct to sign up activity
//                if (ACCOUNT_TYPE.equals("parents")) {
//                    startActivity(new Intent(this, ParentSignup.class));
//                }
//                startActivity(new Intent(this, KidSignup.class));
//            }
        }
    }

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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, check sign up
            if (account != null) {
                ACCOUNT_ID = account.getId();
                // TODO: check if the account exists on server before proceeding
                // TODO: implement KidNavigation
//                if (accountExists(account)) {
//                    if (ACCOUNT_TYPE.equals("parents")) {
                        startActivity(new Intent(this, ParentNavigation.class));
//                    }
//                    startActivity(new Intent(this, KidNavigation.class));
//                } else {
//                    // direct to sign up activity
//                    if (ACCOUNT_TYPE.equals("parents")) {
//                        startActivity(new Intent(this, ParentSignup.class));
//                    }
//                    startActivity(new Intent(this, KidSignup.class));
//                }
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

    public boolean accountExists(GoogleSignInAccount account) {
        // Set the http request for checking account status on server
        try {
            URL url = new URL("10.0.2.2:5000/api/" + ACCOUNT_TYPE + "/info");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", account.getId());

            Log.i("JSON", jsonObj.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObj.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();

            // TODO: parse response body

            return conn.getResponseCode() != 404;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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