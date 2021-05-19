package com.cse403chorecenter.chorecenterapp.ui.reward_history;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.UserLogin;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RHistoryViewAdapter extends RecyclerView.Adapter<RHistoryViewAdapter.RRewardViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<RewardHistoryFragment.ReedemedRewardModel> mDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public RHistoryViewAdapter(List<RewardHistoryFragment.ReedemedRewardModel> dataSet) {
        mDataSet = dataSet;
    }

    @NotNull
    @Override
    public RRewardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_reward_history_item, viewGroup, false);

        return new RRewardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RRewardViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        RewardHistoryFragment.ReedemedRewardModel reward = mDataSet.get(position);
        String nameAndTime = reward.getChildName() + " redeemed " + reward.getName() + " at " + reward.getRedeemedTime();
        viewHolder.textViewName.setText(nameAndTime);
        viewHolder.textViewDescription.setText("Description: " + reward.getDescription());
        viewHolder.position = position;
        viewHolder.RewardId = reward.getId();
    }

    public static class RRewardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        View rootView;
        int position;
        String RewardId;

        public RRewardViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewName = itemView.findViewById(R.id.rewardHistoryTV);
            textViewDescription = itemView.findViewById(R.id.rewardHistoryTV2);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}