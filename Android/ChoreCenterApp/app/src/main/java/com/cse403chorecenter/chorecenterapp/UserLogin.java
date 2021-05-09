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

public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserLogin";
    public static final int RC_SIGN_IN = 403;
    public static GoogleSignInOptions GSO;
    public static String ACCOUNT_TYPE;
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

        // Get the Intent that started this activity "ChooseAccountType" and
        // extract the string passed through MainActivity.EXTRA_MESSAGE
        Intent intent = getIntent();
        GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("551695683870-tn6t4q27f5qpfe8jb61dt0jbr6qjf1fm.apps.googleusercontent.com")  // set up Google API console project to use here
                .build();
        ACCOUNT_TYPE = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        ACCOUNT_ID = "5";
        ACCOUNT_ID_TOKEN = "exp_token";
    }

    /** Called when the user taps the Login button */
    public void signIn() {
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, GSO);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
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

            if (account != null) {
                // Signed in successfully

                //ACCOUNT_ID = account.getId();
                // check if the account exists on server before proceeding
                if (accountExists(account)) {
                    if (ACCOUNT_TYPE.equals("parents")) {
                        startActivity(new Intent(this, ParentNavigation.class));
                    } else {
                        startActivity(new Intent(this, KidNavigation.class));
                    }
                } else {
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

    public boolean accountExists(GoogleSignInAccount account) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            if (ACCOUNT_TYPE.equals("parents")) {
                // params[0] = "http://chorecenter.westus2.cloudapp.azure.com/api/parents/info";
                params[0] = "http://10.0.2.2:80/api/parents/info";
            } else {
                // params[0] = "http://chorecenter.westus2.cloudapp.azure.com/api/children/info";
                params[0] = "http://10.0.2.2:80/api/children/info";
            }

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", ACCOUNT_ID);
            jsonObj.put("GoogleTokenId", ACCOUNT_ID_TOKEN);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null) {
                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
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