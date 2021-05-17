package com.cse403chorecenter.chorecenterapp.ui.redeem_reward;

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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RedeemRewardRecyclerViewAdapter extends RecyclerView.Adapter<RedeemRewardRecyclerViewAdapter.RewardViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<RedeemRewardFragment.RewardModel> mDataSet;

    public RedeemRewardFragment mFragment;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.redeemRewardTV);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public RedeemRewardRecyclerViewAdapter(List<RedeemRewardFragment.RewardModel> dataSet, RedeemRewardFragment fragment) {
        mDataSet = dataSet;
        mFragment = fragment;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public RewardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_reward_item, viewGroup, false);

        return new RewardViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        RedeemRewardFragment.RewardModel reward = mDataSet.get(position);
        String nameAndPoints = reward.getName() + ": " + reward.getPoints();
        viewHolder.textViewName.setText(nameAndPoints);
        viewHolder.textViewDescription.setText(reward.getDescription());
        viewHolder.position = position;
        viewHolder.RewardId = reward.getId();
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
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
            textViewName = itemView.findViewById(R.id.redeemRewardTV);
            textViewDescription = itemView.findViewById(R.id.redeemRewardTV2);
            itemView.findViewById(R.id.redeemRewardBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked reward id: " + RewardId);

                    // redeem reward
                    if (submitChore(RewardId, itemView.findViewById(R.id.redeemRewardTV))) {
                        Snackbar.make(itemView.findViewById(R.id.redeemRewardTV), R.string.redeem_pop_up_success, Snackbar.LENGTH_SHORT)
                                .show();
                        Log.i(TAG, v.getRootView().getRootView().toString());
                        NavigationView navigationView = (NavigationView) v.getRootView().findViewById(R.id.kid_nav_view);
                        View headerView = navigationView.getHeaderView(0);
                        TextView pointsTV = headerView.findViewById(R.id.accountPointsTV);
                        String accountPoints = "Points: " + UserLogin.ACCOUNT_POINTS;
                        pointsTV.setText(accountPoints);
                    }
                }
            });
        }
    }

    /** submit a chore for the kid account */
    public static boolean submitChore(String rewardId, View view) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/rewards/redeem";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("RewardId", rewardId);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
                    Log.i(TAG, response);
                    if (response.equals("405")) {
                        Snackbar.make(view, R.string.redeem_pop_up_failed, Snackbar.LENGTH_SHORT).show();
                    }
                    if (!response.equals("404") && !response.equals("405") && !response.equals("500") && !response.equals("400")) {
                        JSONObject jsonObject = new JSONObject(response);
                        UserLogin.ACCOUNT_POINTS = String.valueOf(jsonObject.getInt("RemainingPoints"));
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
