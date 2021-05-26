package com.cse403chorecenter.chorecenterapp.ui.parent_all_rewards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandlerDelete;
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
public class AllRewardHistoryViewAdapter extends RecyclerView.Adapter<AllRewardHistoryViewAdapter.RewardViewHolder> {
    private static final String TAG = "AllRewardAdapter";
    private List<AllRewardHistoryFragment.RewardModel> mDataSet;

    public AllRewardHistoryFragment mFragment;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public AllRewardHistoryViewAdapter(List<AllRewardHistoryFragment.RewardModel> dataSet, AllRewardHistoryFragment fragment) {
        mDataSet = dataSet;
        mFragment = fragment;
    }

    @NotNull
    @Override
    public RewardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_parent_all_rewards_text_row_item, viewGroup, false);

        return new RewardViewHolder(v);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AllRewardHistoryViewAdapter.RewardViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        AllRewardHistoryFragment.RewardModel reward = mDataSet.get(position);
        String nameAndTime = reward.getName() + ", (" + reward.getPoints() + " pt)";
        viewHolder.textViewName.setText(nameAndTime);
        viewHolder.textViewDescription.setText("Description: " + reward.getDescription());
        String redeemed = "Number of redemptions: " + String.valueOf(reward.getNumberOfRedemptions());
        viewHolder.textViewStatus.setText(redeemed);
        if (reward.getNumberOfRedemptions() != 0) {
            viewHolder.deleteBtn.setVisibility(View.INVISIBLE);
        }
        viewHolder.position = position;
        viewHolder.RewardId = reward.getId();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewStatus;
        ImageView deleteBtn;
        View rootView;
        int position;
        String RewardId;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewName = itemView.findViewById(R.id.parentGetRewardTV);
            textViewDescription = itemView.findViewById(R.id.parentGetRewardTV2);
            textViewStatus = itemView.findViewById(R.id.parentGetRewardTV3);
            deleteBtn = itemView.findViewById(R.id.parentDeleteRewardBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked reward id: " + RewardId);

                    // redeem reward
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Delete Reward")
                            .setMessage("Are you sure you want delete " + textViewName.getText())
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // delete reward
                                    View view = itemView.findViewById(R.id.parentGetRewardTV);
                                    if (deleteReward(RewardId, view)) {
                                        deleteBtn.setVisibility(View.INVISIBLE);
                                        Snackbar.make(view, R.string.delete_pop_up_success, Snackbar.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        Snackbar.make(view, R.string.delete_pop_up_failed, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null).show();
                }
            });
        }
    }

    /** delete an unredeemed reward for the parent account */
    public static boolean deleteReward(String rewardId, View view) {
        try {
            // populate a service handler delete request
            ServiceHandlerDelete sh = new ServiceHandlerDelete();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/rewards/" + rewardId;

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandlerDelete) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
                    Log.i(TAG, response);
                    if (!response.equals("404") && !response.equals("405") && !response.equals("500") && !response.equals("400")) {
                        return true;
                    }
                    return false;
                }
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