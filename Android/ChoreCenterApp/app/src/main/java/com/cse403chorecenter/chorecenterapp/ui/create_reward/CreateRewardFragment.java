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

import com.cse403chorecenter.chorecenterapp.R;

public class CreateRewardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_reward, container, false);
        final TextView textView = view.findViewById(R.id.text_create_reward);

        EditText editRewardName = (EditText) view.findViewById(R.id.editCreateReward);
        EditText editRewardPoints = (EditText) view.findViewById(R.id.editCreateRewardPoints);

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

                String rewardName = editRewardName.getText().toString();
                String rewardPointsStr = editRewardPoints.getText().toString();

                // Check chorePoints is an integer
                try {
                    int rewardPointsInt = Integer.parseInt(rewardPointsStr);
                    editRewardName.setText("");
                    editRewardPoints.setText("");
                } catch (NumberFormatException e) {
                    textView.setText("please input a number for the reward points");
                    return;
                }

                textView.setText("Reward " + rewardName + " created");
            }
        });

        return view;
    }
}
