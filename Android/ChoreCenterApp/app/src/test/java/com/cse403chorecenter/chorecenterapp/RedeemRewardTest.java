package com.cse403chorecenter.chorecenterapp;

import com.cse403chorecenter.chorecenterapp.ui.redeem_reward.RedeemRewardFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RedeemRewardTest {
    String jsonStr = "{\"Rewards\": [{\"RewardId\": \"103\", \"ParentGoogleAccountId\": \"e654\",\"RewardName\": \"ice cream\"," +
            "\"Description\": \"a very very hot ice cream\",\"Points\": 2000,\"CreatedTime\": \"2021-4-2\", \"UpdatedTime\": \"2021-5-2\"," +
            "\"NumberOfRedemptions\": 7 }, {\"RewardId\": \"105\",\"ParentGoogleAccountId\": \"e654\",\"RewardName\": \"hotpot\"," +
            "\"Description\": \"a very very cold hotpot\",\"Points\": 10000,\"CreatedTime\": \"2021-4-12\",\"UpdatedTime\": \"2021-5-11\"," +
            "\"NumberOfRedemptions\": 2 }] }";

    @Test
    public void parseJSONTest() {
        ArrayList<RedeemRewardFragment.RewardModel> mDataset = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray rewards = jsonObj.getJSONArray("Rewards");

            // looping through All Rewards
            for (int i = 0; i < rewards.length(); i++) {
                JSONObject c = rewards.getJSONObject(i);
                mDataset.add(new RedeemRewardFragment.RewardModel(c.getString("RewardName"), c.getString("Points"),
                        c.getString("Description"), c.getString("RewardId"), 0));
            }

            assertEquals(mDataset.get(0).getName(), "ice cream");
            assertEquals(mDataset.get(0).getDescription(), "a very very hot ice cream");
            assertEquals(mDataset.get(1).getPoints(), "10000");
            assertEquals(mDataset.get(1).getId(), "105");
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
