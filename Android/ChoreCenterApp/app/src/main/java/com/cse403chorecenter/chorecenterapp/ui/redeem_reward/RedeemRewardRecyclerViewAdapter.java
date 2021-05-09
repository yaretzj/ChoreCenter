package com.cse403chorecenter.chorecenterapp.ui.redeem_reward;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.UserLogin;

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
    public RedeemRewardRecyclerViewAdapter(List<RedeemRewardFragment.RewardModel> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
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
                    submitChore(RewardId);
                }
            });
        }
    }

    /** submit a chore for the kid account */
    public static boolean submitChore(String rewardId) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            // params[0] = "http://chorecenter.westus2.cloudapp.azure.com/api/children/rewards/redeem";
            params[0] = "http://10.0.2.2:80/api/children/rewards/redeem";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("RewardId", rewardId);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null) {
                    Log.i(TAG, response);
                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
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
