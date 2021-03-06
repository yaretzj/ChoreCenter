package com.cse403chorecenter.chorecenterapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cse403chorecenter.chorecenterapp.R;

/**
 * The home fragment activity for parent account.
 * A convenient way for parent users to navigate to all available functionalities of the app.
 * This will be the main playground for users after incorporating graphics, animations and music.
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = view.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // The Following are all the buttons to every available functionality for parent accounts.

        // Create Chore
        Button bCreateChore = (Button) view.findViewById(R.id.button_home_create_chore);
        bCreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_create_chore);
            }
        });

        // Create Reward
        Button bCreateReward = (Button) view.findViewById(R.id.button_home_create_reward);
        bCreateReward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_create_reward);
            }
        });

        // Signout
        Button bSignout = (Button) view.findViewById(R.id.button_home_signout);
        bSignout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_sign_out);
            }
        });

        // Reward History
        Button bRewardHistory = (Button) view.findViewById(R.id.button_home_reward_history);
        bRewardHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_reward_history);
            }
        });

        // Reward List
        Button bAllRewards = (Button) view.findViewById(R.id.button_home_all_rewards);
        bAllRewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_parent_all_rewards);
            }
        });

        // Chore List
        Button bChoreList = (Button) view.findViewById(R.id.button_home_chore_list);
        bChoreList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_verify_chore);
            }
        });

        return view;
    }
}
