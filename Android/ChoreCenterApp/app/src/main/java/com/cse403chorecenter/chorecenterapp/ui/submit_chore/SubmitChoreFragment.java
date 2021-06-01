package com.cse403chorecenter.chorecenterapp.ui.submit_chore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.SubmitChore;
import com.cse403chorecenter.chorecenterapp.ui.redeem_reward.RedeemRewardRecyclerViewAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * This is the submit chore fragment activity that is used by kid account to submit completed
 * chores to the linked parent account.
 */
public class SubmitChoreFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // Views
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected SubmitChoreRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<SubmitChoreFragment.ChoreModel> mDataset;

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
        View rootView = inflater.inflate(R.layout.fragment_submit_chore, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.submitChoreRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = SubmitChoreFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (SubmitChoreFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new SubmitChoreRecyclerViewAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        // This can be used to implement a second Grid layout
//        mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
//        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setRecyclerViewLayoutManager(SubmitChoreFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER);
//            }
//        });

//        mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
//        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setRecyclerViewLayoutManager(SubmitChoreFragment.LayoutManagerType.GRID_LAYOUT_MANAGER);
//            }
//        });

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(SubmitChoreFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == SubmitChoreFragment.LayoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = SubmitChoreFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = SubmitChoreFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() throws FileNotFoundException {
        mDataset = new ArrayList<>();

        // use http request to get chores
        getChores();
    }

    /**
     * RewardModel helps handling store and retrieve information about rewards
     */
    public static class ChoreModel {
        private final String name;
        private final long points;
        private final String description;
        private final String id;
        private final String status;

        public ChoreModel(String name, long points, String description, String id, String status) {
            this.name = name;
            this.points = points;
            this.description = description;
            this.id = id;
            this.status = status;
        }

        // Java Bean class get methods
        public String getName() {
            return name;
        }
        public long getPoints() {
            return points;
        }
        public String getDescription() {
            return description;
        }
        public String getId() {
            return id;
        }
        public String getStatus() { return status; }
    }

    /** Gets all the chores created by the parent */
    public boolean getChores() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/chores";

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
                        JSONArray chores = jsonResponseObject.getJSONArray("Chores");

                        // looping through All Rewards
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject c = chores.getJSONObject(i);
                            mDataset.add(new SubmitChoreFragment.ChoreModel(c.getString("Name"), c.getLong("Points"),
                                    c.getString("Description"), c.getString("ChoreId"), c.getString("Status")));
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
