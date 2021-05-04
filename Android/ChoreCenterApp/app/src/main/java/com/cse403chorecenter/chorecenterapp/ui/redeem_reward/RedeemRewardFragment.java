package com.cse403chorecenter.chorecenterapp.ui.redeem_reward;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.R;

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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new RedeemRewardRecyclerViewAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });

        mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == LayoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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

        String jsonStr = "{\"Rewards\": [{\"RewardId\": \"103\", \"ParentGoogleAccountId\": \"e654\",\"RewardName\": \"ice cream\"," +
                "\"Description\": \"a very very hot ice cream\",\"Points\": 2000,\"CreatedTime\": \"2021-4-2\", \"UpdatedTime\": \"2021-5-2\"," +
                "\"NumberOfRedemptions\": 7 }, {\"RewardId\": \"105\",\"ParentGoogleAccountId\": \"e654\",\"RewardName\": \"hotpot\"," +
                "\"Description\": \"a very very cold hotpot\",\"Points\": 10000,\"CreatedTime\": \"2021-4-12\",\"UpdatedTime\": \"2021-5-11\"," +
                "\"NumberOfRedemptions\": 2 }] }";
        // TODO: use http request to get actual data
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray rewards = jsonObj.getJSONArray("Rewards");

            // looping through All Rewards
            for (int i = 0; i < rewards.length(); i++) {
                JSONObject c = rewards.getJSONObject(i);
                mDataset.add(new RewardModel(c.getString("RewardName"), c.getLong("Points"),
                        c.getString("Description"), c.getString("RewardId")));
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Json parsing error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });

        }
    }

    /**
     * RewardModel helps handling store and retrieve information about rewards
     */
    public static class RewardModel {
        final String name;
        final long points;
        final String description;
        final String id;

        public RewardModel(String name, long points, String description, String id) {
            this.name = name;
            this.points = points;
            this.description = description;
            this.id = id;
        }

        String getName() {
            return name;
        }

        long getPoints() {
            return points;
        }

        String getDescription() {
            return description;
        }

        String getId() {
            return id;
        }
    }
}
