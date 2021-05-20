package com.cse403chorecenter.chorecenterapp;

import com.cse403chorecenter.chorecenterapp.ui.redeem_reward.RedeemRewardFragment;
import com.cse403chorecenter.chorecenterapp.ui.reward_history.RewardHistoryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RewardHistoryTest {
    String jsonStr = "{\"RedeemedRewards\": [{\"ChildName\": \"Tom\", \"Description\": \"Get ice cream\",\"Name\": \"ice cream\"," +
            "\"RedeemedTime\": \"Sat, 15 May 2021 17:42:28 GMT\",\"RewardId\": \"11\"}," +
            "{\"ChildName\": \"Sam\", \"Description\": \"Get cake\",\"Name\": \"cake\"," +
            "\"RedeemedTime\": \"Sun, 16 May 2021 17:42:28 GMT\",\"RewardId\": \"22\"}] }";
    @Test
    public void parseJSONTest() {
        ArrayList<RewardHistoryFragment.ReedemedRewardModel> mDataset = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray rewards = jsonObj.getJSONArray("RedeemedRewards");

            // looping through All Rewards
            for (int i = 0; i < rewards.length(); i++) {
                JSONObject c = rewards.getJSONObject(i);
                mDataset.add(new RewardHistoryFragment.ReedemedRewardModel(c.getString("Name"), c.getString("ChildName"),
                        c.getString("Description"), c.getString("RewardId"), c.getString("RedeemedTime")));
            }

            assertEquals(mDataset.size(), 2);
            assertEquals(mDataset.get(0).getName(), "ice cream");
            assertEquals(mDataset.get(0).getDescription(), "Get ice cream");
            assertEquals(mDataset.get(0).getRedeemedTime(), "Sat, 15 May 2021 17:42:28 GMT");
            assertEquals(mDataset.get(0).getId(), "11");
            assertEquals(mDataset.get(0).getChildName(), "Tom");

            assertEquals(mDataset.get(1).getName(), "cake");
            assertEquals(mDataset.get(1).getDescription(), "Get cake");
            assertEquals(mDataset.get(1).getRedeemedTime(), "Sun, 16 May 2021 17:42:28 GMT");
            assertEquals(mDataset.get(1).getId(), "22");
            assertEquals(mDataset.get(1).getChildName(), "Sam");
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
