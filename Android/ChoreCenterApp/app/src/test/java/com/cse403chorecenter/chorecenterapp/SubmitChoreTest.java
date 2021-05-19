package com.cse403chorecenter.chorecenterapp;

import com.cse403chorecenter.chorecenterapp.ui.submit_chore.SubmitChoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SubmitChoreTest {
    String getChoresChild = "{\"Chores\": [" +
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
            "            \"Status\": \"Completed\"," +
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
    public void parseJsonTest() {
        ArrayList<SubmitChoreFragment.ChoreModel> list = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(getChoresChild);
            JSONArray chore_list = jsonObj.getJSONArray("Chores");
            for (int i = 0; i < chore_list.length(); i++) {
                JSONObject c = chore_list.getJSONObject(i);
                list.add(new SubmitChoreFragment.ChoreModel(c.getString("Name"), c.getLong("Points"),
                        c.getString("Description"), c.getString("ChoreId"), c.getString("Status")));
            }
            assertEquals(list.size(), 2);
            assertEquals(list.get(0).getName(), "Doing the dishes");
            assertEquals(list.get(0).getPoints(), 4000);
            assertEquals(list.get(0).getDescription(), "Doing all the dishes from yesturday");
            assertEquals(list.get(0).getId(), "A854430D-8025-4BB1-93A4-9630FDFAEC73");
            assertEquals(list.get(0).getStatus(), "Completed");

            assertEquals(list.get(1).getName(), "Doing laundry");
            assertEquals(list.get(1).getPoints(), 2000);
            assertEquals(list.get(1).getDescription(), "Doing all the dirty laundry");
            assertEquals(list.get(1).getId(), "A854430D-8025-4BB1-93A4-9630FDFAEC73");
            assertEquals(list.get(1).getStatus(), "Created");
        } catch (final JSONException e) {
            e.printStackTrace();
        }

    }
}

