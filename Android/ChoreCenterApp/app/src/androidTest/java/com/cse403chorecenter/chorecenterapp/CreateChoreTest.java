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
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CreateChoreTest {

    @Before
    public void fillTestAccountInfo() {
        UserLogin.ACCOUNT_ID = "frontend_test";
        UserLogin.ACCOUNT_DISPLAY_NAME = UserLogin.ACCOUNT_ID;
        UserLogin.ACCOUNT_ID_TOKEN = UserLogin.ACCOUNT_ID;
    }

    @Test
    public void testCreateChore() {
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            onView(withId(R.id.button_home_create_chore)).perform(click());

            // click without input
            Thread.sleep(250);
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("please input the chore name and chore points")));

            // non-integer points input
            onView(withId(R.id.editCreateChoreName)).perform(typeText("testing")).perform(closeSoftKeyboard());
            onView(withId(R.id.editCreateChorePoints)).perform(typeText("1000.12")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("please input a number for the chore points")));

            // valid input
            onView(withId(R.id.editCreateChoreName)).perform(clearText(), typeText("test")).perform(closeSoftKeyboard());
            onView(withId(R.id.editCreateChorePoints)).perform(clearText(), typeText("1000")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_create_chore)).perform(click());
            onView(withId(R.id.text_create_chore)).check(matches(withText("CREATED")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Important: Due to current unavailability of test database, we need to manually delete
        // chores created for testing. This also passively tests the delete chore functionality.
        try (ActivityScenario<ParentNavigation> ignored = ActivityScenario.launch(ParentNavigation.class)) {
            onView(withId(R.id.button_home_chore_list)).perform(click());

            Thread.sleep(250);
            onView(withId(R.id.verifyChoreRecyclerView))
                    .perform(actionOnItemAtPosition(0, TestViewAction.clickChildViewWithId(R.id.delete_icon)));

            Thread.sleep(250);
            onView(withText("Delete")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

            Thread.sleep(250);
            onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Delete successful")));
        } catch (InterruptedException e) {
            e.printStackTrace();
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
