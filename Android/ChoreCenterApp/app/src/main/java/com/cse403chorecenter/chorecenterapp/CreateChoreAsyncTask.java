package com.cse403chorecenter.chorecenterapp;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An HTTP Post request for creation of a new chore through parent account.
 */
public class CreateChoreAsyncTask extends AsyncTask<String, Integer, String> {

    // Using android.os.AsyncTask interface
    // The params argument to doInBackground is the request body.

    @Override
    protected String doInBackground(String... params) {
        try {
            // Set request properties
            URL url = new URL(MainActivity.DNS + "api/parents/chores/new");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Write the post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params[0]);
            wr.flush();
            wr.close();

            // Return the response message
            String response = con.getResponseMessage();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
