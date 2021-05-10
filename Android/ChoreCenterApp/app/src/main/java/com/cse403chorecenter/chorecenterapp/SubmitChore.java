package com.cse403chorecenter.chorecenterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.cse403chorecenter.chorecenterapp.ui.submit_chore.SubmitChoreFragment;
import com.cse403chorecenter.chorecenterapp.ui.submit_chore.SubmitChoreRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class SubmitChore extends AppCompatActivity implements View.OnClickListener {
    Button submit_button;
    ListView listView;
//    private RecyclerView;
    ArrayList<ChoresData> dataChoreArrayList = new ArrayList<>();
    ArrayList<ChoreModel> choreItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_chore);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Submit Chore");
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list_view);
        String url = "http://chorecenter.westus2.cloudapp.azure.com/api/children/chores";
        submit_button = findViewById(R.id.submit_button);
        try {
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = url;
            JSONObject jsonObj = new JSONObject();
//          jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            jsonObj.put("GoogleAccountId", 2);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);
            //progress dialog
//            ProgressDialog dialog = new ProgressDialog(this);
//            dialog.setMessage("Please wait");
//            dialog.setCancelable(true);
//            dialog.show();
            try {
                String response = sh.get();

                if (response != null) {
//                    dialog.dismiss();
                    JSONObject jsonResponseObject = new JSONObject(response);
                    try {
                        JSONArray chores = jsonResponseObject.getJSONArray("Chores");
                        parseArray(chores);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void parseArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                ChoresData data = new ChoresData();
                ChoreModel chore = new ChoreModel(object.getString("Name"),
                        object.getInt("Points"),
                        object.getString("Description"),
                        object.getString("ChoreId"));
                data.setNameChore(object.getString("Name"));
                data.setCreateTime(object.getString("CreatedTime"));
                dataChoreArrayList.add(data);
                choreItem.add(chore);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataChoreArrayList.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = getLayoutInflater().inflate(R.layout.submit_item, null);
                    ChoresData data = dataChoreArrayList.get(position);
                    TextView choreName = view.findViewById(R.id.text_kid_chore);
                    TextView time = view.findViewById(R.id.text_create_time);
                    choreName.setText(data.getNameChore());
                    time.setText(data.getCreateTime());

                    return view;
                }

//                @Override
//                public void onBindViewHolder(@NonNull ChoreViewHolder viewHolder, final int position) {
////                    Log.d(TAG, "Element " + position + " set.");
//
//                    // Get element from your dataset at this position and replace the contents of the view
//                    // with that element
//                    ChoreModel chore = choreItem.get(position);
//                    String nameAndPoints = chore.getName() + ": " + chore.getPoints();
//                    viewHolder.textViewName.setText(nameAndPoints);
//                    viewHolder.textViewDescription.setText(chore.getDescription());
//                    viewHolder.position = position;
//                    viewHolder.ChoreId = chore.getId();
//                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }

    public static class ChoreModel {
        private final String name;
        private final long points;
        private final String description;
        private final String id;

        public ChoreModel(String name, long points, String description, String id) {
            this.name = name;
            this.points = points;
            this.description = description;
            this.id = id;
        }

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
    }

    public static class ChoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        View rootView;
        int position;
        String ChoreId;

        public ChoreViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewName = itemView.findViewById(R.id.submitChoreTV);
            textViewDescription = itemView.findViewById(R.id.submitChoreTV2);
            itemView.findViewById(R.id.submitChoreBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "Clicked chore id: " + ChoreId);
                    // Send the http request

                    // submit chore
                    submitChore(ChoreId);
                }
            });
        }
    }

    public static boolean submitChore(String choreId) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/children/chores/" + choreId + "/update";

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