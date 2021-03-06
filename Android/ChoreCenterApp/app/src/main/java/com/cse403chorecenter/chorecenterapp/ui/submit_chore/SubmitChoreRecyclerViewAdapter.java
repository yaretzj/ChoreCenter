package com.cse403chorecenter.chorecenterapp.ui.submit_chore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.UserLogin;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class SubmitChoreRecyclerViewAdapter extends RecyclerView.Adapter<SubmitChoreRecyclerViewAdapter.ChoreViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<SubmitChoreFragment.ChoreModel> mDataSet;

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
            textView = (TextView) v.findViewById(R.id.submitChoreTV);
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
    public SubmitChoreRecyclerViewAdapter(List<SubmitChoreFragment.ChoreModel> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public SubmitChoreRecyclerViewAdapter.ChoreViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_chore_item, viewGroup, false);

        return new SubmitChoreRecyclerViewAdapter.ChoreViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull SubmitChoreRecyclerViewAdapter.ChoreViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        SubmitChoreFragment.ChoreModel chore = mDataSet.get(position);
        String nameAndPoints = chore.getName() + ": " + chore.getPoints() + " points";
        viewHolder.textViewName.setText(nameAndPoints);
        String description = "Description: " + chore.getDescription();
        viewHolder.textViewDescription.setText(description);
        viewHolder.position = position;
        viewHolder.ChoreId = chore.getId();
        if (!chore.getStatus().equals("Created")) {
            viewHolder.completeChoreBtn.setEnabled(false);
            viewHolder.completeChoreBtn.setText("Completed");
        }

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    /** Return the size of your dataset (invoked by the layout manager) */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * An inner class holding individual views inside the recycler view adapter.
     */
    public static class ChoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        View rootView;
        int position;
        String ChoreId;
        Button completeChoreBtn;

        public ChoreViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewName = itemView.findViewById(R.id.submitChoreTV);
            textViewDescription = itemView.findViewById(R.id.submitChoreTV2);
            completeChoreBtn = itemView.findViewById(R.id.submitChoreBtn);
            completeChoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked chore id: " + ChoreId);

                    // submit chore alert
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Complete Chore")
                            .setMessage("Are you sure you have completed " + textViewName.getText())
                            .setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // submit chore
                                    View view = itemView.findViewById(R.id.submitChoreTV);
                                    if (submitChore(ChoreId)) {
                                        completeChoreBtn.setEnabled(false);
                                        completeChoreBtn.setText("Completed");
                                        Snackbar.make(view, R.string.submit_pop_up_success, Snackbar.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        Snackbar.make(view, R.string.submit_pop_up_failed, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null).show();
                }
            });
        }
    }

    /**
     * Uses service handler to process an asynchronous HTTP Post request
     * to submit a completed chore for the kid account.
     */
    public static boolean submitChore(String choreId) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/chores/" + choreId + "/update";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("Status", "Completed");
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
}
