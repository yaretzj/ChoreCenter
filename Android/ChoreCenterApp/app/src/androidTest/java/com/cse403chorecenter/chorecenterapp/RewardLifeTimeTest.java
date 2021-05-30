package com.cse403chorecenter.chorecenterapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RewardLifeTimeTest {

    @Before
    public void fillTestAccountInfo() {
        UserLogin.ACCOUNT_ID = "frontend_test";
        UserLogin.ACCOUNT_DISPLAY_NAME = UserLogin.ACCOUNT_ID;
        UserLogin.ACCOUNT_ID_TOKEN = UserLogin.ACCOUNT_ID;
    }

    @Test
    public void testCreateReward() {
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            onView(withId(R.id.button_home_create_reward)).perform(click());

            // valid input
            onView(withId(R.id.editCreateReward)).perform(typeText("test")).perform(closeSoftKeyboard());
            onView(withId(R.id.editCreateRewardPoints)).perform(typeText("1000")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_create_reward)).perform(click());
            onView(withId(R.id.text_create_reward)).check(matches(withText("CREATED")));
        }

        // Redeem the reward TODO: requires delete redeemed reward API to automate redeem reward test
//        try (ActivityScenario<KidNavigation> ignored = ActivityScenario.launch(KidNavigation.class)) {
//            onView(withId(R.id.button_home_kid_redeem_reward)).perform(click());
//
//            // click redeem
//            onView(withId(R.id.redeemRewardBtn)).perform(click());
//            onView(withText("Redeem")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
//            onView(withId(R.id.snackbar_text)).check(matches(withText("Redeem successful")));
//        }

        // Delete the reward
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            onView(withId(R.id.button_home_all_rewards)).perform(click());
            onView(withId(R.id.parentDeleteRewardBtn)).perform(click());
            onView(withText("Delete")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.snackbar_text)).check(matches(withText("Delete successful")));
        }
    }
}
