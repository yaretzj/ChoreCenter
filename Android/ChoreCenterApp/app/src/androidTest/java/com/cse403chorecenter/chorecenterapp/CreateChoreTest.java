package com.cse403chorecenter.chorecenterapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CreateChoreTest {

    @Test
    public void testCreateChoreFragmentNavigation() {
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            Application application = ApplicationProvider.getApplicationContext();
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(application);

            onView(withId(R.id.button_home_create_chore)).perform(click());

            // directly click create button
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("please input the chore name and chore points")));

            // non-integer points input
            onView(withId(R.id.editCreateChoreName)).perform(typeText("testing")).perform(closeSoftKeyboard());
            onView(withId(R.id.editCreateChorePoints)).perform(typeText("1000.12")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("please input a number for the chore points")));
        }
    }
}
