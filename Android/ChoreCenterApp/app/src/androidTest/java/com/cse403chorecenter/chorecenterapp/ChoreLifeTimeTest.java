package com.cse403chorecenter.chorecenterapp;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.hamcrest.Matcher;
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
public class ChoreLifeTimeTest {

    @Before
    public void fillTestAccountInfo() {
        UserLogin.ACCOUNT_ID = "frontend_test";
        UserLogin.ACCOUNT_DISPLAY_NAME = UserLogin.ACCOUNT_ID;
        UserLogin.ACCOUNT_ID_TOKEN = UserLogin.ACCOUNT_ID;
    }

    @Test
    public void testCreateCompleteChore() {
        // Create a chore
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            waitViewShown(withId(R.id.button_home_create_chore));
            onView(withId(R.id.button_home_create_chore)).perform(click());

            // valid input
            waitViewShown(withId(R.id.editCreateChoreName));
            onView(withId(R.id.editCreateChoreName)).perform(typeText("test")).perform(closeSoftKeyboard());
            onView(withId(R.id.editCreateChorePoints)).perform(typeText("1000")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("CREATED")));
        }

        // Complete the chore
        try (ActivityScenario<KidNavigation> ignored = ActivityScenario.launch(KidNavigation.class)) {
            waitViewShown(withId(R.id.button_home_kid_submit_chore));
            onView(withId(R.id.button_home_kid_submit_chore)).perform(click());

            // click submit
            waitViewShown(withId(R.id.submitChoreBtn));
            onView(withId(R.id.submitChoreBtn)).perform(click());
            onView(withText("Completed")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.snackbar_text)).check(matches(withText("Submit successful")));
        }

        // Verify the chore TODO: requires delete verified chore API to automate verify chore test
//        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
//            onView(withId(R.id.button_home_chore_list)).perform(click());
//
//            // click verify
//            onView(withId(R.id.verifyChoreBtn)).perform(click());
//            onView(withText("Verify")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
//            onView(withId(R.id.snackbar_text)).check(matches(withText("Verify successful")));
//        }

        // Delete the chore
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            waitViewShown(withId(R.id.button_home_chore_list));
            onView(withId(R.id.button_home_chore_list)).perform(click());
            waitViewShown(withId(R.id.delete_icon));
            onView(withId(R.id.delete_icon)).perform(click());
            onView(withText("Delete")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.snackbar_text)).check(matches(withText("Delete successful")));
        }
    }

    public void waitViewShown(Matcher<View> matcher) {
        IdlingResource idlingResource = new ViewShownIdlingResource(matcher);///
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(matcher).check(matches(isDisplayed()));
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
