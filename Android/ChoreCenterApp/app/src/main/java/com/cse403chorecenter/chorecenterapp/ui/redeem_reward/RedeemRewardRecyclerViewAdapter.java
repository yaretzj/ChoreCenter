package com.cse403chorecenter.chorecenterapp.ui.redeem_reward;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.UserLogin;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
                .inflate(R.layout.text_row_item, viewGroup, false);

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
                    // TODO: Set the http request
//                    try {
//                        URL url = new URL("10.0.2.2:5000/api/" + UserLogin.ACCOUNT_TYPE + "/rewards/redeem");
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                        conn.setRequestProperty("Accept","application/json");
//                        conn.setDoOutput(true);
//                        conn.setDoInput(true);
//
//                        JSONObject jsonObj = new JSONObject();
//                        jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
//                        jsonObj.put("RewardId", RewardId);
//
//                        Log.i("JSON", jsonObj.toString());
//                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                        os.writeBytes(jsonObj.toString());
//
//                        os.flush();
//                        os.close();
//
//                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
//                        Log.i("MSG" , conn.getResponseMessage());
//
//                        conn.disconnect();
//
//                        // TODO: handle response
//
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
                }
            });
        }
    }
}
