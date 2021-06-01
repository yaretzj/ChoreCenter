package com.cse403chorecenter.chorecenterapp.ui.reward_history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.ui.redeem_reward.RedeemRewardFragment;
import com.cse403chorecenter.chorecenterapp.ui.redeem_reward.RedeemRewardRecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This is the reward history fragment activity for displaying all the reward redemption history
 * for all the linked kid accounts of this parent account.
 */
public class RewardHistoryFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    protected List<ReedemedRewardModel> mDataset;
    protected RecyclerView mRecyclerView;
    protected RHistoryViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize dataset
        try {
            initDataset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Set up the recycler view adapter */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_history, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewRewardHistory);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RHistoryViewAdapter(mDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /** Initializes the dataset */
    private void initDataset() throws FileNotFoundException {
        mDataset = new ArrayList<>();
        // use http request to get rewards
        getRewards();
    }

    /**
     * Uses a service handler to send an HTTP Post request for getting all the redeemed rewards
     * created by this account {@code UserLogin.ACCOUNT_ID}.
     * @return true on successful request, false on failed request
     */
    private boolean getRewards() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/rewards/history";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if (response != null && response != "") {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    try {
                        // Getting JSON Array node
                        JSONArray chores = jsonResponseObject.getJSONArray("RedeemedRewards");

                        // notify user if no chores found
                        if (chores.length() == 0) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "No Rewards found", Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                        // looping through All Rewards
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject c = chores.getJSONObject(i);
                            mDataset.add(new ReedemedRewardModel(c.getString("Name"), c.getString("ChildName"),
                                    c.getString("Description"), c.getString("RewardId"), c.getString("RedeemedTime")));
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
                } else
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Error loading page please refresh", Snackbar.LENGTH_SHORT)
                            .show();
                    return false;
            } catch (ExecutionException e) {
                Log.e(TAG, "Async execution error: " + e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG, "Async interrupted error: " + e.getMessage());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return false;
    }

    /**
     * ReedemedRewardModel helps handling store and retrieve information about rewards
     */
    public static class ReedemedRewardModel {
        private final String name;
        private final String childName;
        private final String description;
        private final String id;
        private final String redeemedTime;

        public ReedemedRewardModel(String name, String childName, String description, String id, String reedemedTime) {
            this.name = name;
            this.childName = childName;
            this.description = description;
            this.id = id;
            this.redeemedTime = reedemedTime;
        }

        // Java Bean class get methods
        public String getName() {
            return name;
        }
        public String getChildName() {
            return childName;
        }
        public String getDescription() {
            return description;
        }
        public String getId() {
            return id;
        }
        public String getRedeemedTime() {
            return redeemedTime;
        }
    }

}
