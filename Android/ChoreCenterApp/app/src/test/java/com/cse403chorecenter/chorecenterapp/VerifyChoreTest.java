package com.cse403chorecenter.chorecenterapp;

import com.cse403chorecenter.chorecenterapp.ui.submit_chore.SubmitChoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class VerifyChoreTest {
    String jsonStr = "{\"Chores\": [" +
            "        {" +
            "            \"AcceptedTime\": null," +
            "            \"AccountId\": \"22\"," +
            "            \"AssignedTo\": null," +
            "            \"ChoreId\": \"A854430D-8025-4BB1-93A4-9630FDFAEC73\"," +
            "            \"CompletedTime\": null," +
            "            \"CreatedTime\": \"Sun, 09 May 2021 02:11:27 GMT\"," +
            "            \"Description\": \"Doing all the dishes from yesturday\"," +
            "            \"LastUpdateTime\": \"Sun, 09 May 2021 02:11:27 GMT\"," +
            "            \"Name\": \"Doing the dishes\"," +
            "            \"Points\": 4000," +
            "            \"Status\": \"Created\"," +
            "            \"VerifiedTime\": null" +
            "        }," +
            "        {" +
            "            \"AcceptedTime\": null," +
            "            \"AccountId\": \"22\"," +
            "            \"AssignedTo\": null," +
            "            \"ChoreId\": \"A854430D-8025-4BB1-93A4-9630FDFAEC73\"," +
            "            \"CompletedTime\": null," +
            "            \"CreatedTime\": \"Sun, 09 May 2021 02:11:27 GMT\"," +
            "            \"Description\": \"Doing all the dirty laundry\"," +
            "            \"LastUpdateTime\": \"Sun, 09 May 2021 02:11:27 GMT\"," +
            "            \"Name\": \"Doing laundry\"," +
            "            \"Points\": 2000," +
            "            \"Status\": \"Created\"," +
            "            \"VerifiedTime\": null" +
            "        }" +
            "    ]" +
            "}";

    @Test
    public void parseJSONTest() {
        ArrayList<SubmitChoreFragment.ChoreModel> mDataset = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray chores = jsonObj.getJSONArray("Chores");

            // looping through All Chores
            for (int i = 0; i < chores.length(); i++) {
                JSONObject c = chores.getJSONObject(i);
                mDataset.add(new SubmitChoreFragment.ChoreModel(c.getString("Name"), c.getLong("Points"),
                        c.getString("Description"), c.getString("ChoreId")));
            }

            assertEquals(mDataset.get(0).getName(), "Doing the dishes");
            assertEquals(mDataset.get(0).getDescription(), "Doing all the dishes from yesturday");
            assertEquals(mDataset.get(1).getPoints(), 2000);
            assertEquals(mDataset.get(1).getId(), "A854430D-8025-4BB1-93A4-9630FDFAEC73");
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
