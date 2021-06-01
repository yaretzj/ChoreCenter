package com.cse403chorecenter.chorecenterapp;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An Http Post request handler for asynchronous tasks.
 * The {@code params} argument to {@code doInBackground} has first element
 * as a url and the second element as the request body.
 */
public class ServiceHandler extends AsyncTask<String, Integer, String> {
    private static final String TAG = "ServiceHandler";

    @Override
    protected String doInBackground(String... params) {
        try {
            // Set request properties
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Write the post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            // Get response as stream if non-error code
            // Otherwise return response code
            BufferedReader br = null;
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                //br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                return String.valueOf(con.getResponseCode());
            }

            // Get response as String
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            return "";
        }
    }
}
