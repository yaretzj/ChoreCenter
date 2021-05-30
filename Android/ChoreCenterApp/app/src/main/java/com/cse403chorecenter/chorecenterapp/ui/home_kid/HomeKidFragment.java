package com.cse403chorecenter.chorecenterapp.ui.home_kid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cse403chorecenter.chorecenterapp.R;

/**
 * The home fragment activity for kid account.
 * A convenient way for kid users to navigate to all available functionalities of the app.
 * This will be the main playground for users after incorporating graphics, animations and music.
 */
public class HomeKidFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_kid, container, false);

        // The Following are all the buttons to every available functionality for kid accounts.

        // SubmitChore
        Button bSubmitChore = (Button) view.findViewById(R.id.button_home_kid_submit_chore);
        bSubmitChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_submit_chore);
            }
        });

        // Redeem Reward
        Button bRedeemReward = (Button) view.findViewById(R.id.button_home_kid_redeem_reward);
        bRedeemReward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_redeem_reward);
            }
        });

        // Sign Out
        Button bSignOut = (Button) view.findViewById(R.id.button_home_kid_sign_out);
        bSignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_sign_out);
            }
        });

        return view;
    }
}
