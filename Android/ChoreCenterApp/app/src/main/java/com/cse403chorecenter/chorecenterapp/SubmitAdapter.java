package com.cse403chorecenter.chorecenterapp;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SubmitAdapter extends RecyclerView.Adapter<SubmitAdapter.SubmitViewHolder> {
    private static String ChoreId;
    private ArrayList<SubmitItem> submitList;
    private OnButtonClickListener mListener;

    public interface OnButtonClickListener {
        void onButtonClick(View v);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    public static class SubmitViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView point;
        public TextView description;
        public TextView time;
        public Button submit_button;
        public String id;


        public SubmitViewHolder(View itemView, final OnButtonClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.text_kid_chore);
            point = itemView.findViewById(R.id.text_kid_point);
            description = itemView.findViewById(R.id.text_kid_chore_description);
            time = itemView.findViewById(R.id.text_create_time);
            submit_button = itemView.findViewById(R.id.submit_button);
            ChoreId = id;
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitChore(ChoreId);
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onButtonClick(v);
                        }
                    }
                }
            });
        }
    }

    public SubmitAdapter(ArrayList<SubmitItem> list) {
        submitList = list;
    }
    @NonNull
    @Override
    public SubmitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.submit_item, parent, false);
        SubmitViewHolder svh = new SubmitViewHolder(v, mListener);
        return svh;
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull SubmitViewHolder holder, int position) {
        SubmitItem currentItem = submitList.get(position);
        holder.name.setText("Chore Name: " + currentItem.getChoreName());
        holder.point.setText("Chore Point: " + currentItem.getChorePoint());
        holder.description.setText("Chore Description: " + currentItem.getChoreDescription());
        holder.time.setText("Created Time: " + currentItem.getChoreTime());
        holder.id = currentItem.getChoreID();

    }

    @Override
    public int getItemCount() {
        return submitList.size();
    }


    public static boolean submitChore(String choreId) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = "http://chorecenter.westus2.cloudapp.azure.com/api/children/chores/" + ChoreId + "/update";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            jsonObj.put("Status", "Completed");
            jsonObj.put("AssignedTo", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
//                    Log.i(TAG, response);
                    return !response.equals("404") && !response.equals("500") && !response.equals("400");
                }
                return false;
            } catch (ExecutionException e) {
//                Log.e(TAG, "Async execution error: " + e.getMessage());
            } catch (InterruptedException e) {
//                Log.e(TAG, "Async interrupted error: " + e.getMessage());
            }
        } catch (JSONException e) {
//            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return false;
    }
}
