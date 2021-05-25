package com.cse403chorecenter.chorecenterapp.ui.verify_chore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.ServiceHandlerDelete;
import com.cse403chorecenter.chorecenterapp.UserLogin;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class VerifyChoreRecyclerViewAdapter extends RecyclerView.Adapter<VerifyChoreRecyclerViewAdapter.ChoreViewHolder> {
    private static final String TAG = "VerifyChoreAdapter";

    private List<VerifyChoreFragment.ChoreModel> mDataSet;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

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
            textView = (TextView) v.findViewById(R.id.verifyChoreTV);
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
    public VerifyChoreRecyclerViewAdapter(List<VerifyChoreFragment.ChoreModel> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public VerifyChoreRecyclerViewAdapter.ChoreViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_verify_chore_item, viewGroup, false);

        return new VerifyChoreRecyclerViewAdapter.ChoreViewHolder(v, mListener);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull VerifyChoreRecyclerViewAdapter.ChoreViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        VerifyChoreFragment.ChoreModel chore = mDataSet.get(position);
        String nameAndPoints = chore.getName() + ": " + chore.getPoints() + " points";
        viewHolder.textViewName.setText(nameAndPoints);
        viewHolder.textViewDescription.setText("Description: " + chore.getDescription());
        String status = "Status: " + chore.getStatus();
        viewHolder.textViewStatus.setText(status);
        viewHolder.position = position;
        viewHolder.ChoreId = chore.getId();
        viewHolder.verifyChoreBtn.setVisibility((chore.getStatus().equals("Completed")) ? View.VISIBLE : View.INVISIBLE);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ChoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewStatus;
        Button verifyChoreBtn;
        View rootView;
        int position;
        String ChoreId;
        ImageView deleteIcon;

        public ChoreViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            rootView = itemView;
            textViewName = itemView.findViewById(R.id.verifyChoreTV);
            textViewDescription = itemView.findViewById(R.id.verifyChoreTV2);
            textViewStatus = itemView.findViewById(R.id.verifyChoreTV3);
            verifyChoreBtn = itemView.findViewById(R.id.verifyChoreBtn);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
            verifyChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked chore id: " + ChoreId);
                    // Send the http request
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Verify Chore")
                            .setMessage("Are you sure you want to verify " + textViewName.getText())
                            .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // submit chore
                                    if (verifyChore(ChoreId)) {
                                        verifyChoreBtn.setVisibility(View.INVISIBLE);
                                        String status = "Status: Verified";
                                        textViewStatus.setText(status);
                                        Snackbar.make(itemView.findViewById(R.id.verifyChoreTV), R.string.verify_pop_up_success, Snackbar.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        Snackbar.make(itemView.findViewById(R.id.verifyChoreTV), R.string.verify_pop_up_failed, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null).show();
                }
            });
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked delete chore: " + ChoreId);
                    if (listener != null && deleteChore(ChoreId)) {
                        int position = getAdapterPosition();
                        listener.onDeleteClick(position);
                    }
                }


            });
        }
    }

    /** submit a chore for the kid account */
    public static boolean verifyChore(String choreId) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/chores/" + choreId + "/update";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("Status", "Verified");
            jsonObj.put("AssignedTo", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
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

    public static boolean deleteChore(String choreId) {
        try {
            // checking account status on the server
            ServiceHandlerDelete sh = new ServiceHandlerDelete();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/chores/" + choreId;

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);

            params[1] = jsonObj.toString();
            sh = (ServiceHandlerDelete) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
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
