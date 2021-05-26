package com.cse403chorecenter.chorecenterapp.ui.redeem_reward;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.ui.submit_chore.SubmitChoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RedeemRewardFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected RedeemRewardRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<RewardModel> mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        try {
            initDataset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_redeem_reward, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewRedeemReward);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            // Restore saved layout manager type.
//            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        mAdapter = new RedeemRewardRecyclerViewAdapter(mDataset, this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        return rootView;
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() throws FileNotFoundException {
        mDataset = new ArrayList<>();

        // use http request to get rewards
        getRewards();
        getRedeemedRewards();
    }

    /**
     * RewardModel helps handling store and retrieve information about rewards
     */
    public static class RewardModel {
        private final String name;
        private final String points;
        private final String description;
        private final String id;
        private int numberOfRedemptions;

        public RewardModel(String name, String points, String description, String id, int numberOfRedemptions) {
            this.name = name;
            this.points = points;
            this.description = description;
            this.id = id;
            this.numberOfRedemptions = numberOfRedemptions;
        }

        public String getName() {
            return name;
        }

        public String getPoints() {
            return points;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public int getNumberOfRedemptions() {
            return numberOfRedemptions;
        }

        public void setNumberOfRedemptions(int numberOfRedemptions) {
            this.numberOfRedemptions = numberOfRedemptions;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof RewardModel) {
                return this.id.equals(((RewardModel) obj).id);
            }
            return false;
        }
    }

    /** Get all the rewards created by the parent */
    public boolean getRewards() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/rewards";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if(response != null) {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    try {
                        // Getting JSON Array node
                        JSONArray chores = jsonResponseObject.getJSONArray("Rewards");

                        // looping through All Rewards
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject c = chores.getJSONObject(i);
                            mDataset.add(new RedeemRewardFragment.RewardModel(c.getString("Name"), c.getString("Points"),
                                    c.getString("Description"), c.getString("RewardId"), 0));
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
                } else
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

    /** Get all the redeemed rewards created by the parent */
    public boolean getRedeemedRewards() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/rewards/history";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if(response != null) {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    try {
                        // Getting JSON Array node
                        JSONArray chores = jsonResponseObject.getJSONArray("RedeemedRewards");

                        // looping through All Rewards
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject c = chores.getJSONObject(i);
                            RewardModel newReward = new RewardModel(c.getString("Name"), "0",
                                    c.getString("Description"), c.getString("RewardId"), 0);
                            for (RewardModel rm : mDataset) {
                                if (newReward.equals(rm)) {
                                    rm.setNumberOfRedemptions(rm.getNumberOfRedemptions() + 1);
                                    break;
                                }
                            }
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
                } else
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
}
