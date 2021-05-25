package com.cse403chorecenter.chorecenterapp.ui.parent_all_rewards;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class AllRewardHistoryViewAdapter extends RecyclerView.Adapter<AllRewardHistoryViewAdapter.RewardViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<AllRewardHistoryFragment.RewardModel> mDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public AllRewardHistoryViewAdapter(List<AllRewardHistoryFragment.RewardModel> dataSet) {
        mDataSet = dataSet;
    }

    @NotNull
    @Override
    public RewardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_reward_history_item, viewGroup, false);

        return new RewardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRewardHistoryViewAdapter.RewardViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        AllRewardHistoryFragment.RewardModel reward = mDataSet.get(position);
        String nameAndTime = reward.getPoints() + " redeemed " + reward.getName() + " at " + reward.getNumberOfRedemptions();
        viewHolder.textViewName.setText(nameAndTime);
        viewHolder.textViewDescription.setText("Description: " + reward.getDescription());
        viewHolder.position = position;
        viewHolder.RewardId = reward.getId();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        View rootView;
        int position;
        String RewardId;

        public RewardViewHolder(@NonNull View itemView) {
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