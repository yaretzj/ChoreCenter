package com.cse403chorecenter.chorecenterapp.ui.create_chore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cse403chorecenter.chorecenterapp.CreateChoreAsyncTask;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.UserLogin;
import com.cse403chorecenter.chorecenterapp.UserSignout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * This is the fragment activity for parent account to create chores.
 */
public class CreateChoreFragment extends Fragment {
    private CreateChoreViewModel createChoreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createChoreViewModel = new ViewModelProvider(this).get(CreateChoreViewModel.class);
        View view = inflater.inflate(R.layout.fragment_create_chore, container, false);
        final TextView textView = view.findViewById(R.id.text_create_chore);

        EditText editChoreName = (EditText) view.findViewById(R.id.editCreateChoreName);
        EditText editChorePoints = (EditText) view.findViewById(R.id.editCreateChorePoints);
        // Chore description can be null or empty
        EditText editChoreDesc = (EditText) view.findViewById(R.id.editChoreDescription);


        // context and duration for the Toast
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        int yOffset = 70;


        Button button = (Button) view.findViewById(R.id.button_create_chore);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (editChoreName == null || editChoreName.getText().toString().isEmpty()
                                || editChorePoints == null || editChorePoints.getText().toString().isEmpty()) {
                    textView.setText("please input the chore name and chore points");

                    Toast toastMessage = Toast.makeText(context, "Please input the chore name and chore points", duration);
                    toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                    toastMessage.show();
                    return;
                }

                // Null description is represented by an empty string
                if (editChoreDesc == null) {
                    editChoreDesc.setText("");
                }

                String choreName = editChoreName.getText().toString();
                String chorePointsStr = editChorePoints.getText().toString();
                String choreDesc = editChoreDesc.getText().toString();
                int chorePointsInt;

                // Check chorePoints is an integer
                try {
                    chorePointsInt = Integer.parseInt(chorePointsStr);
                    editChoreName.setText("");
                    editChorePoints.setText("");
                    editChoreDesc.setText("");
                } catch (NumberFormatException e) {
                    textView.setText("please input a number for the chore points");
                    Toast toastMessage = Toast.makeText(context, "Please input a number for the chore points", duration);
                    toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                    toastMessage.show();
                    return;
                }
                // input checks passed
                //send to server
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
                    jsonObject.put("Name", choreName);
                    jsonObject.put("Description", choreDesc);
                    jsonObject.put("Points", chorePointsInt);

                } catch (JSONException e) {
                    Log.e("CreateChore", "json parsing " + e.getMessage());
                }

                CreateChoreAsyncTask networkRequest = new CreateChoreAsyncTask();
                networkRequest = (CreateChoreAsyncTask) networkRequest.execute(jsonObject.toString());

                // output response
                try {
                    String response = networkRequest.get();
                    if(response != null) {
                        textView.setText(response);
                        Toast toastMessage = Toast.makeText(context, response, duration);
                        toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                        toastMessage.show();
                    } else
                        textView.setText("failed");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
