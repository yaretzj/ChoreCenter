package com.cse403chorecenter.chorecenterapp;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateChoreAsyncTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(MainActivity.DNS + "api/parents/chores/new");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // write the post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params[0]);
            wr.flush();
            wr.close();

            String response = con.getResponseMessage();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
