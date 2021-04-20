package com.cse403chorecenter.chorecenterapp.ui.create_chore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.UserSignout;

public class CreateChoreFragment extends Fragment {
    private CreateChoreViewModel createChoreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createChoreViewModel = new ViewModelProvider(this).get(CreateChoreViewModel.class);
        View view = inflater.inflate(R.layout.fragment_create_chore, container, false);
        final TextView textView = view.findViewById(R.id.text_create_chore);

        EditText editChoreName = (EditText) view.findViewById(R.id.editCreateChoreName);
        EditText editChorePoints = (EditText) view.findViewById(R.id.editCreateChorePoints);

        Button button = (Button) view.findViewById(R.id.button_create_chore);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if(editChoreName == null || editChoreName.getText().toString().isEmpty()
                                || editChorePoints == null || editChorePoints.getText().toString().isEmpty()) {
                    textView.setText("please input both fields");
                    return;
                }

                String choreName = editChoreName.getText().toString();
                String chorePointsStr = editChorePoints.getText().toString();

                // Check chorePoints is an integer
                // TODO sanitise choreName input
                try {
                    int chorePointsInt = Integer.parseInt(chorePointsStr);
                    editChoreName.setText("");
                    editChorePoints.setText("");
                } catch (NumberFormatException e) {
                    textView.setText("please input a number for the chore points");
                    return;
                }

                textView.setText("Chore " + choreName + " created");
            }
        });

        return view;
    }
}
