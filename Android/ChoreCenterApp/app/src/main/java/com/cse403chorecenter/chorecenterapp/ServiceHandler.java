package com.cse403chorecenter.chorecenterapp;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHandler extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // write the post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            // get input stream
            BufferedReader br = null;
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                //br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                return String.valueOf(con.getResponseCode());
            }

            // get input as String
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
