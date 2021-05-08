package com.cse403chorecenter.chorecenterapp.ui.create_reward;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cse403chorecenter.chorecenterapp.CreateChoreAsyncTask;
import com.cse403chorecenter.chorecenterapp.CreateRewardAsyncTask;
import com.cse403chorecenter.chorecenterapp.R;

import java.util.concurrent.ExecutionException;

public class CreateRewardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_reward, container, false);
        final TextView textView = view.findViewById(R.id.text_create_reward);

        EditText editRewardName = (EditText) view.findViewById(R.id.editCreateReward);
        EditText editRewardPoints = (EditText) view.findViewById(R.id.editCreateRewardPoints);
        // Reward description can be null or empty
        EditText editRewardDesc = (EditText) view.findViewById(R.id.editRewardDescription);

        Button button = (Button) view.findViewById(R.id.button_create_reward);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(editRewardName == null || editRewardName.getText().toString().isEmpty()
                        || editRewardPoints == null || editRewardPoints.getText().toString().isEmpty()) {
                    textView.setText("please input both fields");
                    return;
                }

                // Null description is represented by an empty string
                if (editRewardDesc == null) {
                    editRewardDesc.setText("");
                }

                String rewardName = editRewardName.getText().toString();
                String rewardPointsStr = editRewardPoints.getText().toString();
                String rewardDesc = editRewardDesc.getText().toString();
                int rewardPointsInt;

                // Check chorePoints is an integer
                try {
                    rewardPointsInt = Integer.parseInt(rewardPointsStr);
                    editRewardName.setText("");
                    editRewardPoints.setText("");
                    editRewardDesc.setText("");
                } catch (NumberFormatException e) {
                    textView.setText("please input a number for the reward points");
                    return;
                }

                // input checks passed
                //send to server
                String request = "{\"GoogleAccountId\": \"1\", \"Name\": \"" + rewardName +
                                                    "\", \"Description\": \""+ rewardDesc + "\", \"Points\": \"" + rewardPointsInt + "\"}";
                CreateRewardAsyncTask networkRequest = new CreateRewardAsyncTask();
                networkRequest = (CreateRewardAsyncTask) networkRequest.execute(request);

                // output response
                try {
                    String response = networkRequest.get();
                    if(response != null) {
                        textView.setText(response.toString());
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
