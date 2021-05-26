package com.cse403chorecenter.chorecenterapp.ui.parent_all_rewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.UserLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AllRewardHistoryFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    protected List<RewardModel> mDataset;
    protected RecyclerView mRecyclerView;
    protected AllRewardHistoryViewAdapter mAdapter;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_all_rewards, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewParentAllRewardHistory);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AllRewardHistoryViewAdapter(mDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

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

    private boolean getRewards() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/rewards";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if(response != null) {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray chores = jsonResponseObject.getJSONArray("Rewards");

                    // looping through All Rewards
                    for (int i = 0; i < chores.length(); i++) {
                        JSONObject c = chores.getJSONObject(i);
                        mDataset.add(new RewardModel(c.getString("Name"), c.getString("Points"),
                                c.getString("Description"), c.getString("RewardId"), 0));
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

    private boolean getRedeemedRewards() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/rewards/history";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if(response != null) {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray chores = jsonResponseObject.getJSONArray("RedeemedRewards");

                    // looping through All Rewards
                    for (int i = 0; i < chores.length(); i++) {
                        JSONObject c = chores.getJSONObject(i);
                        RewardModel newReward = new RewardModel(c.getString("Name"), "0",
                                c.getString("Description"), c.getString("RewardId"), 0);
                        for (RewardModel rm : mDataset) {
                            if (rm.equals(newReward)) {
                                rm.setNumberOfRedemptions(rm.getNumberOfRedemptions() + 1);
                                break;
                            }
                        }
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
