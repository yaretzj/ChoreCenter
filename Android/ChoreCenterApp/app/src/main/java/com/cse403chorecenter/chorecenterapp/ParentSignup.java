package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParentSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);
    }

    /** Called when the user taps the Sign up button */
    public void onClickSignUp(View view) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set http request for parent account creation
        // Set the http request
        try {
            URL url = new URL("10.0.2.2:5000/api/parents/new");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleTokenId", account.getIdToken());
            jsonObj.put("GoogleAccountId", account.getId());
            jsonObj.put("Name", account.getDisplayName());
            jsonObj.put("Email", account.getEmail());

            Log.i("JSON", jsonObj.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObj.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();

            // TODO: handle error status

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Intent intent = new Intent(this, ParentSignup.class);
            startActivity(intent);
        }

        Intent intent = new Intent(this, ChooseAccountType.class);
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}