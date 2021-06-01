package com.cse403chorecenter.chorecenterapp.ui.verify_chore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This is the verify chore fragment activity that displays all the chores
 * created by this parent account {@code UserLogin.ACCOUNT_ID}.
 * This activity also includes the functionality to delete un-verified chores.
 */
public class VerifyChoreFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // Views
    protected VerifyChoreFragment.LayoutManagerType mCurrentLayoutManagerType;
    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected VerifyChoreRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<VerifyChoreFragment.ChoreModel> mDataset;
    protected AlertDialog alert;

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
        View rootView = inflater.inflate(R.layout.fragment_verify_chore, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.verifyChoreRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = VerifyChoreFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (VerifyChoreFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new VerifyChoreRecyclerViewAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        mAdapter.setOnItemClickListener(new VerifyChoreRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                deleteClicked(position);
            }
        });

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(VerifyChoreFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == VerifyChoreFragment.LayoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = VerifyChoreFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = VerifyChoreFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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
     * Update the list when delete is clicked and update view adapter
     */
    public void deleteClicked(int position) {
        mDataset.remove(position);
        mAdapter.notifyItemRemoved(position);
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
        public String getStatus() {
            return status;
        }
    }

    /**
     * Uses a service handler to send asynchronous HTTP Post request to
     * get all the chores created by this parent account {@code UserLogin.ACCOUNT_ID}.
     */
    public boolean getChores() {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/chores";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();

                if(response != null && response != "") {
                    Log.i(TAG, response);
                    JSONObject jsonResponseObject = new JSONObject(response);

                    try {
                        // Getting JSON Array node
                        JSONArray chores = jsonResponseObject.getJSONArray("Chores");

                        if (chores.length() == 0) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "No chores found", Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                        // looping through All Rewards
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject c = chores.getJSONObject(i);
                            mDataset.add(new VerifyChoreFragment.ChoreModel(c.getString("Name"), c.getLong("Points"),
                                    c.getString("Description"), c.getString("ChoreId"), c.getString("Status")));
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
}
