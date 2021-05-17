package com.cse403chorecenter.chorecenterapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class SubmitChore extends AppCompatActivity {
    private Button submit_button;
    private ArrayList<SubmitItem> choreList = new ArrayList<>();

    private RecyclerView recyclerView;
    private SubmitAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_chore);

        submit_button = findViewById(R.id.submit_button);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Complete Chore");
        actionBar.setDisplayHomeAsUpEnabled(true);
        createChoreList();
        buildRecyclerView();
    }


    public void createChoreList() {
        String url = "http://chorecenter.westus2.cloudapp.azure.com/api/children/chores";
        try {
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = url;
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleAccountId", com.cse403chorecenter.chorecenterapp.UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);
            try {
                String response = sh.get();
                if (response != null) {
                    JSONObject jsonResponseObject = new JSONObject(response);
                    try {
                        JSONArray chores = jsonResponseObject.getJSONArray("Chores");
                        for (int i = 0; i < chores.length(); i++) {
                            JSONObject object = chores.getJSONObject(i);
                            if (object.getString("Status").equals("Created")) {
                                SubmitItem chore = new SubmitItem(object.getString("Name"),
                                        object.getInt("Points"),
                                        object.getString("Description"),
                                        object.getString("CreatedTime"),
                                        object.getString("ChoreId"));
                                chore.setChoreName(object.getString("Name"));
                                chore.setChorePoint(object.getInt("Points"));
                                chore.setChoreDescription(object.getString("Description"));
                                chore.setChoreTime(object.getString("CreatedTime"));
                                chore.setChoreID(object.getString("ChoreId"));
                                choreList.add(chore);
                            }
                        }

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


    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adapter = new SubmitAdapter(choreList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(new SubmitAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View v) {
                v.setEnabled(false);
                submit_button = (Button) v;
                submit_button.setText("Completed");
            }
        });
    }
}
